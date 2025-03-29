#!/bin/bash

# Завершаем скрипт при любой ошибке
set -e

echo ">>>>>>BUILDING MICROSERVICES<<<<<<"
echo ""

bash ./build_all.sh


echo ">>>>>>STARTING DOCKER COMPOSE<<<<<<"
echo ""

cd ./deploy
docker-compose up --build -d