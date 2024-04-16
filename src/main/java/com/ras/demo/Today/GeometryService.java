package com.ras.demo.Today;

import com.ras.demo.dto.SellingGeoJSON;
import com.ras.demo.dto.geoJSON;
import com.ras.demo.mapper.GeometryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GeometryService {

    private final GeometryMapper geometryMapper;

    public GeometryService(GeometryMapper geometryMapper) {
        this.geometryMapper = geometryMapper;
    }

    public geoJSON getGeo(){

        return geometryMapper.selectGeometry();
    }

    public List<SellingGeoJSON> getSellingGeo(){

        return geometryMapper.selectSellingGeometry();
    }

}
