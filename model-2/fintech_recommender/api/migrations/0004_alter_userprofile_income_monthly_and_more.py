# Generated by Django 4.2.5 on 2025-02-07 21:46

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('api', '0003_alter_userprofile_percentage_save'),
    ]

    operations = [
        migrations.AlterField(
            model_name='userprofile',
            name='income_monthly',
            field=models.DecimalField(decimal_places=2, max_digits=12),
        ),
        migrations.AlterField(
            model_name='userprofile',
            name='percentage_save',
            field=models.DecimalField(decimal_places=2, max_digits=5),
        ),
        migrations.AlterField(
            model_name='userprofile',
            name='total_investment',
            field=models.DecimalField(decimal_places=2, max_digits=12),
        ),
    ]
