# Микросервисы с использованием Spring Weblux

Этот документ описывает процесс развёртывания и запуска микросервисного стека на локальной машине с использованием Docker и Docker Compose.

## Пререквизиты

Перед началом убедитесь, что на вашей машине установлены:

- **Git** 
- **Docker Engine** 
- **Docker Compose** 

## Шаг 1. Клонирование репозитория

```bash
git clone https://github.com/kirillkon1/SpringManagementSystemReactive.git
```

### Перейти в директорию scripts/deploy

```bash
cd scripts/deploy
```

## Шаг 2. Настройка переменных окружения

### Скопировать шаблон файла переменных окружения:

```bash
cp .env.example .env
```

```
# Ports
## Environment
ENV_API_GATEWAY_HOST=http://127.0.0.1
ENV_API_GATEWAY_PORT=8050

## Microservices
ENV_USERS_SERVICE_PORT=8001
ENV_TASKS_SERVICE_PORT=8002
ENV_PROJECTS_SERVICE_PORT=8003
ENV_ANALYTICS_SERVICE_PORT=8004

#EUREKA
ENV_EUREKA_SERVER_HOST=http://eureka-server:8761/eureka/
ENV_EUREKA_SERVER_PORT=8761

#Microservices urls
ENV_EUREKA_USERS_SERVICE_URL=http://sr-user-service:8001
ENV_EUREKA_TASKS_SERVICE_URL=http://sr-task-service:8002
ENV_EUREKA_PROJECTS_SERVICE_URL=http://sr-project-service:8003
ENV_EUREKA_ANALYTICS_SERVICE_URL=http://sr-analytics-service:8004

# PostgreSQL
ENV_APP_POSTGRESQL_HOST=postgres
ENV_APP_POSTGRESQL_PORT=5432

ENV_APP_POSTGRESQL_USER=postgres
ENV_APP_POSTGRESQL_PASSWORD=postgres

## PostgreSQL database name
ENV_PROJECTS_SERVICE_DATABASE=projects_db
ENV_TASKS_SERVICE_DATABASE=tasks_db
ENV_USERS_SERVICE_DATABASE=users_db
ENV_ANALYTICS_SERVICE_DATABASE=analytics_db

#Kafka
ENV_KAFKA_URL=kafka:9092
```

## Шаг 3. Сборка образов

### В директории `scripts/deploy` выполнить следующую команду

```bash
docker-compose up --build -d
```

### Эта команда выполнит следующие действия:

#### 1. Загрузит зависимости через Gradle
#### 2. Прогонит unit‑тесты
#### 3. Соберёт исполняемый JAR
#### 4. Сформирует Docker‑образ на базе OpenJDK
#### 5. Запустит все контейнеры в фоновом режиме (флаг -d)

