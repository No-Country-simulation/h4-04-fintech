# serializers.py
from rest_framework import serializers
from .models import UserProfile, UserRecommendation, Investment

class FinancingProfileOnboardingSerializer(serializers.Serializer):
    userId = serializers.IntegerField()  # ID del usuario
    riskProfile = serializers.ChoiceField(choices=['CONSERVADOR', 'MODERADO', 'AGRESIVO'])  # Elegir entre perfiles de riesgo
    incomeMonthly = serializers.DecimalField(max_digits=12, decimal_places=2)  # Ingreso mensual
    expensesMonthly = serializers.DecimalField(max_digits=12, decimal_places=2)  # Gastos mensuales
    percentageSave = serializers.DecimalField(max_digits=5, decimal_places=2)  # Porcentaje de ahorro mensual
    birthDate = serializers.DateField()  # Fecha de nacimiento
    investmentExperience = serializers.ChoiceField(choices=['PRINCIPIANTE', 'INTERMEDIO', 'AVANZADO'])  # Experiencia en inversiones

class InvestmentSerializer(serializers.ModelSerializer):
    class Meta:
        model = Investment
        fields = '__all__'

class UserRecommendationSerializer(serializers.ModelSerializer):
    investment = InvestmentSerializer()  # Relacionar las inversiones con las recomendaciones

    class Meta:
        model = UserRecommendation
        fields = ['investment', 'created_at']  # Mostrar la inversión y la fecha de creación de la recomendación

class UserInvestmentSerializer(serializers.Serializer):
    userId = serializers.IntegerField()  # ID del usuario
    investmentId = serializers.IntegerField()  # ID de la inversión
    amount = serializers.DecimalField(max_digits=12, decimal_places=2)   # Monto invertido
