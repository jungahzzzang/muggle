package com.curty.muggle.theater.controller;

import com.curty.muggle.theater.dto.SeatReviewDTO;
import com.curty.muggle.theater.dto.SeatViewAvgDTO;
import com.curty.muggle.theater.service.SeatReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;


@Tag(name = "Seat Review Controller", description = "좌석 리뷰 관련 API 컨트롤러")
@RestController
@RequestMapping("/api/review")
@RequiredArgsConstructor
public class SeatReviewController {

    private final SeatReviewService seatReviewService;

    @PostMapping("/save")
    @Operation(summary = "리뷰 저장", description = "좌석 시야 리뷰 저장")
    public ResponseEntity<?> saveReview(@RequestBody SeatReviewDTO dto) {

        Long reviewId = seatReviewService.saveReview(dto);
        return ResponseEntity.ok().body(Map.of("reviewId", reviewId));
    }

    @GetMapping("/{seatId}")
    @Operation(summary = "리뷰 목록", description = "저장된 좌석 시야 리뷰 불러오기")
    public ResponseEntity<List<SeatReviewDTO>> getSeatReviews(@PathVariable("seatId") int seatId) {
    	
        List<SeatReviewDTO> reviewList = seatReviewService.getReviewList(seatId);
        return ResponseEntity.ok(reviewList);
    }
    
    @PutMapping("/{reviewId}")
    @Operation(summary = "리뷰 수정", description = "좌석 시야 리뷰 수정하기")
    public ResponseEntity<?> updateReview(@PathVariable("reviewId") Long reviewId, @RequestBody SeatReviewDTO dto) {
        
    	seatReviewService.updateReview(reviewId, dto);
        return ResponseEntity.ok(Map.of("message", "좌석 리뷰가 수정되었습니다."));
    }

    @DeleteMapping("/{reviewId}")
    @Operation(summary = "리뷰 삭제", description = "좌석 시야 리뷰 삭제하기")
    public ResponseEntity<?> deleteReview(@PathVariable("reviewId") Long reviewId) {
        
    	seatReviewService.deleteReview(reviewId);
        return ResponseEntity.ok(Map.of("message", "좌석 리뷰가 삭제되었습니다."));
    }
    
    @GetMapping("/avg/{theaterId}")
    @Operation(summary = "좌석 시야 평점", description = "좌석 시야 별점에 대한 평점 계산하기")
    public ResponseEntity<List<SeatViewAvgDTO>> getAvgViewRating(@PathVariable("theaterId") Long theaterId) {
        
    	List<SeatViewAvgDTO> result = seatReviewService.getAvgViewRating(theaterId);
    	return ResponseEntity.ok(result);
    }
    
}
