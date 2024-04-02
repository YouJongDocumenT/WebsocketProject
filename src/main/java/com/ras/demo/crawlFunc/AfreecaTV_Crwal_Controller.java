package com.ras.demo.crawlFunc;

import com.ras.demo.crawlFunc.WebSocketClientApp;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Scanner;


@Controller
public class AfreecaTV_Crwal_Controller {

    private final WebSocketClientApp wapp;
    Scanner scanner = new Scanner(System.in);

    public AfreecaTV_Crwal_Controller(WebSocketClientApp wapp) {
        this.wapp = wapp;
    }

    @GetMapping("AfcCrawlApi")
    public String AfcCrawlApi() throws Exception {

        System.out.print("아프리카TV url : ");
        String url = scanner.next();

        wapp.CrwalAFC(url);

        return "Test";
    }
}

