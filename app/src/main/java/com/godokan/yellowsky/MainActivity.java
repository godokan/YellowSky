package com.godokan.yellowsky;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText ed_id = findViewById(R.id.ed_it);
        EditText ed_pw = findViewById(R.id.ed_pw);
        Button btn_login = findViewById(R.id.btn_login);
        TextView tv_signup = findViewById(R.id.tv_signup);

        btn_login.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MapActivity.class);
            startActivity(intent);
        });
    }
}