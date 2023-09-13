package com.android.managerefrigerator.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.managerefrigerator.databinding.ActivitySingUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class ActivitySignUp extends AppCompatActivity {
    private ActivitySingUpBinding binding;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySingUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        firebaseAuth = FirebaseAuth.getInstance();


        //로그인 버튼 클릭시 로그인 화면으로 이동
        binding.btJoin.setOnClickListener(v -> {
            String email = binding.edEmail.getText().toString();
            String password = binding.edPassword.getText().toString();
            String passwordCheck = binding.edPasswordCheck.getText().toString();

            if (email.isEmpty()) {
                return;
            }
            if (!password.equals(passwordCheck)) {
                Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_LONG).show();
                return;
            }

            //각 입력란이 공백이 아니고 비밀번호와 비밀번호 확인이 일치하면 회원가입
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // 회원가입 성공시
                                Toast.makeText(ActivitySignUp.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(ActivitySignUp.this, ActivityLogin.class);
                                startActivity(i);
                                finish();

                            } else {
                                task.addOnFailureListener(e -> {
                                    Log.e("SignUpActivity", "onFailure: " + e.getMessage());
                                });
                            }
                        }
                    });
        });
    }
}
