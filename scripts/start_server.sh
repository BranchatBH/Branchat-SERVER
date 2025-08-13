#!/bin/bash
set -e

cd /home/ubuntu/app

source .env

docker logout

echo "$DOCKER_HUB_PASSWORD" | docker login -u "$DOCKERHUB_USERNAME" --password-stdin

# export DOCKER_CREDENTIALS_STORE=""  <-- 이제 필요 없으므로 삭제!
# --env-file ./.env                 <-- 이것도 보통 자동 인식되므로 삭제해도 무방

docker compose up -d --pull always
