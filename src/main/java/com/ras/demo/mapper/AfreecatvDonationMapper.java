package com.ras.demo.mapper;

import com.ras.demo.dto.Afreecatv_BJName;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;


import java.util.Date;
@Mapper
@Repository
public interface AfreecatvDonationMapper {
    Afreecatv_BJName selectByDate();
    void insert(Afreecatv_BJName AFCdonation);
    void update(Afreecatv_BJName AFCdonation);

}
