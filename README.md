# 🎭Muggle
[뮤글](https://muggle-frontend-396310251876.asia-northeast3.run.app/)은 뮤지컬 팬들을 위한 커뮤니티입니다. 공연장 좌석별 시야 후기를 작성하고, 실제 관객들의 생생한 후기를 확인할 수 있습니다.
메인 화면에서는 뮤지컬 주간 랭킹 데이터를 확인할 수 있으며, 팬들 간 자유롭게 소통할 수 있는 게시판도 제공합니다.

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

## 🙋‍♀️담당 기능
- 공연장 좌석 시야 리뷰 등록 및 조회
- KOPIS API를 통한 주간 랭킹 자동 수집
- 공연장별 좌석 배치도 데이터 시각화
- 평균 평점 기반 좌석 색상 변경 기능

## 💻실행 방법

### Installation
```bash
$ git clone https://github.com/jungahzzzang/muggle.git
````

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
## 📸 화면 구성
#### [API 문서](https://muggle-backend-396310251876.asia-northeast3.run.app/swagger-ui/index.html)

| 메인 페이지 | 공연장별 좌석배치도 |
| :-: |:-: |
| <img width="1861" height="959" alt="Image" src="https://github.com/user-attachments/assets/bb185ebf-e4e5-45ed-b5e7-4934b0f0b2e1" /> KOPIS Open API를 통해 주간 랭킹 데이터를 가져옴.| <img width="1832" height="863" alt="Image" src="https://github.com/user-attachments/assets/27721bfe-9f88-4a0d-8be2-fdcc0e54c177" /> -좌석 시야 평점 평균값에 따라 색깔 표시 <br/> -휠체어석 표시|
| 좌석 시야 리뷰 작성 | 좌석 리뷰 시야 상세  |
|<img width="853" height="691" alt="Image" src="https://github.com/user-attachments/assets/9e5130de-ad87-48fe-9533-c52fe29a0b16" />|<img width="838" height="761" alt="Image" src="https://github.com/user-attachments/assets/69a40b50-eba4-48c3-9c20-2da359babd99" />|

