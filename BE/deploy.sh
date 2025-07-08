#!/bin/bash
# 환경변수 로드
if [ -f .env.deploy ]; then
  source .env.deploy
else
  echo ".env.deploy 파일을 찾을 수 없습니다."
  exit 1
fi
# 설정값
REGION="asia-northeast3"
IMAGE_TAG="1.0"

FULL_IMAGE="${REGION}-docker.pkg.dev/${PROJECT_ID}/${REPO_NAME}/${IMAGE_NAME}:${IMAGE_TAG}"

echo "Gradle 빌드 시작..."
./gradlew clean bootJar

echo "Docker 멀티 아키텍처 이미지 빌드 및 푸시 중..."
docker buildx build \
  --platform linux/amd64,linux/arm64 \
  -t "$FULL_IMAGE" \
  --push .

echo "Cloud Run 배포 중 (환경변수 SPRING_PROFILES_ACTIVE=dev 포함)..."
gcloud run deploy "$SERVICE_NAME" \
  --image="$FULL_IMAGE" \
  --platform managed \
  --region "$REGION" \
  --allow-unauthenticated \
  --set-env-vars SPRING_PROFILES_ACTIVE=dev


if [ $? -eq 0 ]; then
  echo "Spring Boot 서비스 배포 성공!"
else
  echo "Spring Boot 배포 실패!"
  exit 1
fi
