package com.ras.demo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ras.demo.dto.AfreecaBjBroadDTO;
import com.ras.demo.dto.AfreecaBjDTO;
import com.ras.demo.dto.AfreecaBjListDTO;
import com.ras.demo.mapper.AfreecaBjMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AfreecaBjListService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AfreecaBjMapper afreecaBjMapper;

    @Autowired
    private WebClient webClient;


//    public Mono<String> saveBjsFromRemote() {
//        return webClient.post()
//                .uri("http://localhost:8090/api/send")
//                .retrieve()
//                .bodyToMono(AfreecaBjListDTO.class)
//                .map(bjListDto -> {
//                    bjListDto.getAllBj().forEach(this::insertBj);
//                    return "All bjs have been successfully saved to the database";
//                });
//    }

    public int returnBroadNoIsNull(){
        return afreecaBjMapper.returnBroadNoIsNull();
    }

    public void updateBroadNoBatch() throws JsonProcessingException {
        List<String> bjIds;

        while (!(bjIds = afreecaBjMapper.selectNullBroadNo()).isEmpty()) {
            List<Map<String, Object>> updateList = new ArrayList<>();

            for (String bjId : bjIds) {
                // 외부 서비스 호출
                Integer broadNo = getBroadNoFromExternalService(bjId);

                if (broadNo != null) {
                    Map<String, Object> updateMap = new HashMap<>();
                    updateMap.put("bjId", bjId);
                    updateMap.put("broadNo", broadNo);
                    updateList.add(updateMap);
                }
            }

            // 100개 단위로 업데이트
            for (Map<String, Object> update : updateList) {
                if((Integer) update.get("broadNo") == 0){
                    continue;
                }
                afreecaBjMapper.updateBroadNo((String) update.get("bjId"), (Integer) update.get("broadNo"));
            }
        }
    }

    private int getBroadNoFromExternalService(String bjId) throws JsonProcessingException  {
        // 외부 서비스 호출 로직
        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "Mozilla/5.0");

        HttpEntity<String> entity = new HttpEntity<>(headers);
        String url = "https://bjapi.afreecatv.com/api/"+bjId+"/station";

        ResponseEntity<String> responseEntity  = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        String response = responseEntity.getBody();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(response);
        int broadNode = rootNode.path("broad").path("broad_no").asInt();
        System.out.println("station_no : " + broadNode);

        return broadNode;
    }

    public int urlTestMethod(String url) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "Mozilla/5.0");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> responseEntity  = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        String response = responseEntity.getBody();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(response);
        int broadNode = rootNode.path("broad").path("broad_no").asInt();
        System.out.println("station_no : " + broadNode);

        return broadNode;
    }
    public String postDataToExternalApi(String url) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Object> request = new HttpEntity<>(headers);

        String response = restTemplate.postForObject(url, request, String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(response);
        JsonNode allRankNode = rootNode.path("ALL_RANK");
        List<AfreecaBjDTO> afreecaBjDtos = new ArrayList<>();

        allRankNode.forEach(node -> {
            String bjId = node.path("bj_id").asText();
            String bjNick = node.path("bj_nick").asText();
            String totalRank = node.path("total_rank").asText();
            afreecaBjDtos.add(new AfreecaBjDTO(bjId, bjNick, totalRank));
        });

        if(allRankNode != null && !allRankNode.isEmpty()) {
            try {
                afreecaBjMapper.insertBjInfos(afreecaBjDtos);
            } catch (DataIntegrityViolationException ex) {
                System.err.println("Data integrity violation occurred: " + ex.getMessage());
                // 예외 처리 로직 (예: 로그 작성 또는 실패한 항목의 리스트를 저장)
            }
            return objectMapper.writeValueAsString(afreecaBjDtos);
        }
        return "No data to insert.";
    }

    private void insertBj(AfreecaBjDTO afreecaBjDTO) {
        afreecaBjMapper.insertBj(afreecaBjDTO);
    }
    public void saveAllBjs(List<AfreecaBjDTO> bjs) {
        for (AfreecaBjDTO bj : bjs) {
            afreecaBjMapper.insertBj(bj);
        }
    }
}