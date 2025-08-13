#!/bin/bash
set -e

cd /home/ubuntu/app

source .env

docker logout

echo "$DOCKER_HUB_PASSWORD" | docker login -u "$DOCKERHUB_USERNAME" --password-stdin

docker compose --env-file ./.env up -d --pull
