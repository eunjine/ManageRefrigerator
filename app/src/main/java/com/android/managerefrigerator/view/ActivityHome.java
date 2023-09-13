package com.android.managerefrigerator.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.managerefrigerator.databinding.ActivityHomeBinding;
import com.android.managerefrigerator.databinding.ActivityMainBinding;

public class ActivityHome extends AppCompatActivity {
    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        /**
         * 시작 버튼을 누르면 로그인 화면으로 이동
         */
        binding.btStart.setOnClickListener(v -> {
            Intent i = new Intent(ActivityHome.this, ActivityLogin.class);
            startActivity(i);
            finish();
        });

//        binding.btStart.performClick();
    }
}
