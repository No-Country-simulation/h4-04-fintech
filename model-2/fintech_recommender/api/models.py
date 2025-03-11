from django.db import models

class UserProfile(models.Model):
    user_id = models.BigIntegerField(unique=True)  # Coincide con Long de Java
    risk_profile = models.CharField(max_length=50)  # 'MODERADO', 'CONSERVADOR', etc.
    income_monthly = models.DecimalField(max_digits=12, decimal_places=2)
    expenses_monthly = models.DecimalField(max_digits=12, decimal_places=2)
    percentage_save = models.DecimalField(max_digits=5, decimal_places=2)
    age = models.IntegerField()  # Calculado desde birthDate
    investment_experience = models.IntegerField()  # Mapear desde knowledge_level
    cluster = models.IntegerField(default=-1)
    total_investment = models.DecimalField(max_digits=12, decimal_places=2)  # Campo nuevo para inversión total
    
    class Meta:
        db_table = 'financing_profiles'  # Nombre de tabla si es necesario

class Investment(models.Model):
    investment_id = models.AutoField(primary_key=True)
    investment_type = models.CharField(max_length=100)
    risk_level = models.IntegerField()
    min_investment = models.DecimalField(max_digits=12, decimal_places=2)
    expected_return = models.DecimalField(max_digits=5, decimal_places=2)
    
    class Meta:
        db_table = 'investments'

class UserRecommendation(models.Model):
    user = models.ForeignKey(UserProfile, on_delete=models.CASCADE)
    investment = models.ForeignKey(Investment, on_delete=models.CASCADE)
    created_at = models.DateTimeField(auto_now_add=True)
    
    class Meta:
        db_table = 'user_recommendations'

class UserInvestment(models.Model):
    user = models.ForeignKey(UserProfile, on_delete=models.CASCADE)
    investment = models.ForeignKey(Investment, on_delete=models.CASCADE)
    amount = models.DecimalField(max_digits=12, decimal_places=2)
    created_at = models.DateTimeField(auto_now_add=True)

    class Meta:
        db_table = 'user_investments'

