#!/bin/bash
cd /home/ubuntu/app

# Docker Hub 로그인 (필요 시)
echo $DOCKER_HUB_PASSWORD | docker login -u $DOCKER_HUB_USERNAME --password-stdin

# docker-compose.yml을 사용하여 새 버전의 이미지를 pull 받고 컨테이너 실행
# DOCKERHUB_USER와 IMAGE_TAG는 CodeDeploy 환경변수를 통해 전달받거나,
# .env 파일에 미리 설정해둘 수 있습니다.
docker compose -f docker-compose.yml pull
docker compose -f docker-compose.yml up -d
