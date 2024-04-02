package com.ras.demo.service;

import com.ras.demo.dto.Afreecatv_BJName;
import com.ras.demo.mapper.AfreecatvDonationMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AfreecatvDonationService {

    private final AfreecatvDonationMapper donationMapper;
    private static final Logger logger = LoggerFactory.getLogger(AfreecatvDonationMapper.class);

    public void AFCupdateOrInsertDonation(int count) {
        Afreecatv_BJName donation = donationMapper.selectByDate();
        if (donation != null) {
            // 현재 날짜를 LocalDate로 변환
            LocalDate today = LocalDate.now();
            // donationDate를 LocalDate로 변환
            LocalDate donationDate = donation.getDonationDate().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();

            logger.info(String.valueOf(today));
            logger.info(String.valueOf(donationDate));

            // 년, 월, 일이 모두 같은지 비교
            if (today.isEqual(donationDate)) {
                donation.setDonationCount(donation.getDonationCount() + count);
                donationMapper.update(donation);
            }else {
                donation = new Afreecatv_BJName();
                donation.setDonationDate(new Date());
                donation.setDonationCount(count);
                donationMapper.insert(donation);
            }

        } else {
            donation = new Afreecatv_BJName();
            donation.setDonationDate(new Date());
            donation.setDonationCount(count);
            donationMapper.insert(donation);
        }
    }
}
