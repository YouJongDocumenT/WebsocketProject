package com.ras.demo.Today;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public class MyChzzkWebSocketHandler extends TextWebSocketHandler {

    static String sid;

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {

    }

    static boolean handShakeFlag = true;
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session, message);
        System.out.println("Received message from server: " + message.getPayload());

//         sid를 받아오기 전 까지 실행
        if (handShakeFlag) {
            // JSON의 bdy 속성을 파싱 후 sid 값 추출
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(message.getPayload());
            JSONObject jsonObj = (JSONObject) obj;

            JSONObject bdyObj = (JSONObject) jsonObj.get("bdy");

            System.out.println("casting 테스트 : " + bdyObj);

            System.out.println("casting 테스트 : " + obj);
            setSid(bdyObj.get("sid").toString());
            if (bdyObj.get("sid") != null) {
                handShakeFlag = false;
            }

        }


    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }
}