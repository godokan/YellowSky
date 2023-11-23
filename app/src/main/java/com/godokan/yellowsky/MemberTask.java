package com.godokan.yellowsky;


import java.net.MalformedURLException;
import java.net.URL;

public class MemberTask {
    private final String  URL = "http://ccsyasu.cafe24.com:8082/";

    public boolean loginTask(String id, String pw) {
        String result = null;
        try {
            URL url = new URL(URL+"?id="+id+"&pw="+pw);
            //TODO : YellowSkyAPI와 통신 로직 작성
            // POST방식, 결과코드 "ERR_USER_NOT_FOUND", "ERR_PW_NOT_MATCH", "OK"
            // 각각의 결과에 대응할것
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

    }

    //TODO : 회원 가입 요청 작성

}
