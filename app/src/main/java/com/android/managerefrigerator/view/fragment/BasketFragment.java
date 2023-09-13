package com.android.managerefrigerator.view.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.managerefrigerator.adapter.TodoAdapter;
import com.android.managerefrigerator.data.TodoInfo;
import com.android.managerefrigerator.databinding.FragmentBasketBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class BasketFragment extends Fragment {
    private FragmentBasketBinding binding;
    private TodoAdapter todoAdapter;
    private ArrayList<TodoInfo> list;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    static final String COLLECTION_PATH = "Users";
    String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBasketBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();
        getTodoData();
        itemClickEvent();
    }

    private void init() {
        list = new ArrayList<>();
        todoAdapter = new TodoAdapter(list);
        binding.reTodo.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.reTodo.setAdapter(todoAdapter);
    }

    /**
     * 유저의 todo 데이터 가져오기
     */
    private void getTodoData() {
        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        Log.i("##INFO", "getFoodInfo(): uid = " + uid);



        db.collection(COLLECTION_PATH).document(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.getData() != null){
                    db.collection(COLLECTION_PATH).document(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {

                                    Map<String, Object> res = documentSnapshot.getData();

                                    assert res != null;
                                    res.forEach((key, value) -> {
                                                try {
                                                    ArrayList<HashMap<String, TodoInfo>> v = (ArrayList<HashMap<String, TodoInfo>>) value;
                                                    v.forEach((s) -> {
                                                        String t = String.valueOf(s.get("todo"));
                                                        Boolean b = Boolean.valueOf(String.valueOf(s.get("checked")));
                                                        Log.i("##INFO", "onSuccess(): t.getTodo(); = "+ t);
                                                        Log.i("##INFO", "onSuccess(): t.isChecked(); = "+ b);

                                                        list.add(new TodoInfo(t, b));
                                                        todoAdapter.notifyDataSetChanged();
                                                    });

                                                } catch (ClassCastException e) {
                                                    Log.e("##ERROR", "getTodoData = " + e.getMessage());
                                                }
//
                                            }
                                    );

                            } else {
                                Toast.makeText(requireContext(), "데이터가 없습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }

    /**
     * add 아이템 클릭 이벤트
     */
    private void itemClickEvent() {
        binding.imAdd.setOnClickListener(v -> {
            String todoName = binding.edTodo.getText().toString();

            list.add(new TodoInfo(todoName, false));
            HashMap<String, Object> h = new HashMap<>();
            h.put("Todo", list);
            db.collection(COLLECTION_PATH).document(uid).update(h).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(requireContext(), "추가되었습니다.", Toast.LENGTH_SHORT).show();
                    todoAdapter.notifyDataSetChanged();
                    binding.edTodo.setText("");
                }
            });
        });
    }
}























