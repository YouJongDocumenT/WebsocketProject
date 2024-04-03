package com.ras.demo.mapper;

import com.ras.demo.dto.Cheezk_STName;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface CheezkDonationMapper {
    Cheezk_STName selectByDate();
    void insert(Cheezk_STName CHKdonation);
    void update(Cheezk_STName CHKdonation);

}
