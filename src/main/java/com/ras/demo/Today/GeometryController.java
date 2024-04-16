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
public class GeometryController {

    private final GeometryService geometryService;

    public GeometryController(GeometryService geometryService) {
        this.geometryService = geometryService;
    }

    @GetMapping("/geo")
    public void getGeo(){
        System.out.println(" geo : " + geometryService.getGeo());
    }

    @GetMapping("/Selling")
    public void getSelling(){

        System.out.println(" geo : " + geometryService.getSellingGeo());
    }

    @GetMapping("/Cutting")
    public void cutting(){

        InventoryManager inventoryManager = new InventoryManager(100, 200);
        inventoryManager.markSoldArea(1, 1, 50, 50);
        inventoryManager.markSoldArea(51, 1, 72 , 72);
        inventoryManager.markSoldArea(0, 0, 10, 10);  // Overlapping test

        System.out.println("Total unavailable area before scrap: " + inventoryManager.calculateTotalUnavailableArea() + " cm^2");
        inventoryManager.printInventory();
        // Marking a scrap area and calculating loss
        int lossArea = inventoryManager.calculateLossArea(50, 50, 70, 70);
        System.out.println("Loss area due to scrap: " + lossArea + " cm^2");

        inventoryManager.printInventory();
        System.out.println("Total unavailable area after scrap: " + inventoryManager.calculateTotalUnavailableArea() + " cm^2");
    }
}
