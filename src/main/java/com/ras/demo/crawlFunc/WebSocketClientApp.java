package com.ras.demo.crawlFunc;


import com.ras.demo.controller.AfreecatvBJname;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.*;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import java.util.Map;

@Service
public class WebSocketClientApp {
    private final ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
    private static final Logger logger = LoggerFactory.getLogger(WebSocketClientApp.class);
    private final AfreecaTvApi afcAPI;

    public WebSocketClientApp(AfreecaTvApi afcAPI) {
        this.afcAPI = afcAPI;
    }

    public void CrwalAFC(String url) throws Exception {

        String[] urlParts = url.split("/");
        String BNO = urlParts[urlParts.length - 1];
        String BID = urlParts[urlParts.length - 2];

        Map<String, String> result_afcAPI = afcAPI.get_player_live(BNO, BID);

        // port번호(CHPT) 9001 계속 체크 해야함 -- 2024-03-31
        String websocketUrl = String.format("wss://%s:9001/Websocket/%s", result_afcAPI.get("CHDOMAIN"), BID);

        scheduler.initialize(); // 스케줄러 초기화

        // StandardWebSocketClient에 SSL 설정 적용
        StandardWebSocketClient client = new StandardWebSocketClient();
        client.setSslContext(SSLContextCreator.createSslContext());

        WebSocketHttpHeaders headers = new WebSocketHttpHeaders();
        headers.add(WebSocketHttpHeaders.SEC_WEBSOCKET_PROTOCOL, "chat");



        WebSocketHandler messageHandler = new MyWebSocketHandler() {
            @Override
            public void afterConnectionEstablished(WebSocketSession session) throws Exception {

                String SEPARATOR = "+" + "-".repeat(70) + "+";

                logger.info(SEPARATOR + "\n" +
                        "  CHDOMAIN: " + result_afcAPI.get("CHDOMAIN") + "\n" +
                        "  CHATNO: " + result_afcAPI.get("CHATNO") + "\n" +
                        "  FTK: " + result_afcAPI.get("FTK") + "\n" +
                        "  TITLE: " + result_afcAPI.get("TITLE") + "\n" +
                        "  BJID: " + result_afcAPI.get("BJID") + "\n" +
                        "  CHPT: " + result_afcAPI.get("CHPT") + "\n" +
                        SEPARATOR);

                super.afterConnectionEstablished(session);

                String subProtocol = session.getAcceptedProtocol();

                if ("chat".equals(subProtocol)) {
                    logger.info("Connected to WebSocket server with 'chat' sub-protocol");
                } else {
                    logger.info("Connected to WebSocket server with different sub-protocol: " + subProtocol);
                }

                String F = "\u000c";
                String ESC = "\u001b\t";


                // 최초 연결 패킷 전송
                String CONNECT_PACKET = ESC + "000100000600" + F.repeat(3) + "16" + F;
                session.sendMessage(new BinaryMessage(CONNECT_PACKET.getBytes()));
                logger.info("최초 연결 패킷 전송: ");

                // 메시지 수신을 위한 조인 패킷 전송
                String JOIN_PACKET = ESC + "0002" + calculateByteSize(result_afcAPI.get("CHATNO")) + "00" + F + result_afcAPI.get("CHATNO") + repeat(F, 5);
                session.sendMessage(new BinaryMessage(JOIN_PACKET.getBytes()));
                logger.info("메시지 수신을 위한 패킷 전송: ");

                // 주기적 핑 패킷 전송 설정
                String PING_PACKET = ESC + "000000000100" + F;
                logger.info("주기적 핑 패킷 전송: ");

                scheduler.scheduleWithFixedDelay(() -> {
                    try {
                        if (session.isOpen()) { // 세션이 열려 있는지 확인
                            session.sendMessage(new BinaryMessage(PING_PACKET.getBytes()));
                            logger.info(String.valueOf(session.isOpen()));
                        } else {
                            logger.info(session.isOpen()+"세션이 닫혀 있어 메시지를 보낼 수 없습니다.");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }, 30000); // 60초마다 핑
            }


        };
        // 연결 매니저 설정 및 연결 시작
        WebSocketConnectionManager manager = new WebSocketConnectionManager(client, messageHandler, websocketUrl, headers );
        manager.start();
    }

    // 문자열의 바이트 크기 계산하는 메서드
    private static String calculateByteSize(String string) {
        int length = string.getBytes().length + 6;
        return String.format("%06d", length);
    }

    // 문자열을 count만큼 반복하는 메서드
    private static String repeat(String string, int count) {
        StringBuilder repeatedString = new StringBuilder();
        for (int i = 0; i < count; i++) {
            repeatedString.append(string);
        }
        return repeatedString.toString();
    }



}
