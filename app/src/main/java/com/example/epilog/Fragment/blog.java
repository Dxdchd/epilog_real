package com.example.epilog.Fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.epilog.Basic_BlogView;
import com.example.epilog.FromDb;
import com.example.epilog.ListAdapter;
import com.example.epilog.SelectBlogToBook;
import com.example.epilog.databinding.FragmentBlogBinding;
import com.example.epilog.friendListAdapter;
import com.example.epilog.makeblog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class blog extends Fragment {
    public ArrayList<FromDb> items = new ArrayList<>();
    public ArrayList<FromDb> itemsfrirends = new ArrayList<>();
    FragmentBlogBinding binding;
    int a = 0;
    String uid;
    FirebaseFirestore firestore;
    SharedPreferences preferences;
    ArrayList<String> frienduid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBlogBinding.inflate(getLayoutInflater());
        uid = this.getArguments().getString("uid");
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        preferences = getActivity().getSharedPreferences("UserInfo", MODE_PRIVATE);
        firestore = FirebaseFirestore.getInstance();
        firestore.collection("diary")
                .whereEqualTo("uid", uid)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                FromDb fromDb = document.toObject(FromDb.class);
                                items.add(fromDb);
                                Log.d("TAG", "onComplete: " + fromDb.getImgUrls().get(0));
                                Log.d("TAG", "onComplete: " + fromDb.getDate());
                                Log.d("TAG", document.getId() + " => " + document.getData());
                            }
                            ListAdapter adapter = new ListAdapter(items);
                            RecyclerView.LayoutManager mlayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                            binding.recycler1.setLayoutManager(mlayoutManager);
                            binding.recycler1.setAdapter(adapter);
                            adapter.setOnItemClickListener(new ListAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View v, int position) {
                                    FromDb fromDb = items.get(position);
                                    Log.d("TAG", "onItemClick: " + fromDb);
                                    Intent intent = new Intent(getContext(), Basic_BlogView.class);
                                    intent.putExtra("uid", fromDb.getUid());
                                    intent.putExtra("date", fromDb.getTimestamp());
                                    startActivity(intent);
                                }
                            });
                        }
                        if (items.size() == 0) {
                            binding.nomyblog.setVisibility(View.VISIBLE);
                        }
                    }
                });
        firestore.collection("user").document(preferences.getString("uid", "")).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                frienduid = (ArrayList<String>) documentSnapshot.get("friends");
                if (frienduid.size() != 0) {
                    firestore.collection("diary").whereIn("uid", frienduid).orderBy("timestamp", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    FromDb fromDb = document.toObject(FromDb.class);
                                    itemsfrirends.add(fromDb);
                                }
                                friendListAdapter adapter = new friendListAdapter(itemsfrirends);
                                RecyclerView.LayoutManager mlayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                                binding.recycler2.setLayoutManager(mlayoutManager);
                                binding.recycler2.setAdapter(adapter);
                                adapter.setOnItemClickListener(new friendListAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View v, int position) {
                                        FromDb fromDb = itemsfrirends.get(position);
                                        Intent intent = new Intent(getContext(), Basic_BlogView.class);
                                        intent.putExtra("uid", fromDb.getUid());
                                        intent.putExtra("date", fromDb.getTimestamp());
                                        startActivity(intent);
                                    }
                                });
                            }
                        }
                    });
                } else {
                    binding.nofriendsblog.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        items.clear();
        itemsfrirends.clear();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.makeblogbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent makeblogintent = new Intent(getContext(), makeblog.class);
                startActivity(makeblogintent);
            }
        });

        binding.booklistbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent booklistintent = new Intent(getContext(), SelectBlogToBook.class);
                startActivity(booklistintent);
            }
        });
    }

}