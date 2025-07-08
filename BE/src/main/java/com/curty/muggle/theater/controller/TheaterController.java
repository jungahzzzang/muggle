package com.curty.muggle.theater.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.curty.muggle.theater.dto.SeatDTO;
import com.curty.muggle.theater.service.TheaterService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Theater Controller", description = "극장 좌석배치도 데이터를 가져오는 컨트롤러")
@RestController
@RequestMapping("/api/theater")
@RequiredArgsConstructor
public class TheaterController {
	
	private final TheaterService theaterService;

	@Operation(summary = "극장 좌석 배치도 크롤링", description = "극장 좌석 배치도 크롤링")
    @GetMapping("/getSeatMap")
    public ResponseEntity<List<SeatDTO>> getCrawlTheater(@RequestParam("theaterId") String theaterId) {
        
		List<SeatDTO> seatData = theaterService.getSeatMapPuppeteer(theaterId);
		
        return ResponseEntity.ok(seatData);
    }
}
