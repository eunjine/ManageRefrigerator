package com.android.managerefrigerator.view.fragment.home;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.managerefrigerator.R;
import com.android.managerefrigerator.data.FoodInfo;
import com.android.managerefrigerator.databinding.ItmeFoodBinding;
import com.android.managerefrigerator.view.dlg.DialogType;
import com.android.managerefrigerator.view.dlg.FoodDialog;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class AdapterHome extends RecyclerView.Adapter<AdapterHome.HomeViewHolder> {
    private ArrayList<FoodInfo> fList;
    private Context ctx;

    public AdapterHome(ArrayList<FoodInfo> list, Context context) {
        this.ctx = context;
        this.fList = list;
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItmeFoodBinding binding = ItmeFoodBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new HomeViewHolder(binding.getRoot(), fList);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        holder.bind(fList.get(position));
    }

    @Override
    public int getItemCount() {
        return fList.size();
    }

    public void addItem() {
        notifyDataSetChanged();
    }
    public class HomeViewHolder extends RecyclerView.ViewHolder implements FoodDialog.DlgResult {
        ItmeFoodBinding binding;
        ArrayList<FoodInfo> fList;
        private final FirebaseFirestore db = FirebaseFirestore.getInstance();
        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        private static final String COLLECTION_PATH = "Users";

        private FoodDialog foodDialog;
        private FoodInfo foodInfo;


        public HomeViewHolder(View itemView, ArrayList<FoodInfo> list) {
            super(itemView);
            binding = ItmeFoodBinding.bind(itemView);
            fList = list;

        }

        public void bind(FoodInfo foodInfo) {
            this.foodInfo = foodInfo;
            String foodName = foodInfo.getFoodName();
            String count = foodInfo.getFoodCount();
            String expireDate = foodInfo.getFoodDate();

            binding.tvFoodNameItem.setText(foodName);
            binding.tvCountItem.setText(count);
            binding.tvDateItem.setText(expireDate + "까지");

            Glide.with(itemView).load(foodInfo.getFoodImage()).into(binding.imFoodItem);

            setOnDeleteClick(foodName);
            setOnEditClick();
        }

        /**
         * firebase cloud Firestore에서 데이터 삭제
         * @param foodName
         */
        public void setOnDeleteClick(String foodName ) {
            binding.imDeleteItem.setOnClickListener(v -> {
                HashMap<String,Object> data = new HashMap<>();
                data.put(foodName, FieldValue.delete());    // filed name을 key로 데이터 삭제

                db.collection(COLLECTION_PATH).document(uid).update(data).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.i("##INFO", "itemClick(): delete success");
                    } else {
                        Log.i("##INFO", "itemClick(): delete fail");
                    }
                });

                fList.remove(getAdapterPosition());
                notifyItemRemoved(getAdapterPosition());
            });
        }

        public void setOnEditClick() {
            binding.layoutContainerItem.setOnClickListener(v -> {
                Log.i("##INFO", "setOnEditClick(): " + foodInfo.getFoodName());
                foodDialog = new FoodDialog(itemView.getContext(), this, DialogType.MODIFY);
                foodDialog.show();

                foodDialog.setFoodInfo(Uri.parse(foodInfo.foodImage),foodInfo.foodName,foodInfo.foodCount,foodInfo.foodDate);
            });
        }

        /**
         * 수정
         */
        @Override
        public void onClickPositive() {
            FoodInfo f  = foodDialog.getFoodInfo();

            db.collection(COLLECTION_PATH).document(uid).update(foodInfo.getFoodName(),f).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    foodDialog.dismiss();
                    fList.set(getAdapterPosition(), f);
                    notifyDataSetChanged();
                    Log.i("##INFO", "onClickPositive(): success");
                } else {
                    Log.i("##INFO", "onClickPositive(): update fail");
                }
            }).addOnFailureListener(e -> Log.i("##INFO", "onClickPositive(): " + e.getMessage()));
        }
    }
}









































