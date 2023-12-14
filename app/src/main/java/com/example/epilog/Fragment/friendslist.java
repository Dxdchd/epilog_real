package com.example.epilog.Fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.epilog.BottomDialogAdapter;
import com.example.epilog.BottomDialogFragment;
import com.example.epilog.FriendAdapter;
import com.example.epilog.HomeActivity;
import com.example.epilog.R;
import com.example.epilog.User;
import com.example.epilog.databinding.FragmentFriendslistBinding;
import com.example.epilog.friendrequestlist;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class friendslist extends Fragment {
    FragmentFriendslistBinding bd;
    AlertDialog add;
    EditText addemail;
    ArrayList<friendrequestlist> list;
    ArrayList<String>frienduid;
    ArrayList<User>userdata;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    SharedPreferences preferences;
    int cnt = 0;
    FragmentManager fragmentManager;
    String nick;
    Context context;
    private DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (dialog == add && which == DialogInterface.BUTTON_POSITIVE) {
                String getEmail = addemail.getText().toString();
                if (getEmail.equals(preferences.getString("email", ""))) {
                    Toast.makeText(getActivity(), "본인의 이메일입니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("TAG", "onClick: ");
                    db.collection("user").whereEqualTo("email", getEmail).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            Log.d("TAG", "onComplete: sdsd");
                            if (task.isSuccessful()) {
                                Log.d("TAG", "onComplete: sdsdds");
                                Log.d("TAG", "onComplete: " + task.getResult().size());
                                if (task.getResult().size() == 0) {
                                    Toast.makeText(getActivity(), "에필로그 사용자를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
                                } else {
                                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                        Log.d("TAG", "onComplete: adasdsa");
                                        String documentId = documentSnapshot.getId();
                                        db.collection("user").document(documentId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                Log.d("TAG", "onComplete: task1");
                                                if (task.isSuccessful()) {
                                                    Log.d("TAG", "onComplete: task2");
                                                    DocumentSnapshot document = task.getResult();
                                                    if (document.exists()) {
                                                        nick = (String) document.get("nickname");
                                                        ArrayList<String> friendslist = (ArrayList<String>) document.get("friends");
                                                        if (friendslist != null && friendslist.contains(preferences.getString("uid", ""))) {
                                                            Toast.makeText(getActivity(), "이미 친구입니다.", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            Map<String, Object> addfriend = new HashMap<>();
                                                            addfriend.put("from", preferences.getString("email", ""));
                                                            addfriend.put("to", getEmail);
                                                            db.collection("friendrequest")
                                                                    .document(preferences.getString("email", "") + getEmail)
                                                                    .set(addfriend).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void unused) {
                                                                            Toast.makeText(getActivity(), nick + "님에게 이웃 요청을 보냈습니다.", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    });
//                                                            Map<String, Object> addfriend = new HashMap<>();
//                                                            addfriend.put("from", preferences.getString("uid", ""));
//                                                            addfriend.put("to", getEmail);
//                                                            db.collection("user")
//                                                                    .whereEqualTo("email", getEmail)
//                                                                    .get()
//                                                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                                                                        @Override
//                                                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                                                                            Log.d("TAG", "onSuccess: ");
//                                                                            List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
//                                                                            if (documents.size() != 0) {
//                                                                                db.collection("friendrequest")
//                                                                                        .document(preferences.getString("email", "") + getEmail)
//                                                                                        .set(addfriend).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                                                                            @Override
//                                                                                            public void onSuccess(Void unused) {
//                                                                                                Toast.makeText(getActivity(), documents.get(0).get("nickname") + "님에게 이웃 요청을 보냈습니다.", Toast.LENGTH_SHORT).show();
//                                                                                            }
//                                                                                        });
//                                                                            } else {
//                                                                                Toast.makeText(getActivity(), "에필로그 작성자를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
//                                                                            }
//                                                                        }
//                                                                    }).addOnFailureListener(new OnFailureListener() {
//                                                                        @Override
//                                                                        public void onFailure(@NonNull Exception e) {
//                                                                            Log.d("TAG", "onFailure: ");
//                                                                        }
//                                                                    });
                                                        }
                                                    }
                                                } else {
                                                    Log.d("TAG", "onComplete: taskfail");
                                                }
                                            }
                                        });
                                    }
                                }
                            }
                        }
                    });
                }
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getContext();
        bd = FragmentFriendslistBinding.inflate(getLayoutInflater());
        preferences = getActivity().getSharedPreferences("UserInfo", MODE_PRIVATE);
        list = new ArrayList<>();

        db.collection("user").document(preferences.getString("uid","")).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                frienduid = (ArrayList<String>) documentSnapshot.get("friends");
                if(frienduid.size()!=0){
                    userdata = new ArrayList<>();
                    for(String uid : frienduid){
                        db.collection("user").document(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                User user = documentSnapshot.toObject(User.class);
                                userdata.add(user);
                                Log.d("TAG", "불러오기onSuccess: ");
                                bd.friendrecyclerView.setLayoutManager(new LinearLayoutManager(context));
                                bd.friendrecyclerView.setAdapter(new FriendAdapter(context,userdata, (HomeActivity) getActivity()));

                            }
                        });
                    }
                    Log.d("TAG", "onSuccess: "+userdata.size());

                }
                else{
                    bd.nofriendalert.setVisibility(View.VISIBLE);
                }
            }
        });

        db.collection("friendrequest")
                .whereEqualTo("to", preferences.getString("email", ""))
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                        if (documents.size() != 0) {
                            bd.requestfriend.setText("이웃 요청(" + documents.size() + ")");
                        }
                    }
                });
        db.collection("friendrequest")
                .whereEqualTo("to", preferences.getString("email", ""))
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                        if (documents.size() != 0) {
                            Log.d("TAG", "onSuccess: " + documents.size());
                            while (cnt++ != documents.size()) {
                                Log.d("TAG", "onSuccess" + documents.get(0));
                                db.collection("user").whereEqualTo("email", documents.get(0).get("from")).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        Log.d("TAG", "onComplete: " + task.getResult().size());
                                        if (task.isSuccessful() && task.getResult() != null) {
                                            for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                                String docname = queryDocumentSnapshot.getId();
                                                Log.d("TAG", "onComplete: " + docname);
                                                list.add(new friendrequestlist((String) queryDocumentSnapshot.get("nickname"), (String) queryDocumentSnapshot.get("email"), (String) queryDocumentSnapshot.get("profile"), docname));
                                            }
                                        }
                                    }
                                });
                            }
                            cnt=0;
                        }
                    }
                });

        fragmentManager = getActivity().getSupportFragmentManager();

        bd.requestfriend.setOnClickListener(vv->{
            BottomDialogAdapter adapter = new BottomDialogAdapter(getActivity(), preferences.getString("email", ""), preferences.getString("uid", ""),fragmentManager );
            BottomDialogFragment bottomDialogFragment = new BottomDialogFragment(adapter);
            adapter.setItem(list);
            bottomDialogFragment.show(getActivity().getSupportFragmentManager(), "");
        });

        bd.addfriend.setOnClickListener(v -> {
            addemail = new EditText(getActivity());
            addemail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            add = new AlertDialog.Builder(getActivity())
                    .setTitle("친구의 이메일을 입력해주세요!")
                    .setView(addemail)
                    .setPositiveButton("친구 요청 보내기", dialogListener)
                    .setNegativeButton("취소", null)
                    .show();
        });
        // Inflate the layout for this fragment
        return bd.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("TAG", "onPause: ");
    }
}