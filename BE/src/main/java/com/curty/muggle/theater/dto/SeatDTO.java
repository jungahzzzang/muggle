package com.curty.muggle.theater.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SeatDTO {

	private String seatId;
	
	private String seatNumber;
	
	private String zone; //구역
	
	private String floor; //층

	private String row;
	
	private int x;
	
	private int y;
	
	private String className;

	@JsonProperty("isWheelChair")
	private boolean isWheelChair;
}
