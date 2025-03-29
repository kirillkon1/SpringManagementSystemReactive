# Лёгкое окружение с JDK 17
FROM openjdk:17-jdk-slim
LABEL name="analytics-service-reactive"

WORKDIR /app

# Копируем файл сборки
COPY build/libs/*.jar app.jar

ENTRYPOINT ["java","-jar","/app/app.jar"]