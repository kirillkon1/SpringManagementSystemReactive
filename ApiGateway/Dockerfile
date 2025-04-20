# -----------------------------
# 1) Стадия сборки
# -----------------------------
FROM gradle:7-jdk17 AS builder
WORKDIR /home/gradle/project

# Копируем только скрипты сборки для кеширования зависимостей
COPY build.gradle.kts settings.gradle.kts ./

# Используем встроенный gradle-клиент, а не wrapper
RUN gradle --no-daemon dependencies

# Копируем весь проект и прогоняем тесты + сборку
COPY . .
RUN gradle --no-daemon clean build

# -----------------------------
# 2) Стадия рантайма
# -----------------------------
FROM openjdk:17-jdk-slim
WORKDIR /app

# Копируем готовый JAR
COPY --from=builder /home/gradle/project/build/libs/*.jar app.jar

EXPOSE 8050
ENTRYPOINT ["java","-jar","/app/app.jar"]
