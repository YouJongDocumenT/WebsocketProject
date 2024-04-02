package com.ras.demo.packitTest;

import com.ras.demo.crawlFunc.SSLContextCreator;

import org.springframework.web.socket.*;

import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Base64;


public class WebSocketClientExample {

    public static void main(String[] args) throws Exception {
        // StandardWebSocketClient에 SSL 설정 적용
        StandardWebSocketClient client = new StandardWebSocketClient();
        client.setSslContext(SSLContextCreator.createSslContext());

        WebSocketHttpHeaders headers = new WebSocketHttpHeaders();
        headers.add(WebSocketHttpHeaders.SEC_WEBSOCKET_PROTOCOL, "chat");

        client.doHandshake(new TextWebSocketHandler() {
            @Override
            public void afterConnectionEstablished(WebSocketSession session) throws Exception {

                String base64EncodeConnectPacket = "GwkwMDAxMDAwMDA2MDAMDAwxNgw=";
                String base64EncodeJoinPacket = "GwkwMDAyMDAwMzMyMDAMMjcyDDZiNzdlZjBhOWI2NzM1MDAzMWUxYzNlYzhiMWYwOTJlX2NoMTcxNl8yNjA2Nzk4MThfaHRtbDVfMAwwDAxsb2cRBiYGc2V0X2JwcwY9BjgwMDAGJgZ2aWV3X2JwcwY9BjEwMDAGJgZxdWFsaXR5Bj0Gbm9ybWFsBiYGdXVpZAY9BjU1NDFiMzk4ZWY4MmI3MmRlYmIwMmMxZjFhYmU3YjE5BiYGZ2VvX2NjBj0GS1IGJgZnZW9fcmMGPQY0OAYmBmFjcHRfbGFuZwY9BmtvX0tSBiYGc3ZjX2xhbmcGPQZrb19LUgYmBnN1YnNjcmliZQY9BjAGJgZsb3dsYXRlbmN5Bj0GMAYmBm1vZGUGPQZsYW5kaW5nEnB3ZBESYXV0aF9pbmZvEU5VTEwScHZlchEyEmFjY2Vzc19zeXN0ZW0RaHRtbDUSDA==";

                byte[] binaryDataE = Base64.getDecoder().decode(base64EncodeConnectPacket);
                byte[] binaryDataR = Base64.getDecoder().decode(base64EncodeJoinPacket);

                session.sendMessage(new BinaryMessage(binaryDataE));
                System.out.println("최초 연결 패킷 전송: " + base64EncodeConnectPacket);
                // 메시지 수신을 위한 조인 패킷 전송
                session.sendMessage(new BinaryMessage(binaryDataR));
                System.out.println("메시지 수신을 위한 패킷 전송: " + base64EncodeJoinPacket);

                System.out.println("Connected!");
            }

            @Override
            protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
                System.out.println("Received binary message in Base64: ");
                byte[] payload = message.getPayload().array();
                String base64Encoded = Base64.getEncoder().encodeToString(payload);
                System.out.println("Received binary message in Base64: " + base64Encoded);
            }

            @Override
            protected void handleTextMessage(WebSocketSession session, TextMessage message) {
                System.out.println("Received message: " + message.getPayload());
            }
        },"wss://chat-6e0a4c54.afreecatv.com:9001/Websocket/ch1716", client, headers);

        // 메인 스레드가 종료되지 않도록 대기합니다.
        Thread.sleep(Long.MAX_VALUE);
    }
}

