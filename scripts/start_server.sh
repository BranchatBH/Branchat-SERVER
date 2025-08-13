#!/bin/bash
set -e

cd /home/ubuntu/app

source .env

docker logout

echo "$DOCKER_HUB_PASSWORD" | docker login -u "$DOCKERHUB_USERNAME" --password-stdin

export DOCKER_CREDENTIALS_STORE=""
docker compose --env-file ./.env up -d --pull always
