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
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    private Context context;
    private ArrayList<FromDb> blogRecyclerLists;
    private OnItemClickListener mListener = null ;

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener ;
    }
    public ListAdapter(ArrayList<FromDb> blogRecyclerLists) {
        Log.d("TAG", "ListAdapter: 셍상");
        this.blogRecyclerLists = blogRecyclerLists;
    }

    @NonNull
    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.blog_recycler, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ListAdapter.ViewHolder holder, int position) {
        Log.d("TAG", "onBindViewHolder: 생상");
        holder.date.setText(blogRecyclerLists.get(position).getDate());
        holder.title.setText(blogRecyclerLists.get(position).getTitle());
        holder.location.setText(blogRecyclerLists.get(position).getLocation());
        holder.feel = blogRecyclerLists.get(position).getEmotion();
        holder.weather = blogRecyclerLists.get(position).getWeather();
        holder.mainimg = blogRecyclerLists.get(position).getImgUrls().get(0);
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView date;
        private TextView title;
        private String mainimg;
        private TextView location;
        private int feel;
        private int weather;
        ImageView mainimgview;
        ImageView feelimg;
        ImageView weatherimg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.recyclerdate);
            title = itemView.findViewById(R.id.recyclertitle);
            title.setSelected(true);
            location = itemView.findViewById(R.id.loca);
            location.setSelected(true);
            feelimg = itemView.findViewById(R.id.feel);
            weatherimg = itemView.findViewById(R.id.weather);
            mainimgview = itemView.findViewById(R.id.recyclerimg);

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
                            mListener.onItemClick(v, pos) ;
                        }
                    }
                }
            });
        }
    }
}
