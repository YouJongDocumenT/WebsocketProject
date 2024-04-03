package com.ras.demo.Websocket.Chzzk.Handler;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class MyChzzkWebSocketHandler extends TextWebSocketHandler {

     Long sum = 0L;

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {

    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session, message);
        //System.out.println("Received message from server: " + message.getPayload());

        String msgValue = "";
        String TypeChat = "";
        Long cmdValue = 0L;
        String nickname = "";
        Long payAmount = 0L;

        JSONParser parser = new JSONParser();

        JSONObject jsonObject = null;
        try {
            jsonObject = (JSONObject) parser.parse(message.getPayload());

            cmdValue = (Long) jsonObject.get("cmd");

            if (cmdValue == 0) {
                System.out.println("Ping : " + cmdValue);

                String PingPongJson = "{\n" +
                        "    \"ver\": \"2\",\n" +
                        "    \"cmd\": 10000\n" +
                        "}";
                session.sendMessage(new TextMessage(PingPongJson));

                System.out.println("Pong : " + PingPongJson);
            }

            if (cmdValue == 93101) {
                TypeChat = "채팅";
            } else if (cmdValue == 93102) {
                TypeChat = "후원";
            }

            // "bdy" 키가 있는지 확인
            if (jsonObject.containsKey("bdy")) {
                Object bdyObject = jsonObject.get("bdy");
                // "bdy"가 배열인지 확인
                if (bdyObject instanceof JSONArray) {
                    JSONArray bdyArray = (JSONArray) bdyObject;
                    // 배열의 첫 번째 요소 선택
                    JSONObject firstBdyObject = (JSONObject) bdyArray.get(0);
                    // "msg" 키가 있는지 확인하고 값 반환
                    if (firstBdyObject.containsKey("msg")) {
                        msgValue = (String) firstBdyObject.get("msg");
                    }
                    if(Objects.equals((String) firstBdyObject.get("uid"), "anonymous")){
                        nickname = "익명의 후원자";
                        if (firstBdyObject.containsKey("extras")) {
                            Object donationObject = firstBdyObject.get("extras");

                            if (donationObject instanceof String) {
                                try {
                                    JSONObject donationJson = (JSONObject) parser.parse((String) donationObject);
                                    if (donationJson.containsKey("payAmount")) {
                                        payAmount = (Long) donationJson.get("payAmount");
                                    }
                                } catch (ParseException e) {
                                    // JSON 파싱 오류 처리
                                    e.printStackTrace();
                                }
                            }
                        }

                    }else {
                        // "profile" 키가 있는지 확인
                        if (firstBdyObject.containsKey("profile")) {
                            Object profileObject = firstBdyObject.get("profile");

                            if (profileObject instanceof String) {
                                try {
                                    JSONObject profileJson = (JSONObject) parser.parse((String) profileObject);
                                    if (profileJson.containsKey("nickname")) {
                                        nickname = (String) profileJson.get("nickname");
                                    }
                                } catch (ParseException e) {
                                    // JSON 파싱 오류 처리
                                    e.printStackTrace();
                                }
                            } else {
                                System.out.println("profileObject is not an instance of JSONObject");
                                System.out.println("profileObject class: " + profileObject.getClass().getName());
                            }

                            if (firstBdyObject.containsKey("extras")) {
                                Object donationObject = firstBdyObject.get("extras");

                                if (donationObject instanceof String) {
                                    try {
                                        JSONObject donationJson = (JSONObject) parser.parse((String) donationObject);
                                        if (donationJson.containsKey("payAmount")) {
                                            payAmount = (Long) donationJson.get("payAmount");
                                        }
                                    } catch (ParseException e) {
                                        // JSON 파싱 오류 처리
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = dateFormat.format(now);

        if (cmdValue == 93101) {
            System.out.println("[" + formattedDate + "] " + "[" + TypeChat + "] " + nickname+ " : " + msgValue);
        } else if (cmdValue == 93102) {
            sum = sum + payAmount;
            System.out.println("[" + formattedDate + "] " + "[" + TypeChat + "] " + nickname+ " : " + payAmount + "원     [ 누적 ] " + sum + "원");
        }
    }


    }