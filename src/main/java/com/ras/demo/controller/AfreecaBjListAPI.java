package com.ras.demo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ras.demo.dto.AfreecaBjListDTO;
import com.ras.demo.service.AfreecaBjListService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
public class AfreecaBjListAPI {

    private static final Logger logger = LoggerFactory.getLogger(AfreecaBjListAPI.class);
    @Autowired
    private AfreecaBjListService afreecaBjListService;

//    @PostMapping("/create")
//    public Mono<String> createBjs() {
//        return afreecaBjListService.saveBjsFromRemote();
//    }

    @PostMapping("/send")
    public String sendPostRequest() throws JsonProcessingException {
        String url = "https://afevent2.afreecatv.com/app/rank/api.php?szWhich=viewer&nPage=1&szSearch=&szGender=A";
//        afreecaBjListService.processBjData(afreecaBjListService.postDataToExternalApi(url));
        return afreecaBjListService.postDataToExternalApi(url);
    }
}
