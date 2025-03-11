from django.shortcuts import render
# views.py
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status
from .utils import RecommendationEngine
from .serializers import FinancingProfileOnboardingSerializer, UserRecommendationSerializer, UserInvestmentSerializer
from .models import UserRecommendation, UserProfile, Investment, UserInvestment

class OnboardingView(APIView):
    def post(self, request):
        print("🔍 Datos recibidos en Django:", request.data)  # Log de datos recibidos
        serializer = FinancingProfileOnboardingSerializer(data=request.data)
        if serializer.is_valid():
            print("✅ Datos válidos:", serializer.validated_data)  # Log de datos validados
            try:
                engine = RecommendationEngine()
                user_profile = engine.create_user_profile(serializer.validated_data)
                engine.generate_recommendations(user_profile.user_id)

                return Response({
                    'status': 'Perfil creado', 
                    'cluster': user_profile.cluster
                }, status=status.HTTP_201_CREATED)
            except Exception as e:
                print("❌ Error en el procesamiento:", str(e))  # Log de error
                return Response({'error': str(e)}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)
        print("⚠️ Errores de validación:", serializer.errors)  # Log de errores de validación
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

class RecommendationsView(APIView):
    def get(self, request, user_id):
        try:
            recommendations = UserRecommendation.objects.filter(user__user_id=user_id)
            if not recommendations.exists():
                return Response({
                    'message': 'No se encontraron recomendaciones para este usuario.'
                }, status=status.HTTP_404_NOT_FOUND)

            serializer = UserRecommendationSerializer(recommendations, many=True)
            return Response({
                'userId': user_id,
                'recommendations': serializer.data
            })
        except Exception as e:
            return Response({'error': str(e)}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)

class InvestmentView(APIView):
    def post(self, request):
        serializer = UserInvestmentSerializer(data=request.data)
        if serializer.is_valid():
            user_id = serializer.validated_data['userId']
            investment_id = serializer.validated_data['investmentId']
            amount = serializer.validated_data['amount']

            try:
                user = UserProfile.objects.get(user_id=user_id)
                investment = Investment.objects.get(pk=investment_id)
            except UserProfile.DoesNotExist:
                return Response({'error': 'Usuario no encontrado'}, status=status.HTTP_404_NOT_FOUND)
            except Investment.DoesNotExist:
                return Response({'error': 'Inversión no encontrada'}, status=status.HTTP_404_NOT_FOUND)

            try:
                UserInvestment.objects.create(user=user, investment=investment, amount=amount)
                # Dentro de InvestmentView.post() después de UserInvestment.objects.create(...)
                user.total_investment += amount
                user.save() 

                # Actualizar el clúster del usuario después de la inversión
                engine = RecommendationEngine()
                engine.update_cluster_on_investment(user)

                user.refresh_from_db()
                new_cluster = user.cluster or "No asignado"  # Asignación simplificada

                investment_type = getattr(investment, 'investment_type', 'No especificado')
                expected_return = getattr(investment, 'expected_return', 0.0)

                return Response({
                    "status": "Inversión registrada",
                    "investmentId": investment_id,
                    "amount": amount,
                    "investmentType": investment_type,
                    "expectedReturn": expected_return,
                    "new_cluster": new_cluster
                }, status=status.HTTP_201_CREATED)

            except Exception as e:
                return Response({'error': f'Error al registrar la inversión: {str(e)}'}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)

        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)



