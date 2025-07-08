# React 앱 빌드
FROM node:18 as build

WORKDIR /app

COPY package*.json ./
RUN npm install

COPY . .
# REACT_APP_API_BASE_URL 환경변수로 React 앱 빌드
ARG REACT_APP_API_BASE_URL
ENV REACT_APP_API_BASE_URL=$REACT_APP_API_BASE_URL

RUN echo "REACT_APP_API_BASE_URL is $REACT_APP_API_BASE_URL"

RUN npm run build

# 2. nginx로 서비스
FROM nginx:alpine

# 빌드된 정적파일 복사
COPY --from=build /app/build /usr/share/nginx/html

# nginx 설정 템플릿 복사
COPY nginx.conf.template /etc/nginx/templates/nginx.conf.template

# 컨테이너 포트 노출 (Cloud Run 기본 8080)
EXPOSE 8080

# $PORT 환경변수 치환 후 nginx 실행
CMD ["/bin/sh", "-c", "envsubst '$PORT' < /etc/nginx/templates/nginx.conf.template > /etc/nginx/conf.d/default.conf && nginx -g 'daemon off;'"]
