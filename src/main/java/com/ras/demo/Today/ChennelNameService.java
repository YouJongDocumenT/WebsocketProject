package com.ras.demo.Today;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import java.util.Map;

@Service
public class ChennelNameService {

    private final RestTemplate restTemplate;

    @Autowired
    public ChennelNameService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String fetchCchannelName(String streamer) {
        String url = String.format("https://api.chzzk.naver.com/service/v1/channels/%s", streamer);
        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36");
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

        if (response.getBody() != null && response.getBody().containsKey("content")) {
            Map<String, String> content = (Map<String, String>) response.getBody().get("content");
            return content.get("channelName");
        } else {
            throw new RuntimeException("Response does not contain 'content'");
        }
    }

}
