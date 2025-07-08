package com.curty.muggle.theater.repository;

import com.curty.muggle.theater.dto.SeatViewAvgDTO;
import com.curty.muggle.theater.entity.SeatReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatReviewRepository extends JpaRepository<SeatReview, Long> {

	@Query("SELECT r FROM SeatReview r WHERE r.seatId = :seatId ORDER BY r.updatedAt DESC")
    List<SeatReview> findBySeatId(@Param("seatId") int seatId);
	
	@Query("SELECT new com.curty.muggle.theater.dto.SeatViewAvgDTO(r.seatId, AVG(r.viewRating)) " +
	       "FROM SeatReview r WHERE r.theater.theaterId = :theaterId GROUP BY r.seatId")
	List<SeatViewAvgDTO> findAvgViewRating(@Param("theaterId") Long theaterId);
}
