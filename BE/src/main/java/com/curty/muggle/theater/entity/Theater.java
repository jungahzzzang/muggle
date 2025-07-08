package com.curty.muggle.theater.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "THEATER")
public class Theater {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "THEATER_ID")
	private Long theaterId; // musicalseeya.com 좌석배치도 ID
	
	@Column(name = "THEATER_NM")
    private String theaterNm; // 공연장명 (prfplcnm)
}
