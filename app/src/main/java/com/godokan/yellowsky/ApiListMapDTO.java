package com.godokan.yellowsky;

public class ApiListMapDTO {
    private Integer no;
    private String name;
    private String properName;
    private Double lat;
    private Double lng;
    private String address;
    private String placeUrl;

    public ApiListMapDTO(Integer no, String name, String properName, Double lat, Double lng, String address, String placeUrl) {
        this.no = no;
        this.name = name;
        this.properName = properName;
        this.lat = lat;
        this.lng = lng;
        this.address = address;
        this.placeUrl = placeUrl;
    }

    public Integer getNo() {
        return no;
    }

    public String getName() {
        return name;
    }

    public String getProperName() {
        return properName;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLng() {
        return lng;
    }

    public String getAddress() {
        return address;
    }

    public String getPlaceUrl() {
        return placeUrl;
    }
}
