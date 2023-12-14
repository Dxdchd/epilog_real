package com.example.epilog;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import android.app.AlertDialog;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.target.Target;
import com.example.epilog.Fragment.friendslist;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Firebase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<User> users;
    private HomeActivity Activity;

    public FriendAdapter(Context context, ArrayList<User> users,HomeActivity Activity) {
        Log.d("TAG", "FriendAdapter: "+users.size());
        this.context = context;
        this.users = users;
        this.Activity = Activity;
    }

    @NonNull
    @Override
    public FriendAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.friendlist, parent, false);
        Log.d("TAG", "onCreateViewHolder: ");
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Log.d("tt", "onBindViewHolder: " + users.get(position));
        holder.bind(users.get(position));
    }

    @Override
    public int getItemCount() {

        Log.d("TAG", "getItemCount: "+users.size());
        return users.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView mainfriendprofile;
        private TextView mainnickname;
        private TextView mainemail;
        private ImageView usercalander;
        private ImageView deletefriend;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mainfriendprofile = itemView.findViewById(R.id.mainfriendprofile);
            mainnickname = itemView.findViewById(R.id.mainnickname);
            mainemail = itemView.findViewById(R.id.mainemail);
            usercalander = itemView.findViewById(R.id.usercalander);
            deletefriend = itemView.findViewById(R.id.deletefriend);
        }

        public void bind(User item) {
            mainnickname.setText(item.getNickname());
            mainemail.setText(item.getEmail());
            if(item.getProfile()!=null){
                FirebaseStorage storage = FirebaseStorage.getInstance();
                Log.d("test", "bindSliderImage: " + storage);
                StorageReference path = storage.getReferenceFromUrl(item.getProfile());
                Log.d("test", "bindSliderImage: " + path);
                path.getDownloadUrl().addOnSuccessListener(uri -> {
                    Glide.with(context)
                            .load(uri)
                            .into(mainfriendprofile);
                });
            }
            deletefriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder deleteFriend = new AlertDialog.Builder(context)
                            .setTitle(item.getNickname()+"님과 친구를 끊으시겠어요?")
                            .setPositiveButton("친구 삭제", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                    SharedPreferences preferences = Activity.getSharedPreferences("UserInfo", MODE_PRIVATE);
                                    db.collection("user").document(preferences.getString("uid","")).get()
                                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                @Override
                                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                    if(documentSnapshot.exists()){
                                                        ArrayList<String> friendslist = (ArrayList<String>) documentSnapshot.get("friends");
                                                        friendslist.remove(item.getUid());
                                                        Map<String, Object> data = new HashMap<>();
                                                        data.put("friends", friendslist);
                                                        db.collection("user").document(preferences.getString("uid","")).update(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                db.collection("user").document(item.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                    @Override
                                                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                        if(documentSnapshot.exists()){
                                                                            ArrayList<String> friendslist = (ArrayList<String>) documentSnapshot.get("friends");
                                                                            friendslist.remove(preferences.getString("uid",""));
                                                                            Map<String, Object> data = new HashMap<>();
                                                                            data.put("friends", friendslist);
                                                                            db.collection("user").document(item.getUid()).update(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                @Override
                                                                                public void onSuccess(Void unused) {
                                                                                    Toast.makeText(context,item.getNickname()+"님을 삭제했습니다.",Toast.LENGTH_SHORT).show();
                                                                                    FragmentManager newmanager = Activity.getSupportFragmentManager();
                                                                                    FragmentTransaction ft = newmanager.beginTransaction();
                                                                                    ft.replace(R.id.main_layout, new friendslist()).commit();
                                                                                    Log.d("TAG", "onSuccess: 친구삭제 완전완료");
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
                                }
                            })
                            .setNegativeButton("취소",null);
                    deleteFriend.show();
                }
            });
            usercalander.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,FriendsCalandar.class);
                    intent.putExtra("nickname",item.getNickname());
                    intent.putExtra("uid",item.getUid());
                    context.startActivity(intent);
                }
            });
//            FirebaseStorage storage = FirebaseStorage.getInstance();
//            Log.d("test", "bindSliderImage: " + storage);
//            StorageReference path = storage.getReferenceFromUrl(imageURL);
//            Log.d("test", "bindSliderImage: " + path);
//            path.getDownloadUrl().addOnSuccessListener(uri -> {
//                Glide.with(context)
//                        .load(uri)
//                        .into(mImageView);
//            });
        }
    }
}
