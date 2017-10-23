package com.will.givegreenville.interfaces;

import com.will.givegreenville.models.GeoCodeResults;
import feign.Param;
import feign.RequestLine;

public interface GeoCode {
    @RequestLine("GET /maps/api/geocode/json?address={location}&key={key}")
    GeoCodeResults geoCodeResults(@Param("location") String location, @Param("key") String key);

}
