package com.godokan.yellowsky.Activity;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.godokan.yellowsky.DTO.ApiListMapDTO;
import com.godokan.yellowsky.Task.MarkerTask;
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
    private final MarkerTask markerTask = new MarkerTask();
    Marker tempMarker = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
    }

    // null 이 아닌 GoogleMap 객체를 파라미터로 제공해 줄 수 있을 때 호출
    @Override
    public void onMapReady(@NonNull final GoogleMap googleMap) {
        mMap = googleMap;
        List<ApiListMapDTO> list;
        InitNetMapList mapList = new InitNetMapList();


        double[] lat_lng = getIntent().getDoubleArrayExtra("lat-lng");
        if (lat_lng!=null)
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat_lng[0], lat_lng[1]), 15));

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
                        .title(map.getName())
                        .snippet(map.getAddress())
                ).setTag(map);
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
        // 이중 다이얼 로그 설계
        // dlg : 입력 여부
        // dlg2 : 내용 입력

        AlertDialog.Builder dlg = new AlertDialog.Builder(MapActivity.this);
        AlertDialog.Builder dlg2 = new AlertDialog.Builder(MapActivity.this);

        // dlg2에서 사용 할 뷰
        View dialogView = View.inflate(MapActivity.this, R.layout.init_loc_data, null);
        EditText name = dialogView.findViewById(R.id.ed_proper_name);
        dlg2.setView(dialogView);

        // 새 장소 입력의 경우
        if (marker.equals(tempMarker)) {
            // 나중에 보여질 다이얼로그
            dlg2.setTitle("새 장소 이름 입력");
            dlg2.setNegativeButton("확인", (dialog, which) -> {
                NewMapMarker newMapMarker = new NewMapMarker(this.getApplicationContext(), marker.getPosition(), name.getText().toString());
                newMapMarker.start();
                try {
                    newMapMarker.join();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if (newMapMarker.getResult()) {
                    Toast.makeText(this.getApplicationContext(), "새 장소가 등록되었습니다.", Toast.LENGTH_LONG).show();
                    finish();
                    startActivity(new Intent(MapActivity.this, MapActivity.class).putExtra("lat-lng",new double[]{marker.getPosition().latitude, marker.getPosition().longitude}));
                } else {
                    Toast.makeText(this.getApplicationContext(), "새 장소 등록에 실패하였습니다..", Toast.LENGTH_LONG).show();
                }
            });
            dlg2.setPositiveButton("닫기", null);

            // 먼저 보여질 다이얼로그
            dlg.setTitle("새 장소 추가");
            dlg.setMessage("새 장소를 추가 하시겠습니까?");
            dlg.setNegativeButton("확인", (dialog, which) -> dlg2.show());
            dlg.setPositiveButton("닫기", null);
            dlg.show();
        } else {
            AlertDialog.Builder dlg3 = new AlertDialog.Builder(MapActivity.this);
            ApiListMapDTO map = (ApiListMapDTO) marker.getTag();
            assert map != null;

            // 나중에 보여질 다이얼로그 (수정)
            dlg2.setTitle("새 장소 이름 입력");
            dlg2.setNegativeButton("확인", (dialog, which) -> {
                EditMapMarker editMapMarker = new EditMapMarker(name.getText().toString(), map);
                editMapMarker.start();
                try {
                    editMapMarker.join();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if (editMapMarker.getResult()) {
                    Toast.makeText(this.getApplicationContext(), "장소 정보를 수정하였습니다.", Toast.LENGTH_LONG).show();
                    finish();
                    startActivity(new Intent(MapActivity.this, MapActivity.class).putExtra("lat-lng",new double[]{marker.getPosition().latitude, marker.getPosition().longitude}));
                } else {
                    Toast.makeText(this.getApplicationContext(), "장소 수정에 실패하였습니다..", Toast.LENGTH_LONG).show();
                    finish();
                    startActivity(new Intent(MapActivity.this, MapActivity.class).putExtra("lat-lng",new double[]{marker.getPosition().latitude, marker.getPosition().longitude}));
                }
            });
            dlg2.setPositiveButton("닫기", null);

            // 나중에 보여질 다이얼로그 (수정)
            dlg3.setTitle("정말로 삭제 하시겠습니까?");
            dlg3.setMessage("삭제한 정보는 되돌릴 수 없습니다.");
            dlg3.setNegativeButton("확인", (dialog, which) -> {
                DeleteMapMarker deleteMapMarker = new DeleteMapMarker(map.getNo());
                deleteMapMarker.start();
                try {
                    deleteMapMarker.join();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if(deleteMapMarker.getResult()) {
                    Toast.makeText(this.getApplicationContext(), "장소를 삭제 하였습니다.", Toast.LENGTH_LONG).show();
                    finish();
                    startActivity(new Intent(MapActivity.this, MapActivity.class).putExtra("lat-lng",new double[]{marker.getPosition().latitude, marker.getPosition().longitude}));
                } else {
                    Toast.makeText(this.getApplicationContext(), "장소 삭제에 실패하였습니다..", Toast.LENGTH_LONG).show();
                    finish();
                    startActivity(new Intent(MapActivity.this, MapActivity.class).putExtra("lat-lng",new double[]{marker.getPosition().latitude, marker.getPosition().longitude}));
                }
            });
            dlg3.setPositiveButton("닫기", null);

            // 먼저 보여질 다이얼로그
            dlg.setTitle("장소 수정");
            dlg.setMessage("장소 정보를 수정 하시겠습니까?");
            dlg.setNegativeButton("수정", (dialog, which) -> dlg2.show());
            dlg.setNeutralButton("삭제", (dialog, which) -> dlg3.show());
            dlg.setPositiveButton("닫기", null);
            dlg.show();
        }
    }

    private class InitNetMapList extends Thread {
        private List<ApiListMapDTO> markers = new ArrayList<>();
        @Override
        public void run() {
            markers = markerTask.getMarkerInfo();
        }

        public List<ApiListMapDTO> getMarkers() {
            return markers;
        }
    }

    private class NewMapMarker extends Thread {
        private boolean result = false;

        private final Context context;
        private final LatLng latLng;
        private final String name;

        public NewMapMarker(Context context, LatLng latLng, String name) {
            this.context = context;
            this.latLng = latLng;
            this.name = name;
        }

        @Override
        public void run() {
            result = markerTask.postNewPlace(context, latLng, name);
        }

        public boolean getResult() {
            return result;
        }
    }

    private class EditMapMarker extends Thread {
        private boolean result = false;

        private final String name;
        private final ApiListMapDTO mapDTO;

        public EditMapMarker(String name, ApiListMapDTO mapDTO) {
            this.name = name;
            this.mapDTO = mapDTO;
        }

        @Override
        public void run() {
            result = markerTask.patchEditPlace(name, mapDTO);
        }

        public boolean getResult() {
            return result;
        }
    }

    private class DeleteMapMarker extends Thread {
        private boolean result = false;
        private final Integer no;

        public DeleteMapMarker(Integer no) {
            this.no = no;
        }

        @Override
        public void run() {
            result = markerTask.deletePlace(no);
        }

        public boolean getResult() {
            return result;
        }
    }
}
