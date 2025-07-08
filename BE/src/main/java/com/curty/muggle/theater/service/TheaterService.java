package com.curty.muggle.theater.service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import com.curty.muggle.common.config.properties.ClientProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.curty.muggle.common.config.properties.CrawlProperties;
import com.curty.muggle.theater.dto.SeatDTO;
import com.curty.muggle.theater.dto.TheaterResponseDTO;


@Service
public class TheaterService {
	
	private final CrawlProperties crawlProperties;
	
	private final ClientProperties clientProperties;
	
	private final RestTemplate restTemplate;
	
	@Autowired
	public TheaterService(CrawlProperties crawlProperties, ClientProperties clientProperties, RestTemplate restTemplate) {
		this.crawlProperties = crawlProperties;
		this.clientProperties = clientProperties;
		this.restTemplate = restTemplate;
	}
	
	public List<SeatDTO> getSeatMapPuppeteer(String theaterId) {

		String url = clientProperties.getNodeUrl() + "/crawl/seat?theaterId=" + theaterId;
		
		try {
			ResponseEntity<SeatDTO[]> response = restTemplate.getForEntity(url, SeatDTO[].class);
			return Arrays.asList(response.getBody());
		} catch (Exception e) {
			throw new RuntimeException("Node.js 서버로부터 좌석 정보를 가져오는 데 실패했습니다.", e); // <-추후 공통 에러 코드로 수정 필요
		}
	}
	
	public String getTheaterPuppeteer(String keyword) {
		
		String url = clientProperties.getNodeUrl() + "/crawl/theater?keyword=" + URLEncoder.encode(keyword, StandardCharsets.UTF_8);
		
		try {
			ResponseEntity<TheaterResponseDTO> response = restTemplate.getForEntity(url, TheaterResponseDTO.class);
			TheaterResponseDTO body = response.getBody();
			
			if (body != null && body.getTheaterId() != null) {
				return (String) body.getTheaterId();
			}
		} catch (Exception e) {
			System.err.println("크롤링 실패: " + e.getMessage());
		}
		
		return null;
	}

}

