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

            reader.close();
            builder.setLength(0);
            is.close();
            conn.disconnect();

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

        return false;
    }

    //TODO : 회원 가입 요청 작성

    public boolean signUpTask(String id, String pw, String name) {
        String result = null;
        try {
            URL url = new URL(URL+"api/signup?id="+id+"&pw="+pw+"&name="+name);
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

            reader.close();
            builder.setLength(0);
            is.close();
            conn.disconnect();

        } catch (Exception e) {
            Log.e("YELLOW_SKY_REST_API", "POST method failed: " + e.getMessage());
            e.printStackTrace();
        }

        if (result.equals("OK"))
            return true;

        return false;
    }

    //TODO : ID 중복 체크 로직

    // ID 확인 : 있으면 true, 없으면 false 반환
    public boolean validateID(String id) {
        String result = null;
        try {
            URL url = new URL(URL+"api/chkValidateID?id="+id);
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

            reader.close();
            builder.setLength(0);
            is.close();
            conn.disconnect();

        } catch (Exception e) {
            Log.e("YELLOW_SKY_REST_API", "POST method failed: " + e.getMessage());
            e.printStackTrace();
        }

        if (result!=null) {
            switch (result) {
                case "YES" -> {
                    return true;
                }
                case "NO" -> {
                    return false;
                }
            }
        }
        return false;
    }

    //TODO : 닉네임 중복 체크 로직

    // 닉네임 확인 : 있으면 true, 없으면 false 반환
    public boolean validateName(String name) {
        String result = null;
        try {
            URL url = new URL(URL+"api/checkValidateName?name="+name);
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

            reader.close();
            builder.setLength(0);
            is.close();
            conn.disconnect();

        } catch (Exception e) {
            Log.e("YELLOW_SKY_REST_API", "POST method failed: " + e.getMessage());
            e.printStackTrace();
        }

        if (result!=null) {
            switch (result) {
                case "YES" -> {
                    return true;
                }
                case "NO" -> {
                    return false;
                }
            }
        }
        return false;
    }

    //TODO : 세션 유효 체크 로직
    public boolean checkSession() {
        String result = null;
        try {
            URL url = new URL(URL+"api/hasSession");
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

            reader.close();
            builder.setLength(0);
            is.close();
            conn.disconnect();

        } catch (Exception e) {
            Log.e("YELLOW_SKY_REST_API", "POST method failed: " + e.getMessage());
            e.printStackTrace();
        }

        if (result!=null) {
            switch (result) {
                case "YES" -> {
                    return true;
                }
                case "NO" -> {
                    return false;
                }
            }
        }
        return false;
    }


}
