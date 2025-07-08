FROM node:18

# Puppeteer 의존성 설치
RUN apt-get update && apt-get install -y \
  chromium \
  wget \
  ca-certificates \
  fonts-liberation \
  libappindicator3-1 \
  libasound2 \
  libatk-bridge2.0-0 \
  libatk1.0-0 \
  libcups2 \
  libdbus-1-3 \
  libgdk-pixbuf2.0-0 \
  libnspr4 \
  libnss3 \
  libx11-xcb1 \
  libxcomposite1 \
  libxdamage1 \
  libxrandr2 \
  xdg-utils \
  --no-install-recommends \
  && apt-get clean && rm -rf /var/lib/apt/lists/*

# Puppeteer 실행 시 필요한 환경 설정
ENV PUPPETEER_SKIP_CHROMIUM_DOWNLOAD=true
ENV PUPPETEER_EXECUTABLE_PATH=/usr/bin/chromium

# 작업 디렉토리 설정
WORKDIR /app

COPY package*.json ./
RUN npm install

COPY . .

COPY .env .env

# 포트 열기
EXPOSE 8080

CMD ["node", "index.js"]
