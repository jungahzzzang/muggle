# Muggle
+ 작업 기간 : 2025.06.09 ~ 2025.07.06
+ 참여 인원 : 3명
## 🛠️ Tech Stack

### Frontend
<img src="https://img.shields.io/badge/react-61DAFB?style=for-the-badge&logo=react&logoColor=black">

### Backend
<p>
<img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
<img src="https://img.shields.io/badge/node.js-339933?style=for-the-badge&logo=Node.js&logoColor=white">
</p>

### DevOps / Deployment
<p>
<img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=Docker&logoColor=white">
<img src="https://img.shields.io/badge/googlecloud-4285F4?style=for-the-badge&logo=googlecloud&logoColor=white">
</p>

### Version Control & Collaboration
<p>
<img src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white">
<img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white">
</p>

### Communication
<p>
<img src="https://img.shields.io/badge/Notion-000000?style=for-the-badge&logo=Notion&logoColor=white">
</p>

## 🎭주요 기능
- 공연장 좌석 시야 리뷰 등록 및 조회
- KOPIS API를 통한 주간 랭킹 자동 수집
- 공연장별 좌석 배치도 데이터 시각화
- 평균 평점 기반 좌석 색상 변경 기능

## 실행 방법
#### Backend
```bash
cd BE
./gradlew build
docker build -t muggle-backend .
docker run -p 8080:8080 muggle-backend
```
#### Frontend
```bash
cd FE
npm install
npm run dev
```
