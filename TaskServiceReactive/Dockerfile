# -----------------------------
# 1) Стадия сборки
# -----------------------------
FROM gradle:7-jdk17 AS builder
WORKDIR /home/gradle/project

# Копируем Gradle Wrapper и конфиги сборки для кеширования зависимостей
COPY gradlew gradlew
COPY gradle gradle
COPY gradle/wrapper gradle/wrapper
COPY build.gradle.kts settings.gradle.kts ./

# Предварительно загружаем зависимости
RUN ./gradlew --no-daemon dependencies

# Копируем весь исходный код и прогоняем тесты + сборку
COPY . .
RUN ./gradlew --no-daemon clean build --info

# -----------------------------
# 2) Стадия рантайма
# -----------------------------
FROM openjdk:17-jdk-slim
WORKDIR /app

# Копируем готовый JAR из стадии сборки
COPY --from=builder /home/gradle/project/build/libs/*.jar app.jar

# Порт приложения
EXPOSE 8002

# Запуск
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
