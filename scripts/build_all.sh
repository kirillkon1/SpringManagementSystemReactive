#!/bin/bash

# Завершаем скрипт при любой ошибке
set -e

# Task  Service
echo "Building TaskServiceReactive..."
cd ../TaskServiceReactive
./gradlew clean build -x test


# Project  Service
echo "Building ProjectServiceReactive..."
cd ../ProjectServiceReactive
./gradlew clean build -x test


# User  Service
echo "Building UserServiceReactive..."
cd ../UserServiceReactive
./gradlew clean build -x test


# AnalyticsService
echo "Building AnalyticsServiceReactive..."
cd ../AnalyticsServiceReactive
./gradlew clean build -x test


# API Gateway
echo "Building ApiGateway..."
cd ../ApiGateway
./gradlew clean build -x test