package com.ras.demo.Websocket.AfreecaTV;

import com.ras.demo.Websocket.AfreecaTV.Service.WebSocketClientService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Scanner;


@Controller
public class AfreecaTVSocketController {

    private final WebSocketClientService wapp;
    Scanner scanner = new Scanner(System.in);

    public AfreecaTVSocketController(WebSocketClientService wapp) {
        this.wapp = wapp;
    }

    @GetMapping("sendAfreecaSochet")
    public String AfcCrawlApi() throws Exception {

        System.out.print("아프리카TV url : ");
        String url = scanner.next();

        wapp.CrwalAFC(url);

        return "Test";
    }
}

