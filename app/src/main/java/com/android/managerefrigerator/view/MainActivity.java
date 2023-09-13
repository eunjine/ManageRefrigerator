package com.android.managerefrigerator.view;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.android.managerefrigerator.R;
import com.android.managerefrigerator.adapter.ViewpagerAdapter;
import com.android.managerefrigerator.databinding.ActivityMainBinding;
import com.android.managerefrigerator.view.fragment.BasketFragment;
import com.android.managerefrigerator.view.fragment.home.HomeFragment;
import com.android.managerefrigerator.view.fragment.RecipeFragment;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initFragment();

    }

    /**
     * fragment의 화면을 viewpager에 초기화 히기 위한 메서드
     */
    private void initFragment() {
        //각 프래그먼트 생성
        ArrayList<Fragment> list = new ArrayList<>();
        list.add(new HomeFragment());
        list.add(new RecipeFragment());
        list.add(new BasketFragment());

        ViewpagerAdapter adapter = new ViewpagerAdapter(this, list);
        binding.viewpagerFragment.setAdapter(adapter);


        //바텀 네비게이션 클릭에 따른 홤녀 이동
        binding.navBottom.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.menu_home) {
                    binding.viewpagerFragment.setCurrentItem(0);
                    binding.navBottom.getMenu().getItem(0).setChecked(true);    //각 화면 이동에 따른 바텀 탭 선택 표시
                } else if (itemId == R.id.menu_recipe) {
                    binding.viewpagerFragment.setCurrentItem(1);
                    binding.navBottom.getMenu().getItem(1).setChecked(true);    //각 화면 이동에 따른 바텀 탭 선택 표시
                } else if (itemId == R.id.menu_basket) {
                    binding.viewpagerFragment.setCurrentItem(2);
                    binding.navBottom.getMenu().getItem(2).setChecked(true);    //각 화면 이동에 따른 바텀 탭 선택 표시
                }

                return false;
            }
        });
    }


}
