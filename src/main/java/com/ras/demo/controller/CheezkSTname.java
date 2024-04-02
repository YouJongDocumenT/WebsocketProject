package com.ras.demo.controller;

import com.ras.demo.service.CheezkDonationService;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@RestController
public class CheezkSTname {

    private final CheezkDonationService donationService;
    public CheezkSTname(CheezkDonationService donationService) {
        this.donationService = donationService;
    }
    private static final Logger logger = LoggerFactory.getLogger(CheezkSTname.class);
    private static final Set<String> seenMessages = new HashSet<>();
    private static int accumulatedStarCheezes = 0;
    private static int LogStarCheezes = 0;


    // 크롤링 시작 부분
    @GetMapping("/chk")
    public void CrawlStart(){
        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

        try {
            logger.info("치지직 로직 진행 중");
            driver.get("https://chzzk.naver.com/live/3e825479d71ead63b76de0d6f6b5dc83");
            Thread.sleep(5000); // 초기 페이지 로딩 대기

            // JavaScript로 채팅 메시지에 시간 추가
            addTimestampToMessages(driver);

            // 1시간마다 누적된 별풍선 개수를 데이터베이스에 업데이트
            executorService.scheduleAtFixedRate(() -> {
                try {
                    if(accumulatedStarCheezes > 0){
                        donationService.CHKupdateOrInsertDonation(accumulatedStarCheezes);
                        accumulatedStarCheezes = 0; // 누적된 별풍선 개수 초기화
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
                            "    var chatMessages = document.querySelectorAll('.live_chatting_donation_message_container__r01qB');" +
                            "    var currentTime = new Date().toLocaleTimeString([], {hour: '2-digit', minute: '2-digit', second: '2-digit'});" +
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

    private static void crawlPage(WebDriver driver) {
        List<WebElement> messages = driver.findElements(By.cssSelector(".live_chatting_list_donation__Fy6Vz"));
        for (WebElement message : messages) {
            try {
                String nickname = retryFindElement(message, By.cssSelector(".live_chatting_username_nickname__dDbbj .name_text__yQG50")).getText();
                // 기부 금액 문자열에서 숫자만 추출하여 정수로 변환
                String donationAmountStr = retryFindElement(message, By.cssSelector(".live_chatting_donation_message_money__fE2UC")).getText().replaceAll("[^\\d]", "");
                int donationAmount = Integer.parseInt(donationAmountStr);
                // 동적으로 추가된 시간 스탬프 추출
                String timeStamp = retryFindElement(message, By.cssSelector(".time-stamp")).getText();
                String messageKey = nickname + "_" + donationAmountStr + "_" + timeStamp;

                if (!seenMessages.contains(messageKey)) {
                    logger.info("[ 치지직 ]  닉네임: {}, 기부 금액: {}, 시간: {}", nickname, donationAmount, timeStamp);
                    seenMessages.add(messageKey);
                    accumulatedStarCheezes += donationAmount; // 기부 금액 누적
                    LogStarCheezes += donationAmount; // 별풍선 개수 누적
                    logger.info("총 누적 금액 {}",String.valueOf(LogStarCheezes));
                }
            } catch (NumberFormatException e) {
                logger.error("기부 금액 파싱 중 오류 발생", e);
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
