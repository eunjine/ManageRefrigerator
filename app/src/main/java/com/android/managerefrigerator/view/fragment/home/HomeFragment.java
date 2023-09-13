package com.android.managerefrigerator.view.fragment.home;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.android.managerefrigerator.data.FoodInfo;
import com.android.managerefrigerator.databinding.FragmentMainBinding;
import com.android.managerefrigerator.view.dlg.DialogType;
import com.android.managerefrigerator.view.dlg.FoodDialog;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import kotlin.collections.ArraysKt;

public class HomeFragment extends Fragment implements FoodDialog.DlgResult {
    static final String COLLECTION_PATH = "Users";

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FragmentMainBinding binding;
    private FoodDialog foodDialog;
    private AdapterHome adapterHome;
    public static ArrayList<FoodInfo> foodsList;
    Boolean res;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();
        setEvent();
        getFoodInfo();
    }

    private void init() {
        foodDialog = new FoodDialog(requireContext(), this, DialogType.ADD);
        foodsList = new ArrayList<>();
        binding.reFood.setLayoutManager(new GridLayoutManager(requireContext(), 3));
        adapterHome = new AdapterHome(foodsList, requireContext());
    }

    private void setEvent() {
        binding.imSearch.setOnClickListener(v -> {
            String foodName = binding.edSearch.getText().toString();
            getImage(foodName);
        });
    }

    void getFoodInfo() {
        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        Log.i("##INFO", "getFoodInfo(): uid = " + uid);

        db.collection(COLLECTION_PATH).document(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.i("##INFO", "onSuccess(): documentSnapshot = " + documentSnapshot.getData());
                if (documentSnapshot.getData() != null) {
                    db.collection(COLLECTION_PATH).document(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                Log.i("##INFO", "onSuccess(): documentSnapshot = " + documentSnapshot.getData());

                                    Map<String, Object> res = documentSnapshot.getData();

                                    assert res != null;
                                    res.forEach((key, value) -> {
                                                try {
                                                    HashMap<String, Object> v = (HashMap<String, Object>) value;

                                                    FoodInfo data = new FoodInfo(
                                                            (String) v.get("foodName"),
                                                            (String) v.get("foodDate"),
                                                            (String) v.get("foodImage"),
                                                            (String) v.get("foodCount")
                                                    );

                                                    foodsList.add(data);
                                                } catch (ClassCastException e) {
                                                    Log.e("##ERROR", "ClassCastException = " + e.getMessage());
                                                }
                                            }
                                    );

                            } else {
                                Toast.makeText(requireContext(), "데이터가 없습니다.", Toast.LENGTH_SHORT).show();
                            }
                            foodsList.forEach(item -> Log.i("##INFO", "onSuccess(): item = " + item.foodName));
                            adapterHome = new AdapterHome(foodsList, requireContext());
                            binding.reFood.setLayoutManager(new GridLayoutManager(requireContext(), 3));
                            binding.reFood.setAdapter(adapterHome);
                        }
                    });
                }
            }
        }).addOnFailureListener(e -> {
            Log.e("##ERROR", "getFoodInfo() = " + e.getMessage());
        });


    }

    /**
     * firebase storage에 저장된 이미지를 가져온다.
     *
     * @param foodName : 검색한 음식 이름
     */
    private void getImage(String foodName) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();

        storageReference.child("food").listAll().addOnSuccessListener(listResult -> {

            for (StorageReference item : listResult.getItems()) {

                String s = item.getName().substring(0, item.getName().indexOf("."));
                String chagnedTitle = Normalizer.normalize(s, Normalizer.Form.NFC);

                if (chagnedTitle.equals(foodName)) {
                    item.getDownloadUrl().addOnSuccessListener(uri -> {
                        setImage(uri, foodName);
                    });
                }
            }
        }).addOnFailureListener(e -> {
            Log.e("##ERROR", "getImage(): error = " + e);
        });
    }

    /**
     * storage에서 가져온 uri를 foodDialog에 전달한다.
     *
     * @param uri      : storage에서 가져온 이미지 uri
     * @param foodName : 검색한 음식 이름
     */
    private void setImage(Uri uri, String foodName) {
        foodDialog.show();
        foodDialog.setFoodInfo(uri, foodName, "", "");
    }


    @Override
    public void onClickPositive() {
        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        Log.i("##INFO", "onClickPositive(): uid = "+uid);
        Map<String, Object> food = new HashMap<>();
        food.put(foodDialog.getFoodInfo().getFoodName(), foodDialog.getFoodInfo());

        db.collection(COLLECTION_PATH).document(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                // document가 없으면 생성, 있으면 업데이트
                if (documentSnapshot.getData() == null) {
                    db.collection(COLLECTION_PATH).document(uid).set(food).addOnSuccessListener(aVoid -> {
                        foodDialog.dismiss();
                    }).addOnFailureListener(e -> {
                        Log.e("##ERROR", "onClickAdd(): error = " + e);
                    });
                } else {
                    db.collection(COLLECTION_PATH).document(uid).update(food).addOnSuccessListener(aVoid -> {
                        foodDialog.dismiss();
                    }).addOnFailureListener(e -> {
                        Log.e("##ERROR", "onClickAdd(): error = " + e);
                    });
                }
                binding.edSearch.setText("");
                foodsList.add(foodDialog.getFoodInfo());
                adapterHome = new AdapterHome(foodsList, requireContext());
                binding.reFood.setAdapter(adapterHome);
            }
        });

    }

}























































