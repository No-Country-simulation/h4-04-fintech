# Usa una imagen base con Python
FROM python:3.11

# Establece el directorio de trabajo
WORKDIR /app

# Instala dependencias del sistema necesarias para PostgreSQL
RUN apt-get update && apt-get install -y \
    pkg-config \
    libpq-dev

# Copia el archivo de requerimientos
COPY requirements.txt .

# Instala las dependencias de Python
RUN pip install --no-cache-dir -r requirements.txt

# Copia el resto del código
COPY . .

# Comando de inicio
CMD ["python", "manage.py", "runserver", "0.0.0.0:8000"]


