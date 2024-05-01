package com.ras.demo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ras.demo.dto.AfreecaBjDTO;
import com.ras.demo.dto.AfreecaBjListDTO;
import com.ras.demo.mapper.AfreecaBjMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

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

        System.out.println(afreecaBjDtos);
        afreecaBjMapper.insertBjInfos(afreecaBjDtos);
        return objectMapper.writeValueAsString(afreecaBjDtos);
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