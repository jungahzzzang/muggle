package com.curty.muggle.musical.service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import com.curty.muggle.common.config.properties.KopisProperties;
import com.curty.muggle.theater.dto.TheaterDTO;
import com.curty.muggle.theater.repository.TheaterRepository;

@Service
public class MusicalService {
	
    private final KopisProperties kopisProperties;
    
    private final TheaterRepository theaterRepository;
    
    @Autowired
    public MusicalService(KopisProperties kopisProperties, TheaterRepository theaterRepository) {
		this.kopisProperties = kopisProperties;
		this.theaterRepository = theaterRepository;
    }
    
    
    public String callKopisApi(String endpoint, Map<String, String> params) {
        RestTemplate restTemplate = new RestTemplate();

		restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(kopisProperties.getUrl() + endpoint);

        // 쿼리 파라미터 동적 추가
        for (Map.Entry<String, String> entry : params.entrySet()) {
            builder.queryParam(entry.getKey(), entry.getValue());
        }

        String url = builder.toUriString();
        return restTemplate.getForObject(url, String.class);
    }

    public String getMonthMusical(String startDate, String endDate) {
    	
    	Map<String, String> params = new HashMap<>();
    	params.put("service", kopisProperties.getServiceKey());
    	params.put("stdate", startDate);
    	params.put("eddate", endDate);
    	params.put("cpage", "1");
    	params.put("rows", "10");
    	params.put("shcate", "GGGA");	// 장르 코드 : 뮤지컬
    	params.put("signgucodesub", "11");	// 지역 코드 : 서울
    	params.put("kidstate", "N"); // 아동 공연 여부
    	params.put("format", "json");

    	String response = callKopisApi("pblprfr", params);
    	
    	return response;

    }
    
    public String getRankMusicalOld(String date, String startDate, String endDate) {
    	
    	Map<String, String> params = new HashMap<>();
    	params.put("service", kopisProperties.getServiceKey());
    	params.put("date", date);
    	params.put("stdate", startDate);
    	params.put("eddate", endDate);
    	params.put("catecode", "GGGA");
    	params.put("area", "11");

    	String response = callKopisApi("boxoffice", params);
    	
    	return response;
    }
    
    public List<TheaterDTO> getRankMusical(String date, String startDate, String endDate) {
    	
    	Map<String, String> params = new HashMap<>();
    	params.put("service", kopisProperties.getServiceKey());
    	params.put("date", date);
    	params.put("stdate", startDate);
    	params.put("eddate", endDate);
    	params.put("catecode", "GGGA");
    	params.put("area", "11");

    	//String response = callKopisApi("boxoffice", params);
    	
    	String xml = callKopisApi("boxoffice", params);

        try {
            // XML → JSON 파싱
            JSONObject jsonObject = XML.toJSONObject(xml);
            JSONArray boxofs = jsonObject.getJSONObject("boxofs").getJSONArray("boxof");

            List<TheaterDTO> result = new ArrayList<>();

            for (int i = 0; i < Math.min(10, boxofs.length()); i++) {	//상위 10개만
                JSONObject item = boxofs.getJSONObject(i);

                String theaterName = item.optString("prfplcnm");

                // seatMapId 매핑
                String theaterId = theaterRepository.findIdByTheaterNm(theaterName);
                
                if (theaterId != null) {
                	result.add(new TheaterDTO(
                		theaterId,
                        item.optString("mt20id"),
                        item.optString("prfnm"),
                        item.optString("prfpdfrom"),
                        item.optString("prfpdto"),
                        item.optString("poster"),
                        item.optString("prfpd"),
                        theaterName
                	));
                } else {
                	result.add(new TheaterDTO(
                		null,
                        item.optString("mt20id"),
                        item.optString("prfnm"),
                        item.optString("prfpdfrom"),
                        item.optString("prfpdto"),
                        item.optString("poster"),
                        item.optString("prfpd"),
                        theaterName
                	));
                }

                
            }

            return result;

        } catch (JSONException e) {
            throw new RuntimeException("KOPIS 응답 파싱 실패", e);
        }
        
    }
}
