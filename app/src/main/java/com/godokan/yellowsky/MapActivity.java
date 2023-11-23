package com.godokan.yellowsky;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;

    private MarkerInfoTask infoTask = new MarkerInfoTask();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync((OnMapReadyCallback) this);
    }

    // NULL이 아닌 GoogleMap 객체를 파라미터로 제공해 줄 수 있을 때 호출
    @Override
    public void onMapReady(@NonNull final GoogleMap googleMap) {
        mMap = googleMap;
        List<ApiListMapDTO> list;
        InitNetMapList mapList = new InitNetMapList();

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37.2410864, 127.1775537), 11));

        mapList.start();
        try {
            mapList.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        list = mapList.getMarkers();

        if(list!=null) {
            for (ApiListMapDTO map : list) {
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(map.getLat(),map.getLng()))
                        .title(map.getProperName())
                        .snippet(map.getAddress())
                );
            }
        }

    }

    private class InitNetMapList extends Thread {
        private List<ApiListMapDTO> markers = new ArrayList<>();
        @Override
        public void run() {
            markers = infoTask.getMarkerInfo();
        }

        public List<ApiListMapDTO> getMarkers() {
            return markers;
        }
    }
}
