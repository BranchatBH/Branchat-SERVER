#!/bin/bash
set -e

cd /home/ubuntu/app

source .env

docker logout

echo "$DOCKER_HUB_PASSWORD" | docker login -u "$DOCKERHUB_USERNAME" --password-stdin

export DOCKER_CREDENTIALS_STORE=""

# ### 👇 여기가 최종 수정 지점입니다. ###
# --pull 뒤에 'always'를 추가하여 항상 새 이미지를 받도록 합니다.
docker compose --env-file ./.env up -d --pull always
