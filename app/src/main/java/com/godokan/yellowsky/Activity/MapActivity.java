package com.godokan.yellowsky.Activity;

import android.app.AlertDialog;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.godokan.yellowsky.DTO.ApiListMapDTO;
import com.godokan.yellowsky.Task.MarkerInfoTask;
import com.godokan.yellowsky.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener, GoogleMap.OnInfoWindowClickListener {
    private GoogleMap mMap;
    private final MarkerInfoTask infoTask = new MarkerInfoTask();
    Marker tempMarker = null;


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

        Geocoder geocoder = new Geocoder(this.getApplicationContext());
        ApiListMapDTO mapDTO;

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

        mMap.setOnMapLongClickListener(this);

        mMap.setOnInfoWindowClickListener(this);

    }

    @Override
    public void onMapLongClick(@NonNull LatLng point) {
        if(tempMarker!=null)
            tempMarker.remove();
        tempMarker = mMap.addMarker(new MarkerOptions()
                .position(point)
                .title("새 장소")
                .snippet("여기를 눌러 새 장소를 등록 해 보세요!"));
    }

    @Override
    public void onInfoWindowClick(@NonNull Marker marker) {
        AlertDialog.Builder dlg = new AlertDialog.Builder(MapActivity.this);
        if (marker.equals(tempMarker)) {
            dlg.setTitle("새 장소 추가");
            dlg.setMessage("새 장소를 추가 하시겠습니까?");
            dlg.setNegativeButton("확인", (dialog, which) -> {

            });
            dlg.setPositiveButton("닫기", null);
            dlg.show();
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
