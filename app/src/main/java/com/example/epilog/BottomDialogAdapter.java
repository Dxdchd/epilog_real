package com.example.epilog;

import static android.graphics.BlendMode.COLOR;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.epilog.Fragment.friendslist;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class BottomDialogAdapter extends RecyclerView.Adapter<BottomDialogAdapter.Holder> {
    private List<friendrequestlist> itemList = new ArrayList<>();
    private Context context;
    private FragmentManager fragmentManager;
    private String myemail;
    private String myuid;
    private String fromuid;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public BottomDialogAdapter(Context context, String myemail, String myuid, FragmentManager fragmentManager) {
        this.context = context;
        this.myemail = myemail;
        this.myuid = myuid;
        this.fragmentManager = fragmentManager;
    }


    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friendrequestrecycler, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        friendrequestlist item = itemList.get(position);
        holder.bind(item);
        TextView notice = holder.view.findViewById(R.id.noticeAcceptOrReject);
        holder.view.findViewById(R.id.reject).setOnClickListener(v -> {
            db.collection("friendrequest").whereEqualTo("to", myemail).whereEqualTo("from", item.email).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        Log.d("TAG", "onComplete: " + task.getResult().size());
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            document.getReference().delete();
                            break;
                        }
                    }
                }
            });
            com.example.epilog.Fragment.friendslist friendslist1 = new friendslist();
            fragmentManager.beginTransaction().replace(R.id.main_layout, friendslist1).commit();
            holder.view.findViewById(R.id.reject).setVisibility(View.INVISIBLE);
            holder.view.findViewById(R.id.accept).setVisibility(View.INVISIBLE);
            notice.setText("거절됨");
            notice.setVisibility(View.VISIBLE);
            notice.setTextColor(Color.RED);

        });
        holder.view.findViewById(R.id.accept).setOnClickListener(vv -> {
            db.collection("friendrequest").whereEqualTo("to", myemail).whereEqualTo("from", item.email).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            document.getReference().delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override//요청삭제
                                public void onSuccess(Void unused) {
                                    Log.d("TAG", "onSuccess: ");
                                    db.collection("user").whereEqualTo("email", item.email).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                QuerySnapshot querySnapshot = task.getResult();

                                                // 일치하는 문서가 있는 경우
                                                if (querySnapshot.size() > 0) {
                                                    // 가져온 문서의 이름 출력
                                                    for (QueryDocumentSnapshot document : querySnapshot) {
                                                        String documentId = document.getId();
                                                        db.collection("user").document(documentId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                            @Override
                                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                if (documentSnapshot.exists()) {
                                                                    //남의 친구에 나 추가
                                                                    ArrayList<String> friendslist = (ArrayList<String>) documentSnapshot.get("friends");
                                                                    friendslist.add(myuid);
                                                                    Map<String, Object> data = new HashMap<>();
                                                                    data.put("friends", friendslist);
                                                                    db.collection("user").document(documentId).update(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void unused) {
                                                                            db.collection("user").document(myuid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                                @Override
                                                                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                                    //내 친구에 남 추가
                                                                                    if (documentSnapshot.exists()) {
                                                                                        ArrayList<String> friendslist = (ArrayList<String>) documentSnapshot.get("friends");
                                                                                        friendslist.add(documentId);
                                                                                        Map<String, Object> data = new HashMap<>();
                                                                                        data.put("friends", friendslist);
                                                                                        db.collection("user").document(myuid).update(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                            @Override
                                                                                            public void onSuccess(Void unused) {
                                                                                                com.example.epilog.Fragment.friendslist friendslist1 = new friendslist();
                                                                                                fragmentManager.beginTransaction().replace(R.id.main_layout, friendslist1).commit();
                                                                                                Log.d("TAG", "onSuccess: " + "친구추가 완전완료");
                                                                                            }
                                                                                        });
                                                                                    }
                                                                                }
                                                                            });
                                                                        }
                                                                    });
                                                                }
                                                            }
                                                        });
                                                        // 또는 원하는 처리를 수행할 수 있음
                                                    }
                                                } else {
                                                    // 일치하는 문서가 없음
                                                }
                                            }
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("TAG", "onFailure: ");
                                }
                            });
                            break;
                        }
                    }
                }
            });
            holder.view.findViewById(R.id.accept).setVisibility(View.INVISIBLE);
            holder.view.findViewById(R.id.reject).setVisibility(View.INVISIBLE);
            notice.setText("수락됨");
            notice.setVisibility(View.VISIBLE);
            notice.setTextColor(Color.GREEN);
//            FragmentManager newmanager = getActivity().getSupportFragmentManager();
//            FragmentTransaction ft = newmanager.beginTransaction();
//            ft.add(R.id.main_layout, new friendslist()).commitAllowingStateLoss();
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void setItem(List<friendrequestlist> items) {
        if (items != null && !items.isEmpty()) {
            itemList = items;
            notifyDataSetChanged();
        }
    }


    public class Holder extends RecyclerView.ViewHolder {
        private View view;

        public Holder(View itemView) {
            super(itemView);
            view = itemView;
        }

        public void bind(friendrequestlist item) {
            Log.d("TAG", "bind: " + item.uid + item.email + item.nickname);
            TextView nickname = view.findViewById(R.id.nickname);
            nickname.setText(item.nickname);
            TextView email = view.findViewById(R.id.email);
            email.setText(item.email);
            CircleImageView circleImageView = view.findViewById(R.id.friendprofile);
            if (item.profile != null) {
                FirebaseStorage storage = FirebaseStorage.getInstance();
                Log.d("test", "bindSliderImage: " + storage);
                StorageReference path = storage.getReferenceFromUrl(item.profile);
                Log.d("test", "bindSliderImage: " + path);
                path.getDownloadUrl().addOnSuccessListener(uri -> {
                    Glide.with(context)
                            .load(uri)
                            .into(circleImageView);
                });
            }
        }
    }
}