package com.godokan.yellowsky;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MarkerInfoTask {

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
            System.out.println(result);
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
                mapDto = new ApiListMapDTO(obj.getInt("no"),obj.getString("name"),obj.getString("properName"),obj.getDouble("lat"),obj.getDouble("lng"),obj.getString("address"),obj.getString("placeUrl"));
                markers.add(mapDto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return markers;
    }
}
