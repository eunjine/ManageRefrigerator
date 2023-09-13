package com.android.managerefrigerator.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.managerefrigerator.R;
import com.android.managerefrigerator.data.TodoInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Objects;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TodoViewHolder> {
    private ArrayList<TodoInfo> tList;

    public TodoAdapter(ArrayList<TodoInfo> list) {
        super();
        tList = list;
    }

    @NonNull
    @Override
    public TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_todo, parent, false);
        return new TodoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoViewHolder holder, int position) {
        holder.bind(tList.get(position).getTodo(),tList.get(position).isChecked());
        holder.itemClick();
    }

    @Override
    public int getItemCount() {
        return tList.size();
    }

    public void addItem(TodoInfo info) {
        tList.add(info);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        tList.remove(position);
        notifyDataSetChanged();
    }

    public class TodoViewHolder extends RecyclerView.ViewHolder {
        private final FirebaseFirestore db = FirebaseFirestore.getInstance();
        static final String COLLECTION_PATH = "Users";
        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        private TextView tv;
        private CheckBox cb;
        private ImageView imdelete;

        public TodoViewHolder(@NonNull View itemView) {
            super(itemView);

            tv = itemView.findViewById(R.id.tv_todo);
            cb = itemView.findViewById(R.id.cb_check);
            imdelete = itemView.findViewById(R.id.im_delete_item);
        }

        void bind(String s,boolean isChecked) {
            Log.i("##INFO", "bind(): s = "+s);
            tv.setText(s);
            cb.setChecked(isChecked);
        }

        void itemClick() {
            imdelete.setOnClickListener(v -> {
                Log.i("##INFO", "itemClick(): click");
                tList.remove(getAdapterPosition());
                db.collection(COLLECTION_PATH).document(uid).update("Todo", tList).addOnSuccessListener(aVoid -> {
                    Toast.makeText(v.getContext(), "삭제 완료", Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                });

            });

            cb.setOnClickListener(v -> {
                if (cb.isChecked()) {
                    tList.get(getAdapterPosition()).setChecked(true);
                    cb.setChecked(true);
                } else {
                    tList.get(getAdapterPosition()).setChecked(false);
                    cb.setChecked(false);
                }

                db.collection(COLLECTION_PATH).document(uid).update("Todo", tList).addOnSuccessListener(aVoid -> {
                    Toast.makeText(v.getContext(), "체크 완료", Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                    cb.setChecked(false);
                });
            });
        }
    }
}
























