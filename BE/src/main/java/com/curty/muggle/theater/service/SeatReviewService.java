package com.curty.muggle.theater.service;

import com.curty.muggle.member.entity.Member;
import com.curty.muggle.member.repository.MemberRepository;
import com.curty.muggle.theater.dto.SeatReviewDTO;
import com.curty.muggle.theater.dto.SeatViewAvgDTO;
import com.curty.muggle.theater.entity.SeatReview;
import com.curty.muggle.theater.entity.Theater;
import com.curty.muggle.theater.repository.SeatReviewRepository;
import com.curty.muggle.theater.repository.TheaterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SeatReviewService {

    private final SeatReviewRepository seatReviewRepository;
    private final MemberRepository memberRepository;
    private final TheaterRepository theaterRepository;

    public Long saveReview(SeatReviewDTO dto) {
        Member member = memberRepository.findByMemberId(dto.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
        Theater theater = theaterRepository.findByTheaterId(dto.getTheaterId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 공연장입니다."));

        SeatReview review = SeatReview.builder()
                .member(member)
                .theater(theater)
                .seatId(dto.getSeatId())
                .floor(dto.getFloor())
                .zone(dto.getZone())
                .row(dto.getRow())
                .seatNumber(dto.getSeatNumber())
                .review(dto.getReview())
                .viewRating(dto.getViewRating())
                .lightRating(dto.getLightRating())
                .soundRating(dto.getSoundRating())
                .build();

        SeatReview saved = seatReviewRepository.save(review);
        return saved.getReviewId();
    }

    public List<SeatReviewDTO> getReviewList(int seatId) {
        return seatReviewRepository.findBySeatId(seatId).stream()
                .map(review -> SeatReviewDTO.builder()
                        .theaterId(review.getTheater().getTheaterId())
                        .reviewId(review.getReviewId())
                        .memberId(review.getMember().getMemberId())
                        .seatId(review.getSeatId())
                        .floor(review.getFloor())
                        .zone(review.getZone())
                        .row(review.getRow())
                        .seatNumber(review.getSeatNumber())
                        .review(review.getReview())
                        .viewRating(review.getViewRating())
                        .lightRating(review.getLightRating())
                        .soundRating(review.getSoundRating())
                        .createdAt(review.getCreatedAt())
                        .updatedAt(review.getUpdatedAt())
                        .build())
                .collect(Collectors.toList());
    }
    
    public void updateReview(Long reviewId, SeatReviewDTO dto) {
        SeatReview review = seatReviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 좌석 리뷰입니다."));
        review.setReview(dto.getReview());
        review.setViewRating(dto.getViewRating());
        review.setLightRating(dto.getLightRating());
        review.setSoundRating(dto.getSoundRating());
        seatReviewRepository.save(review);
    }

    public void deleteReview(Long reviewId) {
    	
        SeatReview review = seatReviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 좌석 리뷰입니다."));
        seatReviewRepository.delete(review);
    }
    
    public List<SeatViewAvgDTO> getAvgViewRating(Long theaterId) {
    	
    	return seatReviewRepository.findAvgViewRating(theaterId);
    }
}
