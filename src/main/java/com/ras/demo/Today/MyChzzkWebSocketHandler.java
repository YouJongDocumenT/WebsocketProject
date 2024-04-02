package com.ras.demo.Today;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

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

        System.out.println(jsonObject);
    }

}