const express = require('express');
const app = express();
const port = process.env.PORT || 8080;

const runSeatCrawl = require('./seat-crawl');
const runTheaterCrawl = require('./theater-crawl');

// 기본 테스트용 엔드포인트
app.get('/', (req, res) => {
  res.send('muggle-node 서버 실행 중!');
});

app.get('/crawl/seat', async (req, res) => {
    try {
        const theaterId = req.query.theaterId;
        if (!theaterId) return res.status(400).json({ error: 'theaterId required' });

        const seatData = await runSeatCrawl(theaterId);
        res.json(seatData);
    } catch (error) {
        console.error(error);
        res.status(500).json({ error: '서버 에러' });
    }
});

// /crawl/theater → theater-crawl.js 실행
app.get('/crawl/theater', async (req, res) => {
  const keyword = req.query.keyword;
  try {
    const result = await runTheaterCrawl(keyword); // 함수 실행
    if (result) res.json(result);
    res.status(200).send({ message: 'theater-crawl 완료', result });
  } catch (err) {
    console.error('theater-crawl 실패:', err);
    res.status(500).json({ error: 'theater 크롤링 실패', details: err.message });
  }
});

app.listen(port, () => {
  console.log(`muggle-node 서버가 ${port} 포트에서 실행 중`);
});
