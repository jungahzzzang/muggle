import { useState, useEffect, useCallback } from 'react';
import styles from '../../assets/css/SeatMap.module.css';
import axios from 'axios';
import { FaWheelchair } from 'react-icons/fa';
import SeatReviewModal from '../theater/SeatReviewModal';

const SeatMap = ({ seatData, theaterId }) => {

  const API_BASE_URL = process.env.REACT_APP_API_BASE_URL;

  const [activeFloor, setActiveFloor] = useState("1");
  const [selectedSeat, setSelectedSeat] = useState(null);
  const [avgViewRating, setAvgViewRating] = useState({});

  const fetchAvgViewRating = useCallback(async () => {
    try {
      const res = await axios.get(`${API_BASE_URL}/api/review/avg/${theaterId}`);
      const avgMap = {};
      res.data.forEach(avg => {
        avgMap[avg.seatId] = avg.avgViewRating;
      });
      setAvgViewRating(avgMap);
    } catch (err) {
      console.error("좌석 시야 평균 평점 조회 실패:", err);
    }
  }, [theaterId]);

  useEffect(() => {
    fetchAvgViewRating();
  }, [fetchAvgViewRating]);

  // 좌석 색깔 지정
  const getSeatColorClass = (seatId) => {
    const rating = avgViewRating[seatId];
    if (rating === undefined) return styles.noRating;
    if (rating >= 4.5) return styles.ratingExcellent;
    if (rating >= 3.5) return styles.ratingGood;
    if (rating >= 2.5) return styles.ratingAverage;
    return styles.ratingPoor;
  };
  
  // 층별로 그룹핑
  const groupedFloor = seatData.reduce((acc, seat) => {
    if (!acc[seat.floor]) acc[seat.floor] = [];
    acc[seat.floor].push(seat);
    return acc;
  }, {});

  const currentFloorSeats = groupedFloor[activeFloor] || [];

  // 구역별 그룹핑
  const groupedZone = currentFloorSeats.reduce((acc, seat) => {
    if (!acc[seat.zone]) acc[seat.zone] = [];
    acc[seat.zone].push(seat);
    return acc;
  }, {});

  const floorKeys = Object.keys(groupedFloor).sort();
  const zoneKeys = Object.keys(groupedZone).sort();

  return (
    <div>
      {/* 탭 버튼 */}
      <div className={styles.tabs}>
        {floorKeys.map((floor) => (
          <button
            key={floor}
            onClick={() => setActiveFloor(floor)}
            className={`${styles.tab} ${activeFloor === floor ? styles.activeTab : ''}`}
          >
            {floor}층
          </button>
        ))}
      </div>

      <div className={styles.legendWrapper}>
        <div className={styles.legend}>
          <div className={styles.legendItem}>
            <span className={`${styles.legendBox} ${styles.ratingWorst}`}></span>
          </div>
          <div className={styles.legendItem}>
            <span className={`${styles.legendBox} ${styles.ratingVeryPoor}`}></span>
          </div>
          <div className={styles.legendItem}>
            <span className={`${styles.legendBox} ${styles.ratingPoor}`}></span>
          </div>
          <div className={styles.legendItem}>
            <span className={`${styles.legendBox} ${styles.ratingAverage}`}></span>
          </div>
          <div className={styles.legendItem}>
            <span className={`${styles.legendBox} ${styles.ratingGood}`}></span>
          </div>
          <div className={styles.legendItem}>
            <span className={`${styles.legendBox} ${styles.ratingExcellent}`}></span>
          </div>
        </div>
      </div>

      {/* 선택된 층만 표시 */}
      {groupedFloor[activeFloor] && (
        <div className={styles.floor}>
          <div>
            {groupedFloor[activeFloor].map((seat) => (
              <div
                key={seat.seatId}
                className={`${styles.seat} ${getSeatColorClass(seat.seatId)} ${seat.isWheelChair ? styles.wheelchair : ""}`}
                style={{
                  left: `${seat.x}px`,
                  top: `${seat.y}px`,
                }}
                onClick={() => setSelectedSeat(seat)}
              >
                {seat.isWheelChair ? <FaWheelchair size={18} color="#2b2b2b" /> : seat.seatNumber}
              </div>
            ))}
          </div>
        </div>
      )}

      {/* 좌석 클릭 시 모달 표시 */}
      {selectedSeat && (
        <SeatReviewModal
          seat={selectedSeat}
          onClose={() => setSelectedSeat(null)}
          theaterId={theaterId}
          onRefreshAvgViewRating={fetchAvgViewRating}
        />
      )}
    </div>
  );
};

export default SeatMap;