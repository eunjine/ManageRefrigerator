package com.android.managerefrigerator.view.dlg;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.Window;

import com.android.managerefrigerator.R;
import com.android.managerefrigerator.data.FoodInfo;
import com.android.managerefrigerator.databinding.DialogFoodBinding;
import com.bumptech.glide.Glide;

public class FoodDialog {

    private Context mContext;
    private DialogFoodBinding binding;
    Dialog dlg;
    DlgResult dlgResult;
    private Uri foodImage;

    public FoodDialog(Context context, DlgResult callback,DialogType type) {
        mContext = context;
        dlgResult = callback;

        binding = DialogFoodBinding.inflate(LayoutInflater.from(mContext));

        dlg = new Dialog(mContext);
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dlg.setCanceledOnTouchOutside(false);
        dlg.setCancelable(false);

        dlg.setContentView(binding.getRoot());

        if (type == DialogType.MODIFY) {
            binding.btAdd.setText("수정");
        }
        binding.btCancel.setOnClickListener(v -> {
            dismiss();
        });
        binding.btAdd.setOnClickListener(v -> {
           dlgResult.onClickPositive();
        });
    }

    public void show() {
        dlg.show();
    }

    public void dismiss() {
        dlg.dismiss();
    }

    public void setFoodInfo(Uri uri,String foodName, String count,String expireDate) {
        foodImage = uri;
        binding.tvFoodName.setText(foodName);
        binding.edCount.setText(count);
        binding.edExpireDate.setText(expireDate);
        Glide.with(mContext).load(uri).into(binding.imFood);
    }

    public FoodInfo getFoodInfo() {
        return new FoodInfo(
                binding.tvFoodName.getText().toString(),
                binding.edExpireDate.getText().toString(),
                foodImage.toString(),
                binding.edCount.getText().toString()
        );
    }


    /**
     * Dialog의 버튼 이벤트를 처리하기 위한 인터페이스
     */
    public interface DlgResult {

        /**
         * Dialog의 추가 버튼을 눌렀을 때 호출되는 메소드
         */
        void onClickPositive();

    }
}










































