#!/bin/bash
set -e

# CodeDeploy가 파일을 복사해준 위치로 이동
cd /home/ubuntu/app

# .env 파일에 저장된 변수들을 현재 셸 세션으로 로드
source .env

# ### 👇 이 부분을 추가/수정합니다. ###
# 1. 혹시 모를 이전 로그인 정보나 잘못된 설정을 깨끗하게 지웁니다.
docker logout

# 2. .env에 있는 변수를 사용하여 새로 로그인합니다.
echo "$DOCKER_HUB_PASSWORD" | docker login -u "$DOCKER_HUB_USERNAME" --password-stdin

# docker-compose가 컨테이너를 실행합니다.
docker compose pull
docker compose up -d
