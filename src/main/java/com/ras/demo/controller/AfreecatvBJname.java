package com.ras.demo.controller;

import com.ras.demo.dto.AFCLog;
import com.ras.demo.dto.Afreecatv_BJName;
import com.ras.demo.service.AfreecatvDonationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@RestController
public class AfreecatvBJname {

    private final AfreecatvDonationService donationService;

    private static final Logger logger = LoggerFactory.getLogger(AfreecatvBJname.class);
    private static final Set<String> seenMessages = new HashSet<>();
    private static int accumulatedStarBalloons = 0;
    private static int LogStarBalloons = 0;

    // 생성자 수정: logDataService 초기화
    public AfreecatvBJname(AfreecatvDonationService donationService) {
        this.donationService = donationService;
    }




    // 크롤링 시작 부분
    @GetMapping("/afc")
    public void CrawlStart(){
        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

        try {
            logger.info("아프리카TV 로직 진행 중");
            driver.get("https://play.afreecatv.com/ginis2/259864604");
            Thread.sleep(5000); // 초기 페이지 로딩 대기

            // JavaScript로 채팅 메시지에 시간 추가
            addTimestampToMessages(driver);

            // 1시간마다 누적된 별풍선 개수를 데이터베이스에 업데이트
            executorService.scheduleAtFixedRate(() -> {
                try {
                    if(accumulatedStarBalloons > 0){
                        donationService.AFCupdateOrInsertDonation(accumulatedStarBalloons);
                        accumulatedStarBalloons = 0; // 누적된 별풍선 개수 초기화
                    }
                } catch (Exception e) {
                    logger.error("Donation update error", e);
                }
            },10, 10, TimeUnit.SECONDS);//, 1, 1, TimeUnit.HOURS);

            // 메인 크롤링 로직
            while (true) {
                crawlPage(driver);
                Thread.sleep(5000); // 주기적으로 새로운 메시지 확인
            }
        } catch (InterruptedException e) {
            logger.error("Interrupted!", e);
            Thread.currentThread().interrupt();
        } finally {
            driver.quit();
            executorService.shutdown();
        }

    }

    // ChromeDriver 실행시 개발자 콘솔 코드 입력 함수
    private static void addTimestampToMessages(WebDriver driver) {
        if (driver instanceof JavascriptExecutor) {
            ((JavascriptExecutor) driver).executeScript(
                    "setInterval(function () {" +
                            "    var chatMessages = document.querySelectorAll('.donation-container .donation-bubble');" +
                            "    var currentTime = new Date().toTimeString().split(' ')[0];" +
                            "    chatMessages.forEach(function (message) {" +
                            "        if (!message.querySelector('.time-stamp')) {" +
                            "            var timeSpan = document.createElement('span');" +
                            "            timeSpan.className = 'time-stamp';" +
                            "            timeSpan.textContent = currentTime;" +
                            "            message.appendChild(timeSpan);" +
                            "        }" +
                            "    });" +
                            "}, 1000);"
            );
        }
    }

    // 메인 크롤링 함수
    private void crawlPage(WebDriver driver) {
        List<WebElement> messages = driver.findElements(By.cssSelector(".donation-container .donation-bubble"));
        for (WebElement message : messages) {
            try {
                String nickname = retryFindElement(message, By.cssSelector(".name")).getAttribute("user_nick");
                String starCountStr = retryFindElement(message, By.cssSelector(".money em")).getText();
                int starCount = Integer.parseInt(starCountStr);
                String timeStamp = retryFindElement(message, By.cssSelector(".time-stamp")).getText();
                String messageKey = nickname + "_" + starCountStr + "_" + timeStamp;

                if (!seenMessages.contains(messageKey)) {
                    logger.info("닉네임: {}, 별풍선 개수: {}, 시간: {}", nickname, starCount, timeStamp);
                    seenMessages.add(messageKey);
                    accumulatedStarBalloons += starCount; // 별풍선 개수 누적
                    LogStarBalloons += starCount; // 별풍선 개수 누적
                    logger.info("[ 아프리카 ]  총 누적 개수 {}",String.valueOf(LogStarBalloons));
                }
            } catch (NumberFormatException e) {
                logger.error("별풍선 개수 파싱 중 오류 발생", e);
            } catch (NoSuchElementException e) {
                logger.error("필요한 웹 요소를 찾을 수 없음", e);
            } catch (Exception e) {
                logger.error("알 수 없는 예외 발생", e);
            }
        }
    }


    // 크롤링 실패시 리트라이 함수
    private static WebElement retryFindElement(WebElement baseElement, By by) {
        final int maxAttempts = 3;
        for (int i = 0; i < maxAttempts; i++) {
            try {
                return baseElement.findElement(by);
            } catch (StaleElementReferenceException | NoSuchElementException e) {
                if (i == maxAttempts - 1) throw e;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(ie);
                }
            }
        }
        throw new NoSuchElementException("Element not found after retrying " + maxAttempts + " times.");
    }

}
