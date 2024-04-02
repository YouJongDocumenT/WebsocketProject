package com.ras.demo.Today;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

@Controller
public class TodayController {

    private final ChatChannelService chts;
    private final ChennelNameService chtns;

    private final AccessTokenService acts;

    private final UserIdHashService uihs;

    Scanner scanner = new Scanner(System.in);

    public TodayController(ChatChannelService chts, ChennelNameService chtns, AccessTokenService acts, UserIdHashService uihs) {
        this.chts = chts;
        this.chtns = chtns;
        this.acts = acts;
        this.uihs = uihs;
    }

    @GetMapping("/n")
    public String netty() {

        System.out.print("치지직 URL : ");
        String url = scanner.next();

        // 맨 오른쪽 "/" 기준 오른쪽에서부터 1번째 문자열 가져옴
        String[] urlParts = url.split("/");
        String streammer_id = urlParts[urlParts.length - 1];
        System.out.println("[ StreammerId ] : " + streammer_id);

        Map<String, String> cookies = new HashMap<>();
        cookies.put("NID_AUT", "OiVcsk9UvoKtcCj5lubRExLugEK3YoBsOkrAuWQFbWS2CoO/GKOpb/kwSbAP+ka+");
        cookies.put("NID_SES", "AAAByKv7jX8W95ngAUL5zuaW7Aw05YkxjDSdCTURTAk6UNqSxhFwLK67PNZxHtMgiWxio80jTPHqs5F61W4SZm/TivkUhYd/ja/nfHpEx6Msw8js4Jqd+247uBmpnTqA29E4uPu7XAnWpdckBRa/OsJdhRXQxc1KGgxfnsIjOlMX/pb38Hq15M/zLV54istMbjbX5nE3HNFTr9IqlYSOdDL4mB/eK+YDf1sdewDnAz4asvpZYf/DATZhJqCf41/tQi0MBzMZqUjOiDXRs0EA2WpHOPwM0BJJGwy6P3pKVKNzAm0fX48qerTstdV7AJsbvwp9sQPpozkhoPafAIMxRmsDSgBAJQeHLZzqFvQiO5tpwZdMgccUUSvUdLZfEXBSnfF6LNxfv1ujD/q4bV/1DaAAeO3RQMa4m+Tc8fJXRyL7Ye592jeLgH087aqv/zZn1Oaqdb8NHyWLJvpUAnbKI5bxDx9w9AUfoQ8ocaxPKyoUJkq+eDk/CmB+U5p6hGu+kAdUOvgm1Ynb1Ootqq+Pho3UNPRe0zLyr1hqW2vQf3pxPxqtB7UAqTkKs1JX8yCckNHoFxMDaeqREH27Ejcv4zLNgarE/HxfUFydKfnx41BVX8kR");

        System.out.println("[ ChatChannelId ] : " + chts.fetchChatChannelId(streammer_id));
        System.out.println("[ channelName ] : " + chtns.fetchCchannelName(streammer_id));

        System.out.println("[ AccessToken ] : " + acts.fetchAccessToken(chts.fetchChatChannelId(streammer_id), cookies).get("accessToken"));
        System.out.println("[ ExtraToken ] : " + acts.fetchAccessToken(chts.fetchChatChannelId(streammer_id), cookies).get("extraToken"));
        System.out.println("[ UserIdHash ] : "+ uihs.fetchUserIdHash(cookies));

        return "Test";
    }
}
