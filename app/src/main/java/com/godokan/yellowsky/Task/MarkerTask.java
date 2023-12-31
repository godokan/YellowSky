package com.godokan.yellowsky.Task;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.godokan.yellowsky.DTO.ApiListMapDTO;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MarkerTask {

    public List<ApiListMapDTO> getMarkerInfo() {
        String result = null;
        ApiListMapDTO mapDto;
        List<ApiListMapDTO> markers = new ArrayList<>();
        String KEY = "jPUXr41vcXxyiZvvOLL1JQ==:RGvYtuQPvozPR3dZL3zpKKThTN0x1CG1XPF6WcmUlDE=";
        String STUD_NUM = "202155012";

        try {
            URL url = new URL("http://ccsyasu.cafe24.com:81/api/map?key="+ KEY +"&studNum="+ STUD_NUM);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
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
        }
        catch (Exception e) {
            // Error calling the rest api
            Log.e("REST_API", "GET method failed: " + e.getMessage());
            e.printStackTrace();
        }

        if (result == null) return null;

        try {
            JSONArray jArray = new JSONArray(result);
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject obj = jArray.getJSONObject(i);
                mapDto = new ApiListMapDTO(obj.getInt("no"),obj.getString("name"),obj.getDouble("lat"),obj.getDouble("lng"),obj.getString("address"));
                markers.add(mapDto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return markers;
    }

    // 새 장소 추가
    public boolean postNewPlace(Context context, LatLng latLng, String name) {
        String result = null;

        // 위도 경도 정보로 주소 찾기
        Geocoder geocoder = new Geocoder(context, Locale.KOREA);
        List<Address> address;
        String addr = "";
        try {
            address = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (address != null && address.size() > 0) {
                addr = address.get(0).getAddressLine(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            URL url = new URL("http://ccsyasu.cafe24.com:81/api/map/new?name="+name+"&lat="+latLng.latitude+"&lng="+latLng.longitude+"&address="+addr);
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

            reader.close();
            builder.setLength(0);
            is.close();
            conn.disconnect();
        }
        catch (Exception e) {
            // Error calling the rest api
            Log.e("REST_API", "GET method failed: " + e.getMessage());
            e.printStackTrace();
        }

        return result != null && result.equals("OK");
    }

    public boolean patchEditPlace(String name, ApiListMapDTO mapDTO) {
        String result = null;

        try {
            URL url = new URL("http://ccsyasu.cafe24.com:81/api/map/edit?name="+name+"&lat="+mapDTO.getLat()+"&lng="+mapDTO.getLng()+"&address="+mapDTO.getAddress()+"&no="+mapDTO.getNo());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PATCH");
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

            reader.close();
            builder.setLength(0);
            is.close();
            conn.disconnect();
        }
        catch (Exception e) {
            // Error calling the rest api
            Log.e("REST_API", "GET method failed: " + e.getMessage());
            e.printStackTrace();
        }

        return result != null && result.equals("OK");
    }

    public boolean deletePlace(Integer no) {
        String result = null;

        try {
            URL url = new URL("http://ccsyasu.cafe24.com:81/api/map/delete?no="+no);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");
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

            reader.close();
            builder.setLength(0);
            is.close();
            conn.disconnect();
        }
        catch (Exception e) {
            // Error calling the rest api
            Log.e("REST_API", "GET method failed: " + e.getMessage());
            e.printStackTrace();
        }

        return result != null && result.equals("OK");
    }
}
