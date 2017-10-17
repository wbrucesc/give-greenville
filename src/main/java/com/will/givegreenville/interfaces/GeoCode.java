package com.will.givegreenville.interfaces;

import com.will.givegreenville.models.GeoCodeResults;
import feign.Param;
import feign.RequestLine;

public interface GeoCode {
    @RequestLine("GET /maps/api/geocode/json?address={zipcode}&key={key}")
    GeoCodeResults geoCodeResults(@Param("zipcode") String zipcode, @Param("key") String key);

}
