package com.godokan.yellowsky.Activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.godokan.yellowsky.Task.MemberTask;
import com.godokan.yellowsky.R;

public class SignUpActivity extends AppCompatActivity {
    private static final MemberTask memberTask = new MemberTask();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        EditText ed_id = findViewById(R.id.ed_it);
        EditText ed_pw = findViewById(R.id.ed_pw);
        EditText ed_nn = findViewById(R.id.ed_nn);
        Button btn_signup = findViewById(R.id.btn_signup);

        btn_signup.setOnClickListener(view -> {
            String id = ed_id.getText().toString();
            String pw = ed_pw.getText().toString();
            String nn = ed_nn.getText().toString();

            if (id.length()==0)
                Toast.makeText(this.getApplicationContext(), "아이디를 입력하시기 바랍니다", Toast.LENGTH_LONG).show();
            else if (pw.length()==0)
                Toast.makeText(this.getApplicationContext(), "비밀번호를 입력하시기 바랍니다", Toast.LENGTH_LONG).show();
            else if (nn.length()==0)
                Toast.makeText(this.getApplicationContext(), "닉네임을 입력하시기 바랍니다", Toast.LENGTH_LONG).show();
            else {
                boolean result = false;

                PostValidateID validateID = new PostValidateID(id);
                PostValidateName validateName = new PostValidateName(nn);
                PostNetSignUp postNetSignUp = new PostNetSignUp(id,pw,nn);

                validateID.start();
                validateName.start();

                try {
                    validateID.join();
                    validateName.join();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                boolean valID = validateID.getValidateResult();
                boolean valName = validateName.getValidateResult();

                if (valID) {
                    Toast.makeText(this.getApplicationContext(), "이미 사용중인 아아디입니다", Toast.LENGTH_LONG).show();
                } else if (valName) {
                    Toast.makeText(this.getApplicationContext(), "이미 사용중인 닉네임입니다", Toast.LENGTH_LONG).show();
                } else {
                    postNetSignUp.start();

                    try {
                        postNetSignUp.join();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    result = postNetSignUp.getSignUpResult();
                }

                if (result) {
                    Toast.makeText(this.getApplicationContext(), "회원가입 성공", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });
    }

    private class PostNetSignUp extends Thread {
        private String id;
        private String pw;
        private String name;
        private boolean result = false;

        public PostNetSignUp(String id, String pw, String name) {
            this.id = id;
            this.pw = pw;
            this.name = name;
        }

        @Override
        public void run() {
            result = memberTask.signUpTask(id, pw, name);
        }

        public boolean getSignUpResult() { return result; }
    }

    private class PostValidateID extends Thread {
        private String id;
        private boolean result = false;


        public PostValidateID(String id) {
            this.id = id;
        }

        @Override
        public void run() {
            result = memberTask.validateID(id);
        }

        public boolean getValidateResult() { return result; }

    }

    private class PostValidateName extends Thread {
        private String name;
        private boolean result = false;


        public PostValidateName(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            result = memberTask.validateName(name);
        }

        public boolean getValidateResult() { return result; }

    }
}
