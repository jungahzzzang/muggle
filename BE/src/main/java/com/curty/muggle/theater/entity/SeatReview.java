package com.curty.muggle.theater.entity;

import com.curty.muggle.common.entity.BaseTime;
import com.curty.muggle.member.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "SEAT_REVIEW")
public class SeatReview extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REVIEW_ID", nullable = false, unique = true)
    private Long reviewId;

    @ManyToOne
    @JoinColumn(name = "THEATER_ID", nullable = false)
    private Theater theater;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    private Member member;

    @Column(name = "SEAT_ID", nullable = false)
    private int seatId;

    @Column(name = "SEAT_FLOOR", nullable = false)
    private String floor;

    @Column(name = "SEAT_ZONE", nullable = false)
    private String zone;

    @Column(name = "SEAT_ROW", nullable = false)
    private String row;

    @Column(name = "SEAT_NUMBER", nullable = false)
    private String seatNumber;

    @Column(name = "REVIEW", nullable = false, length = 1000)
    private String review;

    @Column(name = "VIEW_RATING", nullable = false)
    private int viewRating;
    
    @Column(name = "LIGHT_RATING", nullable = false)
    private int lightRating;
    
    @Column(name = "SOUND_RATING", nullable = false)
    private int soundRating;
}
