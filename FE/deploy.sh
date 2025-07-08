#!/bin/bash

source .env.deploy

# 설정값
REGION="asia-northeast3"
IMAGE_TAG="1.0"

FULL_IMAGE="${REGION}-docker.pkg.dev/${PROJECT_ID}/${REPO_NAME}/${IMAGE_NAME}:${IMAGE_TAG}"

echo "기존 빌드 폴더 제거 중..."
rm -rf build

echo "React 빌드 중..."
npm run build

echo "Docker 이미지 빌드 및 푸시 (빌드 타임 환경변수 포함)..."
docker buildx build \
  --platform=linux/amd64 \
  --build-arg REACT_APP_API_BASE_URL="${REACT_APP_API_BASE_URL}" \
  -t "$FULL_IMAGE" \
  --push .

echo "Cloud Run 배포 중..."
gcloud run deploy "$SERVICE_NAME" \
  --image="$FULL_IMAGE" \
  --platform=managed \
  --region="$REGION" \
  --allow-unauthenticated

if [ $? -eq 0 ]; then
  echo "React 서비스 배포 성공!"
else
  echo "chmod +x deploy.sh
React 배포 실패!"
  exit 1
fi
