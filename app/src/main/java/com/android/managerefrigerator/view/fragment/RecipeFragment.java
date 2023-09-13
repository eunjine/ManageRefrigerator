package com.android.managerefrigerator.view.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.managerefrigerator.data.FoodInfo;
import com.android.managerefrigerator.databinding.FragmentMainBinding;
import com.android.managerefrigerator.databinding.FragmentRecipeBinding;
import com.android.managerefrigerator.view.fragment.home.HomeFragment;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firestore.admin.v1.Index;

import java.util.ArrayList;

/**
 * 유저의 데이터를 기반으로 유튜브 연동
 */
public class RecipeFragment extends Fragment {
    private FragmentRecipeBinding binding;
    private ArrayList<FoodInfo> f;
    private int currentItem = 0;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRecipeBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setYoutube();
        onClick();
        loadYoutube();
    }

    private void onClick() {

        binding.imbtnPrev.setOnClickListener(v -> {
            if ((currentItem - 1) < 0) {
                currentItem = f.size() - 1;
            } else {
                currentItem--;
            }
            loadYoutube();
        });

        binding.imbtnNext.setOnClickListener(v -> {
            if ((currentItem + 1) > f.size() - 1) {
                currentItem = 0;
            } else {
                currentItem++;
            }
            loadYoutube();
        });
    }

    /**
     * webview 셋팅
     */
    private void setYoutube() {
        f = HomeFragment.foodsList;
        binding.webYoutube.clearCache(true);
        binding.webYoutube.setWebViewClient(new WebViewClient());
        binding.webYoutube.getSettings().setJavaScriptEnabled(true);
        binding.webYoutube.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        binding.webYoutube.getSettings().setSupportMultipleWindows(true);
        binding.webYoutube.getSettings().setLoadWithOverviewMode(true);
        binding.webYoutube.getSettings().setUseWideViewPort(true);
        binding.webYoutube.getSettings().setBuiltInZoomControls(true);
        binding.webYoutube.getSettings().setCacheMode(binding.webYoutube.getSettings().LOAD_NO_CACHE);
        binding.webYoutube.getSettings().setDefaultTextEncodingName("UTF-8");
    }

    private void loadYoutube() {
        try {
            binding.webYoutube.loadUrl("https://www.youtube.com/results?search_query=" + f.get(currentItem).getFoodName() + "+레시피");
            binding.tvFoodNameRecipe.setText(f.get(currentItem).getFoodName());
        } catch (IndexOutOfBoundsException error) {
            Toast.makeText(getContext(), "아직 데이터 로딩이 완료 되지 않았습니다.", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getContext(), "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
        }
    }
}





































