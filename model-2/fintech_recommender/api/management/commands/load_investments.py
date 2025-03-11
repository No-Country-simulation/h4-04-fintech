# management/commands/load_investments.py
from django.core.management.base import BaseCommand
import pandas as pd
from api.models import Investment

class Command(BaseCommand):
    help = 'Carga inversiones desde CSV'

    def handle(self, *args, **options):
        df = pd.read_csv('investment_data.csv')
        for _, row in df.iterrows():
            Investment.objects.update_or_create(
                investment_id=row['investment_id'],  # Usar el campo correcto
                defaults={
                    'investment_type': row['investment_type'],
                    'risk_level': row['risk_level'],
                    'min_investment': row['min_investment'],
                    'expected_return': row['expected_return']
                }
            )
        self.stdout.write(self.style.SUCCESS(f'Cargadas {len(df)} inversiones'))

    