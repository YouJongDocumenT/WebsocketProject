package com.ras.demo.Websocket.AfreecaTV.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Service
public class AfreecaTvApi {
    public Map<String, String> get_player_live(String bno, String bid) throws Exception {
        Map<String, String> resultMap = new HashMap<>();
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {

            HttpUriRequest httppost = RequestBuilder.post()
                    .setUri(new URI("https://live.afreecatv.com/afreeca/player_live_api.php"))
                    .addParameter("bjid", bid)
                    .addParameter("bno", bno)
                    .addParameter("type", "live")
                    .addParameter("confirm_adult", "false")
                    .addParameter("player_type", "html5")
                    .addParameter("mode", "landing")
                    .addParameter("from_api", "0")
                    .addParameter("pwd", "")
                    .addParameter("stream", "common")
                    .addParameter("quality", "HD")
                    .addParameter("Content-Type", "text/html; charset=utf-8")
                    .build();

            CloseableHttpResponse response = httpclient.execute(httppost);

            try {
                String responseBody = EntityUtils.toString(response.getEntity());
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(responseBody);
                JsonNode channel = root.path("CHANNEL");

                System.out.println();
                resultMap.put("CHDOMAIN", channel.path("CHDOMAIN").asText());
                resultMap.put("CHATNO", channel.path("CHATNO").asText());
                resultMap.put("FTK", channel.path("FTK").asText());
                resultMap.put("TITLE", channel.path("TITLE").asText());
                resultMap.put("BJID", channel.path("BJID").asText());
                resultMap.put("CHPT", channel.path("CHPT").asText());


            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
        return resultMap;
    }
}