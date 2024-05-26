package com.ras.demo.mapper;

import com.ras.demo.dto.AfreecaBjDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AfreecaBjMapper {

    void insertBjInfos(List<AfreecaBjDTO> afreecaBjDtoList);

    void insertBj(AfreecaBjDTO afreecaBjDTO);

    int returnBroadNoIsNull();

    List<String> selectNullBroadNo();
    void updateBroadNo(@Param("bjId") String bjid, @Param("broadNo") int broadNo);


}