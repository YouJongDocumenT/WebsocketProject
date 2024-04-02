package com.ras.demo.Today;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyChzzkWebSocketHandler extends TextWebSocketHandler {

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {

    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session, message);
        System.out.println("Received message from server: " + message.getPayload());

        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(message.getPayload());

        Long cmdValue = (Long) jsonObject.get("cmd");
        System.out.println("cmd 값: " + cmdValue);

        // "bdy" 객체 추출
        JSONArray bdyArray = (JSONArray) jsonObject.get("bdy");



        String TypeChat = "";
        if(cmdValue == 93101){
            TypeChat = "채팅";
        }else if(cmdValue == 93102){
            TypeChat = "후원";
        }
        /*
        String nickname = "";
        if(uidValue == "anonymous"){
            nickname = "익명의 후원자";
        }else {
            nickname = (String) bdyObject.get("nickname");
            System.out.println("nickname 값: " + nickname);
        }


        String msgValue = (String) bdyObject.get("msg");
*/
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = dateFormat.format(now);

        System.out.println("["+formattedDate+"] "+"["+TypeChat+"]"); // "+nickname+" : "+msgValue);
    }


    }