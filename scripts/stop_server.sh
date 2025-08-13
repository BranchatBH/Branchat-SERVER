#!/bin/bash
# CodeDeploy가 파일을 복사해준 위치로 이동
cd /home/ubuntu/app

# docker-compose.yml 파일이 존재하는 경우에만 down 명령어를 실행
if [ -f docker-compose.yml ]; then
    docker compose down
fi
