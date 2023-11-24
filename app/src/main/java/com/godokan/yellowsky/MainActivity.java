package com.godokan.yellowsky;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity{

    private static final MemberTask memberTask = new MemberTask();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText ed_id = findViewById(R.id.ed_it);
        EditText ed_pw = findViewById(R.id.ed_pw);
        Button btn_login = findViewById(R.id.btn_login);
        TextView tv_signup = findViewById(R.id.tv_signup);

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
                    startActivity(intent);
                } else {
                    Toast.makeText(this.getApplicationContext(), "로그인 실패", Toast.LENGTH_LONG).show();
                }
            }
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
}