package com.ras.demo.Today;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class UserIdHashService {

    private final RestTemplate restTemplate;

    public UserIdHashService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String fetchUserIdHash(Map<String, String> cookies) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36");
        // 쿠키 설정
        headers.set("Cookie", cookies.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .reduce((c1, c2) -> c1 + "; " + c2)
                .orElse(""));

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Map> response = restTemplate.exchange("https://comm-api.game.naver.com/nng_main/v1/user/getUserStatus", org.springframework.http.HttpMethod.GET, entity, Map.class);

        Map<String, String> content = (Map<String, String>) response.getBody().get("content");
        return content.get("userIdHash");
    }

}
