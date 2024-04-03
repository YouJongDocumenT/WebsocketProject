package com.ras.demo.Websocket.AfreecaTV.Handler;

import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.nio.charset.StandardCharsets;

public class MyWebSocketHandler extends TextWebSocketHandler {

    int sum = 0;

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
        String SEPARATOR = "+" + "-".repeat(70) + "+";

        // getPayload() 메소드를 사용하여 ByteBuffer를 얻은 후, byte 배열로 변환합니다.
        byte[] bytes = message.getPayload().array();

        // 메시지를 \x0c 기준으로 나눕니다.
        String[] messages = new String(bytes, StandardCharsets.UTF_8).split("\u000c");

        // GwkwMDE4 : esc0018
        // GwkwMDA1 : esc0005

        // 조건에 맞는 메시지인지 확인합니다.
        if (messages.length >= 5 && !messages[1].equals("-1") && !messages[1].equals("1") && !messages[1].contains("|")) {
            // 일반채팅(0005) 크롤링
            /*
            if(messages[0].substring(2, 6).equals("0005")){
                String userId = messages[2];
                String comment = messages[1];
                String userNickname = messages[6];

                // 메시지 출력
                System.out.println(SEPARATOR);
                System.out.println("| " + userNickname + "[" + userId + "] - " + comment);
                System.out.println(messages[0].substring(0, 6));
            }
            */
            if(messages[0].substring(2, 6).equals("0018")){

                /*
                for(int i = 0; i <= 11; i++){
                    String a = messages[i];
                    System.out.println(i+"번: " + a);
                }
                */

                String userid = messages[2];
                String usernick = messages[3];
                String count = messages[4];
                //String etc = messages[11];

                // 메시지 출력
                System.out.println(SEPARATOR);
                System.out.println("| " + "   [닉네임] " + usernick + " ( " + userid + " ) - " + "[별풍선] " + count + " 개");
                System.out.println(messages[0].substring(0, 6));

                sum = sum + Integer.parseInt(count);
                System.out.println(" [ 누적 별풍선 개수 ] " + sum + " 개");

            }

        } else {
            // 다른 타입의 메시지 처리
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session, message);

        String receivedMessage = message.getPayload();
        System.out.println("Received message from server: " + receivedMessage);
    }
}

