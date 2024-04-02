package com.ras.demo.packitTest;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class WebSocketMessageProcessor {

    public static void main(String[] args) {
        String binaryMessage = "GwkwMDE4MDAwMDY3MDAMZ3VzZGsyMzYyDGxpbTA1MAzrp5DrnpHsnbTqsozrkJjrhKQMNjMMMAwwDDMwODcMDDEMMAxrb3JfY3VzdG9tMDIM";

        // Base64 디코딩
        byte[] decodedBytes = Base64.getDecoder().decode(binaryMessage);
        String decodedString = new String(decodedBytes);

        // 출력 패턴 적용
        StringBuilder result = new StringBuilder();
        int startIndex = 0;
        int length = decodedString.length();
        for (int i = 0; i < length; i++) {
            char c = decodedString.charAt(i);
            if (c == '\u000c') {
                result.append(", ");
                result.append(decodedString.substring(startIndex, i));
                startIndex = i + 1;
            }
        }
        // 마지막 부분 처리
        result.append(", ");
        result.append(decodedString.substring(startIndex));
        System.out.println(result.toString());


        String[] parts = result.toString().split(", ");
        String userid = parts[3];
        String usernick = parts[4];
        String count = parts[5];
        String etc = parts[12];

        // 값 출력
        System.out.println("아이디 : " + userid);
        System.out.println("닉네임 : " + usernick);
        System.out.println("별풍선 : " +count+ " 개");
        System.out.println("etc 값: " + etc);
    }
}

