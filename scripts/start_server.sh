#!/bin/bash
set -e

# CodeDeploy가 파일을 복사해준 위치로 이동
cd /home/ubuntu/app

# .env 파일에 저장된 변수들을 현재 셸 세션으로 로드
source .env

# 이전 로그인 정보나 잘못된 설정을 깨끗하게 지웁니다.
docker logout

# .env에 정의된 변수를 사용하여 Docker Hub에 로그인합니다.
echo "$DOCKER_HUB_PASSWORD" | docker login -u "$DOCKER_HUB_USERNAME" --password-stdin

# ### 👇 이 부분이 최종 해결책입니다 ###
# docker-compose가 gpg 같은 외부 자격 증명 도우미를 사용하지 않도록 강제합니다.
# 이렇게 하면 바로 위에서 성공한 'docker login' 정보를 사용하게 됩니다.
export DOCKER_CREDENTIALS_STORE=""

# docker-compose가 컨테이너를 실행합니다.
docker compose pull
docker compose up -d
