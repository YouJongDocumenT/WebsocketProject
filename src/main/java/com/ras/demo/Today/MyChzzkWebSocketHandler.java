package com.ras.demo.Today;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public class MyChzzkWebSocketHandler extends TextWebSocketHandler {

    String sid;

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {

    }

    static boolean handShakeFlag = false;
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session, message);
        System.out.println("Received message from server: " + message.getPayload());

//        if (handShakeFlag) {
//            JSONParser parser = new JSONParser();
//            Object obj = parser.parse(message.getPayload());
//            JSONObject jsonObj = (JSONObject) obj;
//            String tempSid = jsonObj.get("sid").toString();
//            System.out.println("sid 출력 테스트 : " + tempSid);
//            setSid(tempSid);
//
//            if(tempSid != null){
//                handShakeFlag = false;
//            }

        }

//    public String getSid() {
//        return sid;
//    }
//
//    public void setSid(String sid) {
//        this.sid = sid;
//    }
}