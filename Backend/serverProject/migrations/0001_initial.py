# Generated by Django 3.0.7 on 2021-11-02 22:41

from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    initial = True

    dependencies = [
    ]

    operations = [
        migrations.CreateModel(
            name='R_info',
            fields=[
                ('rId', models.IntegerField(primary_key=True, serialize=False)),
                ('recipe_title', models.CharField(max_length=100)),
                ('serving', models.CharField(max_length=20)),
                ('cookingTime', models.CharField(max_length=20)),
                ('difficult', models.CharField(max_length=20)),
                ('recipe_source', models.CharField(max_length=100)),
                ('menu_img', models.CharField(max_length=100)),
                ('recipe_category', models.CharField(max_length=20)),
                ('created_at', models.DateTimeField(auto_now_add=True)),
                ('updated_at', models.DateTimeField(auto_now=True)),
            ],
        ),
        migrations.CreateModel(
            name='User',
            fields=[
                ('userId', models.CharField(max_length=100, primary_key=True, serialize=False)),
                ('accessToken', models.CharField(blank=True, max_length=200, null=True)),
                ('refreshToken', models.CharField(blank=True, max_length=200, null=True)),
                ('password', models.CharField(max_length=100)),
                ('created_at', models.DateTimeField(auto_now_add=True)),
                ('updated_at', models.DateTimeField(auto_now=True)),
            ],
        ),
        migrations.CreateModel(
            name='ranking_defaultRecipe',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('rank', models.IntegerField(auto_created=True)),
                ('rId', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, related_name='rankingRecipe', to='serverProject.R_info')),
            ],
        ),
        migrations.CreateModel(
            name='R_order',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('recipe_order', models.IntegerField()),
                ('description', models.CharField(max_length=100)),
                ('created_at', models.DateTimeField(auto_now_add=True)),
                ('updated_at', models.DateTimeField(auto_now=True)),
                ('rId', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='serverProject.R_info')),
            ],
        ),
        migrations.CreateModel(
            name='R_grade',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('grade', models.IntegerField()),
                ('created_at', models.DateTimeField(auto_now_add=True)),
                ('updated_at', models.DateTimeField(auto_now=True)),
                ('rId', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='serverProject.R_info')),
                ('userId', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='serverProject.User')),
            ],
        ),
        migrations.CreateModel(
            name='main_defaultRecipe',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('rId', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, related_name='mainRecipe', to='serverProject.R_info')),
            ],
        ),
    ]
