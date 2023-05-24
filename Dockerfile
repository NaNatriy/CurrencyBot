# Используйте базовый образ с поддержкой Java
FROM adoptopenjdk:11-jdk-hotspot

# Установка рабочей директории внутри контейнера
WORKDIR /app

# Копирование файла pom.xml для загрузки зависимостей
COPY pom.xml .

# Копирование всех файлов проекта в контейнер
COPY . .

# Команда для запуска приложения при старте контейнера
CMD ["java", "-jar", "target/TelegramApi-0.0.1-SNAPSHOT.jar.jar"]
