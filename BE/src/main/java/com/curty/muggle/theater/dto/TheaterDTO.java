package com.curty.muggle.theater.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TheaterDTO {
	
	private String theaterId;
	private String mt20id;
    private String title;
    private String startDate;
    private String endDate;
    private String poster;
    private String prfpd;
    private String theaterNm; // 공연장명 (prfplcnm)

}
