import axios from 'axios';
import { xmlToJson, getDateString } from '../util/util';

const RankMusicalApi = async () => {

    console.log('API_BASE_URL:', process.env.REACT_APP_API_BASE_URL);

    const API_BASE_URL = process.env.REACT_APP_API_BASE_URL;

    const response = await axios.get(`${API_BASE_URL}/api/musical/getRankMusical`, {
        params: {
            date: getDateString('today'),   // 오늘 날짜 기준으로 이번주 주간 랭킹 가져오기
            startDate: getDateString(),
            endDate: getDateString('lastday'),
        }
    });

   // const jsonData = xmlToJson(response.data);

    // const list = jsonData?.boxofs?.boxof?.map((item) => ({
    //     id: item.mt20id?._text,
    //     title: item.prfnm?._text,
    //     startDate: item.prfpdfrom?._text,
    //     endDate: item.prfpdto?._text,
    //     poster: item.poster?._text,
    //     prfpd: item.prfpd?._text,
    //     prfplcnm: item.prfplcnm?._text,
    // })) ?? [];

    const list = response.data.map((item) => ({
        id: item.mt20id,
        title: item.title,
        startDate: item.startDate,
        endDate: item.endDate,
        poster: item.poster,
        prfpd: item.prfpd,
        prfplcnm: item.theaterNm,
        theaterId: item.theaterId,
    }));

    return list;

        /*
        <boxofs>
            <boxof>
                <prfplcnm>세종문화회관 세종대극장</prfplcnm>
                <seatcnt>3022</seatcnt>
                <rnum>1</rnum>
                <poster>http://www.kopis.or.kr/upload/pfmPoster/PF_PF262644_250408_111826.jfif</poster>
                <prfpd>2025.05.31~2025.08.11</prfpd>
                <mt20id>PF262644</mt20id>
                <prfnm>팬텀</prfnm>
                <cate>뮤지컬</cate>
                <prfdtcnt>72</prfdtcnt>
                <area>서울</area>
            </boxof>
        </boxofs>
    */
}

export default RankMusicalApi;