package com.godokan.yellowsky;


import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class MemberTask {
    private final String  URL = "http://ccsyasu.cafe24.com:8082/";

    // 로그린
    public boolean loginTask(String id, String pw) {
        String result = null;
        MemberDTO memberDTO;
        try {
            URL url = new URL(URL+"api/login?id="+id+"&pw="+pw);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type","application/json;charset=UTF-8");
            InputStream is = conn.getInputStream();
            StringBuilder builder = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }

            // Set the result
            result = builder.toString();
            System.out.println(result);

        } catch (Exception e) {
            Log.e("YELLOW_SKY_REST_API", "POST method failed: " + e.getMessage());
            e.printStackTrace();
        }

        if (result!=null) {
            switch (result) {
                case "OK" -> {
                    return true;
                }
                case "ERR_USER_NOT_FOUND", "ERR_PW_NOT_MATCH" -> {
                    return false;
                }
            }
        }

        System.out.println(result);

        return false;
    }

    //TODO : 회원 가입 요청 작성

    //TODO : ID 중복 체크 로직
    //TODO : 닉네임 중복 체크 로직
    //TODO : 세션 유효 체크 로직



}
