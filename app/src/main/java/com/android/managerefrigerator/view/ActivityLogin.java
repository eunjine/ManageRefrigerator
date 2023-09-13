package com.android.managerefrigerator.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.managerefrigerator.databinding.ActivityHomeBinding;
import com.android.managerefrigerator.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class ActivityLogin extends AppCompatActivity {
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        /**
         * firebase에서 제공하는 auth기능을 활용하여 로그인 로직 구현.
         */
        binding.btLogin.setOnClickListener(view -> {
            FirebaseAuth auth;
            auth = FirebaseAuth.getInstance();

            String email = binding.edEmail.getText().toString();
            String passward = binding.edPassword.getText().toString();
            auth.signInWithEmailAndPassword(email, passward).
                    addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ActivityLogin.this, "로그인 성공", Toast.LENGTH_SHORT).show();

                                /**
                                 * 로그인 성공시 홈 화면으로 이동
                                 */
                                Intent i = new Intent(ActivityLogin.this, MainActivity.class);
                                startActivity(i);
                            } else {
                                Log.i("##INFO", "onComplete(): failure", task.getException());
                            }

                            task.addOnFailureListener(e -> {
                                Toast.makeText(ActivityLogin.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                                Log.i("##INFO", "onComplete(): e = " + e.getMessage());
                            });
                        }
                    });
        });

        /**
         * 회원가입 버튼 클릭시 화면 이동
         */
        binding.btSignup.setOnClickListener(v -> {
            Intent i = new Intent(ActivityLogin.this, ActivitySignUp.class);
            startActivity(i);
        });


//        binding.edEmail.setText("tester123@test.com");
//        binding.edPassword.setText("aaaaaa");
//        binding.btLogin.performClick();
    }
}
