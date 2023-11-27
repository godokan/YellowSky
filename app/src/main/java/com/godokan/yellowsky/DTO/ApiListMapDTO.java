package com.godokan.yellowsky.DTO;

public class ApiListMapDTO {
    private Integer no;
    private String name;
    private Double lat;
    private Double lng;
    private String address;

    public ApiListMapDTO(Integer no, String name, Double lat, Double lng, String address) {
        this.no = no;
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.address = address;
    }

    public Integer getNo() {
        return no;
    }

    public String getName() {
        return name;
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
}
