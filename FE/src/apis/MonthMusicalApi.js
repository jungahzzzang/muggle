import axios from 'axios';
import { xmlToJson, getDateString } from '../util/util';

const MonthMusicalApi = async () => {

    const API_BASE_URL = process.env.REACT_APP_API_BASE_URL;

    const response = await axios.get(`${API_BASE_URL}/api/musical/getMonthMusical`, {
        params: {
            startDate: getDateString(),
            endDate: getDateString('lastday'),
        }
    });

    const jsonData = xmlToJson(response.data);

    const list = jsonData?.dbs?.db?.map((item) => ({
        id: item.mt20id?._text,
        title: item.prfnm?._text,
        startDate: item.prfpdfrom?._text,
        endDate: item.prfpdto?._text,
        poster: item.poster?._text,
    })) ?? [];

    return list;

    /*
    <dbs>
        <db>
            <mt20id>PF267750</mt20id>
            <prfnm>아름다운 사인</prfnm>
            <prfpdfrom>2025.06.25</prfpdfrom>
            <prfpdto>2025.06.29</prfpdto>
            <fcltynm>극장 동국</fcltynm>
            <poster>http://www.kopis.or.kr/upload/pfmPoster/PF_PF267750_250620_135955.gif</poster>
            <area>서울특별시</area>
            <genrenm>연극</genrenm>
            <openrun>N</openrun>
            <prfstate>공연예정</prfstate>
        </db>
    </dbs>
    */
}

export default MonthMusicalApi;