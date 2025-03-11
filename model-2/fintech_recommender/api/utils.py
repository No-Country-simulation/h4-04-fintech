# utils.py
import joblib
import numpy as np
from django.db.models import Avg
from .models import UserProfile, Investment, UserRecommendation, UserInvestment
from django.utils import timezone
from datetime import datetime
import pandas as pd

class RecommendationEngine:
    def __init__(self):
        self.model = joblib.load('kmeans_model.joblib')  # Asegurar que la ruta sea correcta

    def calculate_risk_tolerance(self, risk_profile):
        # Mapear los valores de risk_profile a numéricos
        risk_mapping = {
            'CONSERVADOR': 1,
            'MODERADO': 2,
            'AGRESIVO': 3
        }
        return risk_mapping.get(risk_profile.upper(), 2)
    
    def calculate_age(self, birth_date):  # Asegúrate de que reciba "self"
        today = timezone.now().date()
        if isinstance(birth_date, datetime):
            birth_date = birth_date.date()
        
        age = today.year - birth_date.year
        
        if (today.month, today.day) < (birth_date.month, birth_date.day):
            age -= 1
        
        return age
    
    # En utils.py dentro de la clase RecommendationEngine
    def create_user_profile(self, onboarding_data):
        # Validar fecha de nacimiento
        try:
            birth_date = onboarding_data['birthDate']
            
            # Asegurarse de que birth_date es un objeto date, no un string
            if isinstance(birth_date, str):
                birth_date = datetime.strptime(birth_date, '%Y-%m-%d').date()

            age = self.calculate_age(birth_date)
            
            # Validar que la edad sea razonable
            if not (18 <= age <= 100):
                raise ValueError("La edad debe estar entre 18 y 100 años")
                
        except KeyError:
            raise ValueError("Fecha de nacimiento es requerida")
        except TypeError:
            raise ValueError("Formato de fecha inválido")

        # Crear perfil de usuario
        user_profile = UserProfile.objects.create(
            user_id=onboarding_data['userId'],
            risk_profile=onboarding_data['riskProfile'],
            income_monthly=onboarding_data['incomeMonthly'],
            expenses_monthly=onboarding_data['expensesMonthly'],
            percentage_save=onboarding_data['percentageSave'],
            age=age,
            investment_experience=self.map_experience(onboarding_data['riskProfile']),
            cluster=-1,
            total_investment=0.0  # Inicializar en 0    
        )
        
        # Ejecutar clustering inicial
        self.update_user_cluster(user_profile)
        return user_profile
        
    def map_experience(self, knowledge_level):
        # Mapear conocimiento a experiencia numérica
        experience_mapping = {
            'PRINCIPIANTE': 1,
            'INTERMEDIO': 3,
            'AVANZADO': 5
        }
        return experience_mapping.get(knowledge_level.upper(), 1)
    
    def update_user_cluster(self, user_profile):
        # Preparar features para el modelo usando pandas
        features = pd.DataFrame([{
            'age': user_profile.age,
            'income': float(user_profile.income_monthly),
            'investment_experience': user_profile.investment_experience,
            'risk_tolerance': self.calculate_risk_tolerance(user_profile.risk_profile), 
            
        }])
        
        # Predecir cluster
        user_profile.cluster = self.model.predict(features)[0]
        user_profile.save()
    
    def generate_recommendations(self, user_id):
        user = UserProfile.objects.get(user_id=user_id)
        print(f"User: {user.id}, Risk Profile: {user.risk_profile}")
        
        # Obtener usuarios dentro del mismo cluster
        cluster_users = UserProfile.objects.filter(cluster=user.cluster)
        print(f"Cluster Size: {cluster_users.count()}")

        # Calcular el riesgo promedio del cluster
        risk_mapping = {'CONSERVADOR': 1, 'MODERADO': 2, 'AGRESIVO': 3}
        
        # Mostrar los perfiles de riesgo de los usuarios del cluster
        cluster_risk_values = [risk_mapping.get(u.risk_profile, 2) for u in cluster_users]
        print(f"Cluster Risk Values: {cluster_risk_values}")
        
        total_risk = sum(cluster_risk_values)
        cluster_risk = total_risk / len(cluster_risk_values) if cluster_risk_values else 2
        print(f"Calculated Cluster Risk: {cluster_risk}")
        
        # Mapear el perfil de riesgo del usuario
        user_risk = risk_mapping.get(user.risk_profile, 2)
        print(f"User Risk: {user_risk}")

        # Filtrar las inversiones con el nivel de riesgo adecuado
        recommended_investments = Investment.objects.filter(risk_level__lte=user_risk)
        print(f"Recommended Investments Count: {recommended_investments.count()}")

        # Eliminar recomendaciones previas para este usuario
        UserRecommendation.objects.filter(user=user).delete()

        # Crear las recomendaciones para el usuario
        for inv in recommended_investments:
            print(f"Recommending investment: {inv.investment_type}, Risk Level: {inv.risk_level}")
            UserRecommendation.objects.create(user=user, investment=inv)
        
        return recommended_investments




    
    def update_cluster_on_investment(self, user):
        """
        Recalcula el cluster del usuario basado en sus inversiones.
        """
        investments = UserRecommendation.objects.filter(user=user).select_related('investment')
        
        if not investments.exists():
            return  # Si el usuario no ha invertido, no recalculamos
        
        risk_profile_value = self.calculate_risk_tolerance(user.risk_profile)

        features = pd.DataFrame([{
            'age': user.age,
            'income': float(user.income_monthly),
            'investment_experience': user.investment_experience,
            'risk_tolerance': risk_profile_value,  # Usamos el valor numérico
        }])

        # Predecir nuevo cluster y actualizar usuario
        user.cluster = self.model.predict(features)[0]
        user.save()




    def register_investment(self, user_id, investment_id):
        """
        Registra una inversión de un usuario y recalcula su cluster.
        """
        user = UserProfile.objects.get(user_id=user_id)
        investment = Investment.objects.get(investment_id=investment_id)
        
        # Registrar inversión
        UserInvestment.objects.create(user=user, investment=investment, amount=investment.min_investment)
        
        # Actualizar el total de inversión del usuario
        user.total_investment += investment.min_investment
        user.save()
        
        # Actualizar el cluster del usuario después de la inversión
        self.update_cluster_on_investment(user)

        return {'status': 'Inversión registrada y cluster actualizado', 'new_cluster': user.cluster}


    