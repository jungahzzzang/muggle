import {useEffect, useState} from "react";
import axios from "axios";
import { useParams, useLocation } from "react-router-dom";
import Loading from "../../components/shared/Loading";
import SeatMap from "../../components/theater/SeatMap";
import SeatReviewModal from "../../components/theater/SeatReviewModal";

const TheaterSeatPage = () => {

    const API_BASE_URL = process.env.REACT_APP_API_BASE_URL;

    const { theaterId } = useParams();
    const [seatData, setSeatData] = useState(null);
    const [loading, setLoading] = useState(true);
    const [selectedSeat, setSelectedSeat] = useState(null);
    const location = useLocation();
    const theaterName = location.state?.theaterName;

    useEffect(() => {
        const fetchSeats = async () => {
            try {
                setLoading(true);
                const res = await axios.get(`${API_BASE_URL}/api/theater/getSeatMap?theaterId=${encodeURIComponent(theaterId)}`);
                setSeatData(res.data);
            } catch (error) {
                console.error("좌석 데이터 요청 실패", error);
            } finally {
                setLoading(false);
            }
        };

        fetchSeats();
    }, [theaterId]);

    if (loading) return <Loading />;
    if (!seatData || seatData.length === 0) return <div>해당 공연장의 좌석 배치도는 준비중입니다.</div>;

    return (
        <div>
            <div style={{marginTop: '130px', textAlign:'center'}}><h2>{theaterName} 좌석 배치도</h2></div>
            <SeatMap seatData={seatData} theaterId={theaterId}/>
            {selectedSeat && (
                <SeatReviewModal seat={selectedSeat} onClose={() => setSelectedSeat(null)} />
            )}
        </div>
    )
}

export default TheaterSeatPage;