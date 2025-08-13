#!/bin/bash
set -e

cd /home/ubuntu/app

source .env

docker logout

echo "$DOCKER_HUB_PASSWORD" | docker login -u "$DOCKERHUB_USERNAME" --password-stdin

# 시스템이 자동으로 credsStore를 추가하더라도, 이 명령어가 일시적으로 무시시킵니다.
export DOCKER_CREDENTIALS_STORE=""

# --pull 옵션으로 pull과 up을 한번에 실행
docker compose --env-file ./.env up -d --pull
