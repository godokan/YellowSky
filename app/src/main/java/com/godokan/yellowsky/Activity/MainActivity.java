package com.godokan.yellowsky.Activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.godokan.yellowsky.Task.MemberTask;
import com.godokan.yellowsky.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends AppCompatActivity{

    private static final MemberTask memberTask = new MemberTask();
    private FusedLocationProviderClient fusedLocationClient;
    double[] lat_lng = new double[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        EditText ed_id = findViewById(R.id.ed_it);
        EditText ed_pw = findViewById(R.id.ed_pw);
        Button btn_login = findViewById(R.id.btn_login);
        TextView tv_signup = findViewById(R.id.tv_signup);


        checkLocationPermission();

        // 로그인
        btn_login.setOnClickListener(view -> {
            String id = ed_id.getText().toString();
            String pw = ed_pw.getText().toString();

            if (id.length()==0)
                Toast.makeText(this.getApplicationContext(), "아이디를 입력하시기 바랍니다", Toast.LENGTH_LONG).show();
            else if (pw.length()==0)
                Toast.makeText(this.getApplicationContext(), "비밀번호를 입력하시기 바랍니다", Toast.LENGTH_LONG).show();
            else {
                PostNetLogin netLogin = new PostNetLogin(id, pw);

                netLogin.start();

                try {
                    netLogin.join();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                boolean result = netLogin.getLoginResult();
                if (result) {
                    Intent intent = new Intent(getApplicationContext(), MapActivity.class);
                    intent.putExtra("lat-lng", lat_lng);
                    startActivity(intent);
                } else {
                    Toast.makeText(this.getApplicationContext(), "로그인 실패", Toast.LENGTH_LONG).show();
                }
            }
        });

        // 회원가입
        tv_signup.setOnClickListener(view-> {
            Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
            startActivity(intent);
        });
    }

    private class PostNetLogin extends Thread{
        private String id;
        private String pw;
        private boolean result = false;

        public PostNetLogin(String id, String pw) {
            this.id = id;
            this.pw = pw;
        }

        @Override
        public void run() {
            result = memberTask.loginTask(id, pw);
        }

        public boolean getLoginResult() {
            return result;
        }
    }

    private final ActivityResultLauncher<String[]> locationPermissionRequest = registerForActivityResult(
            new ActivityResultContracts.RequestMultiplePermissions(),
            result -> {
                Boolean fineLocationGranted = result.get(Manifest.permission.ACCESS_FINE_LOCATION);
                Boolean coarseLocationGranted = result.get(Manifest.permission.ACCESS_COARSE_LOCATION);

                if (!((fineLocationGranted != null && fineLocationGranted)||(coarseLocationGranted != null && coarseLocationGranted))) {
                    Toast.makeText(this,
                            "위치 권한 없이는 이 앱을 사용할 수 없습니다.",
                            Toast.LENGTH_LONG
                    ).show();
                    finish();
                }
            }
    );

    private void checkLocationPermission() {
        boolean coarseLocationGranted = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED;

        boolean fineLocationGranted = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED;

        if (!coarseLocationGranted && !fineLocationGranted) {
            locationPermissionRequest.launch(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            });
        } else {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            lat_lng[0] = location.getLatitude();
                            lat_lng[1] = location.getLongitude();
                        }
                    });
        }
    }
}