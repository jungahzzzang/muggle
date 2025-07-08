package com.curty.muggle.theater.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SeatViewAvgDTO {

	private int seatId;
	private double avgViewRating;
}
