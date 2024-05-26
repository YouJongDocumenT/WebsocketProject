package com.ras.demo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ras.demo.dto.AfreecaBjListDTO;
import com.ras.demo.service.AfreecaBjListService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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

    @GetMapping("/send")
    public String sendPostRequest() throws JsonProcessingException {
        for(int pageNum = 1; pageNum<166; pageNum++) {
            String url = "https://afevent2.afreecatv.com/app/rank/api.php?szWhich=viewer&nPage="+pageNum+"&szSearch=&szGender=A";
//        afreecaBjListService.processBjData(afreecaBjListService.postDataToExternalApi(url));
            System.out.println(url);

            try {
                afreecaBjListService.postDataToExternalApi(url);
            } catch (DataIntegrityViolationException ex) {
                // MySQL Duplicate Entry 에러에 대한 처리
            }
        }
        return "Success bjList Insert to DB";
    }

    @GetMapping("/bjInfo")
    public int getBjInfo() throws JsonProcessingException {
        int nullCnt = afreecaBjListService.returnBroadNoIsNull();
        int nullCntBatch = (int) Math.ceil(nullCnt*0.01);
        System.out.println(nullCntBatch);
        String bjId = "devil0108";

        String url = "https://bjapi.afreecatv.com/api/"+bjId+"/station";
        int result = afreecaBjListService.urlTestMethod(url);
        System.out.println(result);
        return result;
    }

    @GetMapping("/update")
    public void scheduleUpdateBroadNoBatch() throws JsonProcessingException {
        afreecaBjListService.updateBroadNoBatch();
    }

}
