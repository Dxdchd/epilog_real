package com.example.epilog;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class friendListAdapter extends RecyclerView.Adapter<friendListAdapter.FriendHolder> {
    private Context context;
    private ArrayList<FromDb> blogRecyclerLists;
    private OnItemClickListener mListener = null;

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public friendListAdapter(ArrayList<FromDb> blogRecyclerLists) {
        Log.d("TAG", "ListAdapter: 셍상");
        this.blogRecyclerLists = blogRecyclerLists;
    }

    @NonNull
    @Override
    public friendListAdapter.FriendHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.friendblogrecycler, parent, false);
        return new FriendHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull friendListAdapter.FriendHolder holder, int position) {
        Log.d("TAG", "onBindViewHolder: 생상");
        holder.date.setText(blogRecyclerLists.get(position).getDate());
        holder.title.setText(blogRecyclerLists.get(position).getTitle());
        holder.location.setText(blogRecyclerLists.get(position).getLocation());
        holder.feel = blogRecyclerLists.get(position).getEmotion();
        holder.weather = blogRecyclerLists.get(position).getWeather();
        holder.mainimg = blogRecyclerLists.get(position).getImgUrls().get(0);
        holder.time.setText(convertToTimeAgo(blogRecyclerLists.get(position).getNow()));
        FirebaseFirestore.getInstance().collection("user").document(blogRecyclerLists.get(position).getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String name = (String) documentSnapshot.get("nickname");
                holder.name.setText(name);
            }
        });
        context = holder.itemView.getContext();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        Log.d("test", "bindSliderImage: " + storage);
        StorageReference path = storage.getReferenceFromUrl(holder.mainimg);
        Log.d("test", "bindSliderImage: " + path);
        path.getDownloadUrl().addOnSuccessListener(uri -> {
            Glide.with(context)
                    .load(uri)
                    .into(holder.mainimgview);
        });
        switch (holder.feel) {
            case 0:
                holder.feelimg.setImageResource(R.drawable.laugh);
                break;
            case 1:
                holder.feelimg.setImageResource(R.drawable.mad);
                break;
            case 2:
                holder.feelimg.setImageResource(R.drawable.smile);
                break;
            case 3:
                holder.feelimg.setImageResource(R.drawable.sad);
                break;
            default:
                holder.feelimg.setImageResource(R.drawable.soso);
                break;
        }
        switch (holder.weather) {
            case 0:
                holder.weatherimg.setImageResource(R.drawable.rainy);
                break;
            case 1:
                holder.weatherimg.setImageResource(R.drawable.sunny);
                break;
            case 2:
                holder.weatherimg.setImageResource(R.drawable.cloudy);
                break;
            case 3:
                holder.weatherimg.setImageResource(R.drawable.windy);
                break;
            default:
                holder.weatherimg.setImageResource(R.drawable.snowy);
                break;

        }
    }

    @Override
    public int getItemCount() {
        return blogRecyclerLists.size();
    }

    public class FriendHolder extends RecyclerView.ViewHolder {
        private TextView date;
        private TextView title;
        private String mainimg;
        private TextView location;
        private TextView name;
        private TextView time;
        private int feel;
        private int weather;
        ImageView mainimgview;
        ImageView feelimg;
        ImageView weatherimg;

        public FriendHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.time);
            date = itemView.findViewById(R.id.recyclerdate);
            title = itemView.findViewById(R.id.recyclertitle);
            title.setSelected(true);
            location = itemView.findViewById(R.id.loca);
            location.setSelected(true);
            feelimg = itemView.findViewById(R.id.feel);
            weatherimg = itemView.findViewById(R.id.weather);
            mainimgview = itemView.findViewById(R.id.recyclerimg);
            name = itemView.findViewById(R.id.friendnickname);
//            FirebaseStorage storage = FirebaseStorage.getInstance();
//            Log.d("test", "bindSliderImage: "+storage);
//            StorageReference path = storage.getReferenceFromUrl(mainimg);
//            Log.d("test", "bindSliderImage: "+path);
//            path.getDownloadUrl().addOnSuccessListener(uri->{
//                Glide.with(context)
//                        .load(uri)
//                        .into(mainimgview);
//            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        if (mListener != null) {
                            mListener.onItemClick(v, pos);
                        }
                    }
                }
            });
        }
    }

    public static String convertToTimeAgo(String firebaseDateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());

        try {
            Date date = sdf.parse(firebaseDateString);
            long timeInMillis = date.getTime();
            long currentTimeMillis = getCurrentTimeInLocal();
            long diff = currentTimeMillis - timeInMillis;
            Log.d("TAG", "convertToTimeAgo: " + diff + date + timeInMillis);
            Log.d("TAG", "convertToTimeAgo: " + currentTimeMillis);

            if (diff < 60 * 1000) {
                return "(방금 전)";
            } else if (diff < 60 * 60 * 1000) {
                long minutes = diff / (60 * 1000);
                return "(" + minutes + "분 전)";
            } else if (diff < 24 * 60 * 60 * 1000) {
                long hours = diff / (60 * 60 * 1000);
                return "(" + hours + "시간 전)";
            } else if (diff < 7 * 24 * 60 * 60 * 1000) {
                long days = diff / (24 * 60 * 60 * 1000);
                return "(" + days + "일 전)";
            } else {
                return "오래 전";
            }
        } catch (ParseException e) {
            e.printStackTrace();
            // 날짜 변환에 실패한 경우, 원래의 문자열을 반환하거나 에러 처리를 할 수 있습니다.
            return firebaseDateString;
        }
    }

    public static long getCurrentTimeInLocal() {
        // 현재 시간을 UTC로 얻음
        long currentTimeUTC = System.currentTimeMillis();

        // 현재 로컬 시간대를 얻음
        TimeZone localTimeZone = TimeZone.getTimeZone("GMT+9");

        // 로컬 시간대로 변환
        int offset = localTimeZone.getRawOffset();
        return currentTimeUTC ;
    }
}
