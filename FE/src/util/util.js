import {xml2json} from 'xml-js';

//날짜 가져오는 함수
export const getDateString = (type) => {
    const today = new Date();
    const year = today.getFullYear();
    const month = ('0'+(today.getMonth()+1)).slice(-2);
    const day = ('0' + today.getDate()).slice(-2);

    if (type === 'today') {
        return `${year}${month}${day}`;
    } else if (type === 'yesterday') {
        const yesterday = new Date(today);
        yesterday.setDate(today.getDate() - 1);
        const yYear = yesterday.getFullYear();
        const yMonth = ('0'+(yesterday.getMonth()+1)).slice(-2);
        const yDay = ('0'+yesterday.getDate()).slice(-2);
        return `${yYear}${yMonth}${yDay}`;
    } else if (type === 'lastday') {
        const lastDay = getLastDayOfMonth(today.getFullYear(), today.getMonth() + 1);
        return `${year}${month}${lastDay}`;
    } else {
        return `${year}${month}01`;
    }
}

export const getLastDayOfMonth = (year, month) => {
    const date = new Date(year, month, 0); // 다음 달 0일 = 해당 월 말일
    const lastDay = date.getDate();
    return ('0' + lastDay).slice(-2);
};

//xml to JSON
export const xmlToJson = (xmlData) => {
    try {
      const cleanedXml = xmlData
        .replace(/<script\s*\/>/gi, '')
        .replace(/<meta[^>]*\/>/gi, '')
        .replace(/<link[^>]*\/>/gi, '')
        .replace(/<style[^>]*>[\s\S]*?<\/style>/gi, '')
        .replace(/<head>[\s\S]*?<\/head>/gi, '');
  
      const json = xml2json(cleanedXml, {
        compact: true,
        ignoreDeclaration: true,
        ignoreInstruction: true,
        ignoreComment: true,
        ignoreCdata: false,
        ignoreDoctype: true,
        spaces: 2,
      });
  
      return JSON.parse(json);
    } catch (error) {
      console.error('❌ XML to JSON parsing error:', error);
      return null;
    }
  };

//뮤지컬만 필터링
export const filterOnlyMusical = (data) =>
data?.filter((perf) => perf.cate[0] === '뮤지컬');