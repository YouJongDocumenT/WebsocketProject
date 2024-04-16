package com.ras.demo.mapper;


import com.ras.demo.dto.SellingGeoJSON;
import com.ras.demo.dto.User;
import com.ras.demo.dto.geoJSON;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface GeometryMapper {
    geoJSON selectGeometry();
    List<SellingGeoJSON> selectSellingGeometry();
}
