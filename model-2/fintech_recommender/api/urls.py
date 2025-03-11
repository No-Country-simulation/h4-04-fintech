# urls.py
from django.urls import path
from .views import OnboardingView, RecommendationsView, InvestmentView

urlpatterns = [
    path('onboarding/', OnboardingView.as_view(), name='user-onboarding'),
    path('recommendations/<int:user_id>/', RecommendationsView.as_view(), name='user-recommendations'),
    path('invest/', InvestmentView.as_view(), name='user-invest'),
]