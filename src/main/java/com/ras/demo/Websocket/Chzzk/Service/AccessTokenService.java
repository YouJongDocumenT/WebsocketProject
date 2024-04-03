package com.ras.demo.Websocket.Chzzk.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class AccessTokenService {

    private final RestTemplate restTemplate;

    @Autowired
    public AccessTokenService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Map<String, String> fetchAccessToken(String chatChannelId, Map<String, String> cookies) {
        String url = String.format(
                "https://comm-api.game.naver.com/nng_main/v1/chats/access-token?channelId=%s&chatType=STREAMING",
                chatChannelId);

        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36");
        // 쿠키 설정
        headers.set("Cookie", cookies.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .reduce((c1, c2) -> c1 + "; " + c2)
                .orElse(""));

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

        Map<String, Object> content = (Map<String, Object>) response.getBody().get("content");
        String accessToken = (String) content.get("accessToken");
        String extraToken = (String) content.get("extraToken");

        return Map.of("accessToken", accessToken, "extraToken", extraToken);
    }
}



