package com.ras.demo.Today;

import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

@RestController
public class ChzzkSocketController {

    private static final ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();

    private final ChatChannelService chts;
    private final ChennelNameService chtns;

    private final AccessTokenService acts;

    private final UserIdHashService uihs;

    public ChzzkSocketController(ChatChannelService chts, ChennelNameService chtns, AccessTokenService acts, UserIdHashService uihs) {
        this.chts = chts;
        this.chtns = chtns;
        this.acts = acts;
        this.uihs = uihs;
    }

    Scanner scanner = new Scanner(System.in);

    @GetMapping("/sendChzzkSochet")
    public void SendConnetionChzzkSochet() throws Exception{

        System.out.print("치지직 URL : ");
        String url = scanner.next();

        // 맨 오른쪽 "/" 기준 오른쪽에서부터 1번째 문자열 가져옴
        String[] urlParts = url.split("/");
        String streammer_id = urlParts[urlParts.length - 1];
        System.out.println("[ StreammerId ] : " + streammer_id);

        Map<String, String> cookies = new HashMap<>();
        cookies.put("NID_AUT", "OiVcsk9UvoKtcCj5lubRExLugEK3YoBsOkrAuWQFbWS2CoO/GKOpb/kwSbAP+ka+");
        cookies.put("NID_SES", "AAAByKv7jX8W95ngAUL5zuaW7Aw05YkxjDSdCTURTAk6UNqSxhFwLK67PNZxHtMgiWxio80jTPHqs5F61W4SZm/TivkUhYd/ja/nfHpEx6Msw8js4Jqd+247uBmpnTqA29E4uPu7XAnWpdckBRa/OsJdhRXQxc1KGgxfnsIjOlMX/pb38Hq15M/zLV54istMbjbX5nE3HNFTr9IqlYSOdDL4mB/eK+YDf1sdewDnAz4asvpZYf/DATZhJqCf41/tQi0MBzMZqUjOiDXRs0EA2WpHOPwM0BJJGwy6P3pKVKNzAm0fX48qerTstdV7AJsbvwp9sQPpozkhoPafAIMxRmsDSgBAJQeHLZzqFvQiO5tpwZdMgccUUSvUdLZfEXBSnfF6LNxfv1ujD/q4bV/1DaAAeO3RQMa4m+Tc8fJXRyL7Ye592jeLgH087aqv/zZn1Oaqdb8NHyWLJvpUAnbKI5bxDx9w9AUfoQ8ocaxPKyoUJkq+eDk/CmB+U5p6hGu+kAdUOvgm1Ynb1Ootqq+Pho3UNPRe0zLyr1hqW2vQf3pxPxqtB7UAqTkKs1JX8yCckNHoFxMDaeqREH27Ejcv4zLNgarE/HxfUFydKfnx41BVX8kR");

        String channelName = chtns.fetchCchannelName(streammer_id);

        System.out.println(" [ "+channelName+" ] 채팅창에 연결중 ... ");

        scheduler.initialize();

        WebSocketClient client = new StandardWebSocketClient();

        String channelId = chts.fetchChatChannelId(streammer_id);
        String userId = uihs.fetchUserIdHash(cookies);
        String accessToken = acts.fetchAccessToken(chts.fetchChatChannelId(streammer_id), cookies).get("accessToken");

        WebSocketHandler chzzkMessageHandler = new MyChzzkWebSocketHandler() {

            @Override
            public void afterConnectionEstablished(WebSocketSession session) throws Exception {
                super.afterConnectionEstablished(session);

                String jsonData = "{\n" +
                        "    \"ver\": \"2\",\n" +
                        "    \"svcid\": \"game\",\n" +
                        "    \"cid\": \"" + channelId + "\",\n" +
                        "    \"cmd\": 100,\n" +
                        "    \"tid\": 1\n" +
                        "    \"bdy\": {\n" +
                        "        \"uid\": \"" + userId + "\",\n" +
                        "        \"devType\": 2001,\n" +
                        "        \"accTkn\": \"" + accessToken + "\",\n" +
                        "        \"auth\": \"SEND\"\n" +
                        "    },\n" +
                        "}";
                // JSON 문자열 파싱

                // 루트 레벨의 값을 추출

                session.sendMessage(new TextMessage(jsonData));
                System.out.println("sendMessage : "+ jsonData);
                //self.sid = sock_response['bdy']['sid']

                System.out.println(" [ "+channelName+" ] 채팅창에 연결중 ... ");

                String jsonData_2 = "{\n" +
                        "    \"ver\": \"2\",\n" +
                        "    \"svcid\": \"game\",\n" +
                        "    \"cid\": \"" + channelId + "\",\n" +
                        "    \"cmd\": 5101,\n" +
                        "    \"tid\": 2\n" +
                        "    \"sid\": 2\n" +
                        "    \"bdy\": {\n" +
                        "        \"recentMessageCount\": 50,\n" +
                        "    },\n" +
                        "}";



                String PING_PACKET = "{\n" +
                        "    \"ver\": \"2\",\n" +
                        "    \"cmd\": 10000\n" +
                        "}";
                System.out.println("주기적 핑 패킷 전송: " + PING_PACKET);

                System.out.println(session.isOpen());

                scheduler.scheduleWithFixedDelay(() -> {
                    try {
                        if (session.isOpen()) { // 세션이 열려 있는지 확인
                            System.out.println("ReLink");
                            session.sendMessage(new TextMessage(PING_PACKET));
                        } else {
                            System.out.println("세션이 닫혀 있어 메시지를 보낼 수 없습니다.");
                            System.out.println(session.isOpen());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }, 20000);
            }
        };
        WebSocketConnectionManager manager = new WebSocketConnectionManager(client,
                chzzkMessageHandler,
                "wss://kr-ss1.chat.naver.com/chat"
        );
        manager.start();
    }
}
