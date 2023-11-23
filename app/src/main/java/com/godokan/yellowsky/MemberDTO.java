package com.godokan.yellowsky;

public class MemberDTO {
    private Integer no;
    private String id;
    private String pw;
    private String name;

    public MemberDTO(Integer no, String id, String pw, String name) {
        this.no = no;
        this.id = id;
        this.pw = pw;
        this.name = name;
    }

    public Integer getNo() {
        return no;
    }

    public void setNo(Integer no) {
        this.no = no;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
