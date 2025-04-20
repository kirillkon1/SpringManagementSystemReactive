#!/bin/bash

# Завершаем скрипт при любой ошибке
set -e

#echo ">>>>>>СБОРКА МИКРОСЕРВСИОВ<<<<<<"
#echo ""
#
#bash ./build_all.sh


echo ">>>>>>ЗАПУСК DOCKER COMPOSE<<<<<<"
echo ""

cd ./deploy
#docker-compose up --build -d
docker-compose build --no-cache
docker-compose up -d
