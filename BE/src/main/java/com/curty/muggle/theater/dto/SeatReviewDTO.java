package com.curty.muggle.theater.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeatReviewDTO {

    private Long theaterId;
    private Long reviewId;
    private Long memberId;
    private int seatId;
    private String floor;
    private String zone;
    private String row;
    private String seatNumber;
    private String review;
    private int viewRating;
    private int lightRating;
    private int soundRating;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
