package com.godokan.yellowsky;

public class ApiListMapDto {
    private Integer no;
    private String name;
    private String properName;
    private Double lat;
    private Double lng;
    private String address;
    private String placeUrl;

    public ApiListMapDto(Integer no, String name, String properName, Double lat, Double lng, String address, String placeUrl) {
        this.no = no;
        this.name = name;
        this.properName = properName;
        this.lat = lat;
        this.lng = lng;
        this.address = address;
        this.placeUrl = placeUrl;
    }
}
