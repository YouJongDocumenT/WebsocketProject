package com.ras.demo.service;


import com.ras.demo.dto.Cheezk_STName;
import com.ras.demo.mapper.CheezkDonationMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class CheezkDonationService {

    private final CheezkDonationMapper ChkDonationMapper;
    private static final Logger logger = LoggerFactory.getLogger(CheezkDonationService.class);

    public void CHKupdateOrInsertDonation(int count) {
        Cheezk_STName donation = ChkDonationMapper.selectByDate();
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
                ChkDonationMapper.update(donation);
            }else {
                donation = new Cheezk_STName();
                donation.setDonationDate(new Date());
                donation.setDonationCount(count);
                ChkDonationMapper.insert(donation);
            }

        } else {
            donation = new Cheezk_STName();
            donation.setDonationDate(new Date());
            donation.setDonationCount(count);
            ChkDonationMapper.insert(donation);
        }
    }

}
