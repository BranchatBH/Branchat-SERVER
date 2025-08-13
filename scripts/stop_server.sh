#!/bin/bash
# set -e를 제거하여 컨테이너가 없어도 스크립트가 실패하지 않도록 합니다.

# docker-compose.yml에 정의된 컨테이너 이름을 변수로 지정
CONTAINER_NAME="branchat-api"

# 이름으로 컨테이너 ID를 찾습니다. (실행 중이거나, 멈춰있거나 모두 포함)
EXISTING_CONTAINER_ID=$(docker ps -a -q -f name=^/${CONTAINER_NAME}$)

# 컨테이너가 존재하는 경우에만 중지 및 제거 명령을 실행
if [ "$EXISTING_CONTAINER_ID" ]; then
  echo "Stopping and removing existing container: ${CONTAINER_NAME} (${EXISTING_CONTAINER_ID})"
  docker stop ${EXISTING_CONTAINER_ID}
  docker rm ${EXISTING_CONTAINER_ID}
else
  echo "No existing container named '${CONTAINER_NAME}' found. Skipping cleanup."
fi
