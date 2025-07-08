import { useState, useEffect } from "react";
import axios from "axios";
import styles from "../../assets/css/SeatReviewModal.module.css";
import { useMember } from "../../context/MemberContext";

const SeatReviewModal = ({ seat, onClose, theaterId, onRefreshAvgViewRating }) => {

  const API_BASE_URL = process.env.REACT_APP_API_BASE_URL;

  const { member } = useMember(); // 로그인한 사용자 정보

  const [savedReview, setSavedReview] = useState([]);
  const [listCount, setListCount] = useState(3);
  const [isWriting, setIsWriting] = useState(false);
  const [editingReviewId, setEditingReviewId] = useState(null);

  const [review, setReview] = useState("");
  const [viewRating, setViewRating] = useState(0);
  const [lightRating, setLightRating] = useState(0);
  const [soundRating, setSoundRating] = useState(0);

  const fetchReviews = async () => {
    try {
      const res = await axios.get(`${API_BASE_URL}/api/review/${seat.seatId}`);
      setSavedReview(res.data);
    } catch (err) {
      console.error("리뷰 조회 실패:", err);
    }
  };

  useEffect(() => {
    fetchReviews();
  }, [seat.seatId]);

  const handleShowMore = () => setListCount((prev) => prev + 3);

  const resetForm = () => {
    setReview("");
    setViewRating(0);
    setLightRating(0);
    setSoundRating(0);
    setEditingReviewId(null);
    setIsWriting(false);
  };

  const handleSubmit = async () => {
    try {
      if (!member) return alert("로그인이 필요합니다.");

      if (editingReviewId) {
        // 수정 요청
        await axios.put(`${API_BASE_URL}/api/review/${editingReviewId}`, {
          theaterId,
          seatId: seat.seatId,
          floor: seat.floor,
          zone: seat.zone,
          row: seat.row,
          seatNumber: seat.seatNumber,
          review,
          viewRating,
          lightRating,
          soundRating,
        });
        alert("리뷰가 수정되었습니다.");
      } else {
        // 새 리뷰 작성
        await axios.post(`${API_BASE_URL}/api/review/save`, {
          theaterId,
          seatId: seat.seatId,
          floor: seat.floor,
          zone: seat.zone,
          row: seat.row,
          seatNumber: seat.seatNumber,
          review,
          viewRating,
          lightRating,
          soundRating,
          memberId: member.memberId,
        });
        alert("좌석 리뷰가 등록되었습니다.");
      }

      resetForm();
      fetchReviews();
      if (onRefreshAvgViewRating) onRefreshAvgViewRating();
    } catch (err) {
      console.error("리뷰 저장 실패:", err);
      alert("리뷰 저장에 실패했습니다.");
    }
  };

  const handleEdit = (r) => {
    setEditingReviewId(r.reviewId);
    setReview(r.review);
    setViewRating(r.viewRating);
    setLightRating(r.lightRating);
    setSoundRating(r.soundRating);
    setIsWriting(true);
  };

  const handleDelete = async (reviewId) => {
    if (!window.confirm("리뷰를 삭제하시겠습니까?")) return;
    try {
      await axios.delete(`${API_BASE_URL}/api/review/${reviewId}`);
      alert("리뷰가 삭제되었습니다.");
      fetchReviews();
      if (onRefreshAvgViewRating) onRefreshAvgViewRating();
    } catch (err) {
      console.error("리뷰 삭제 실패:", err);
      alert("리뷰 삭제에 실패했습니다.");
    }
  };

  const renderStars = (rating, setRating) => {
    return [...Array(5)].map((_, i) => (
      <span
        key={i}
        onClick={() => setRating(i + 1)}
        style={{
          fontSize: "24px",
          color: i < rating ? "#FFD700" : "#ccc",
          cursor: "pointer",
        }}
      >
        ★
      </span>
    ));
  };

  return (
    <div className={styles.modalOverlay}>
      <div className={styles.modalContent}>
        <div className={styles.modalHeader}>
          <h3>{seat.floor}층 {seat.zone}구역 {seat.row}열 {seat.seatNumber}번 좌석</h3>
          <button className={styles.closeButton} onClick={onClose}>✕</button>
        </div>

        {isWriting ? (
          <div className={styles.writeReview}>
            <h4>{editingReviewId ? "리뷰 수정" : "리뷰 작성"}</h4>
            <div><strong>시야</strong>{renderStars(viewRating, setViewRating)}</div>
            <div><strong>조명</strong>{renderStars(lightRating, setLightRating)}</div>
            <div><strong>음향</strong>{renderStars(soundRating, setSoundRating)}</div>
            <textarea
              placeholder="이 좌석의 리뷰를 작성해주세요."
              value={review}
              onChange={(e) => setReview(e.target.value)}
            />
            <div className={styles.modalButtons}>
              <button onClick={handleSubmit}>{editingReviewId ? "수정 완료" : "제출"}</button>
              <button onClick={resetForm}>취소</button>
            </div>
          </div>
        ) : (
          <>
            <div className={styles.reviewList}>
              <h4>리뷰 목록</h4>
              {savedReview.length === 0 ? (
                <p>등록된 리뷰가 없습니다.</p>
              ) : (
                savedReview.slice(0, listCount).map((r, idx) => (
                  <div key={r.reviewId || idx} className={styles.reviewItem}>
                    <div className={styles.reviewTop}>
                      <p><strong>시야:</strong> {r.viewRating} / 5</p>
                      {member && r.memberId === member.memberId && (
                        <div className={styles.reviewActions}>
                          <button onClick={() => handleEdit(r)}>수정</button>
                          <button onClick={() => handleDelete(r.reviewId)}>삭제</button>
                        </div>
                      )}
                    </div>
                    <p><strong>조명:</strong> {r.lightRating} / 5</p>
                    <p><strong>음향:</strong> {r.soundRating} / 5</p>
                    <p>{r.review}</p>
                    <hr />
                  </div>
                ))
              )}
              {savedReview.length > listCount && (
                <button onClick={handleShowMore} className={styles.moreButton}>더보기</button>
              )}
            </div>
            <div className={styles.writeTrigger}>
              <button onClick={() => setIsWriting(true)}>리뷰 작성하기</button>
            </div>
          </>
        )}
      </div>
    </div>
  );
};

export default SeatReviewModal;
