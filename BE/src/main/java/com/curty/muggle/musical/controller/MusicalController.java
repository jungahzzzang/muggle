package com.curty.muggle.musical.controller;

import com.curty.muggle.theater.dto.TheaterDTO;
import com.curty.muggle.musical.service.MusicalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Musical Controller", description = "Open API를 통해 뮤지컬 데이터를 가져오는 컨트롤러")
@RestController
@RequestMapping("/api/musical")
@RequiredArgsConstructor
public class MusicalController {

    private final MusicalService musicalService;

    @Operation(summary = "월별 뮤지컬 목록 가져오기", description = "월별 뮤지컬 목록 가져오기")
    @GetMapping("/getMonthMusical")
    public String getMonthMusical(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) {
        
    	return musicalService.getMonthMusical(startDate, endDate);
    }

    @Operation(summary = "뮤지컬 주간 랭킹 가져오기", description = "뮤지컬 주간 랭킹 가져오기")
    @GetMapping("/getRankMusical")
    public ResponseEntity<List<TheaterDTO>> getRankMusical(@RequestParam("date") String date, @RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) {
        
    	//return musicalService.getRankMusical(date, startDate, endDate);
    	return ResponseEntity.ok(musicalService.getRankMusical(date, startDate, endDate));
    }
}
