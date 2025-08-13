#!/bin/bash
cd /home/ubuntu/app

# 실행 중인 컨테이너가 있으면 내리기
if [ "$(docker ps -q -f name=branchat-api)" ]; then
    docker compose -f docker-compose.yml down
fi
