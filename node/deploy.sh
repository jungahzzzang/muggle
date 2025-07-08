#!/bin/bash
if [ -f .env.deploy ]; then
  source .env.deploy
else
  echo ".env.deploy 파일을 찾을 수 없습니다."
  exit 1
fi

# 설정
PROJECT_ID="$PROJECT_ID"
REGION="asia-northeast3"
REPO_NAME="$REPO_NAME"
IMAGE_NAME="$IMAGE_NAME"
IMAGE_TAG="1.0"
SERVICE_NAME="$SERVICE_NAME"
FULL_IMAGE="${REGION}-docker.pkg.dev/${PROJECT_ID}/${REPO_NAME}/${IMAGE_NAME}:${IMAGE_TAG}"

# .env → 환경변수 문자열 변환
if [ ! -f .env ]; then
echo ".env 파일이 없습니다. 중단합니다."
exit 1
fi

ENV_VARS=$(cat .env | grep -v '^#' | xargs | sed 's/ /,/g')

echo "이미지 빌드 및 배포 시작..."
echo "이미지: $FULL_IMAGE"

# Docker 이미지 빌드 (M1 대응)
docker buildx build \
--platform linux/amd64 \
-t "$FULL_IMAGE" \
--push .

# Cloud Run 배포
gcloud run deploy "$SERVICE_NAME" \
--image="$FULL_IMAGE" \
--region="$REGION" \
--platform=managed \
--set-env-vars="$ENV_VARS" \
--allow-unauthenticated \
--memory=1Gi

if [ $? -eq 0 ]; then
echo "배포 성공!"
else
echo "배포 실패"
exit 1
fi
