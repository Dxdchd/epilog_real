package com.example.epilog;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.Firebase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ImageSliderFromDb extends RecyclerView.Adapter<ImageSliderFromDb.MyViewHolder> {
    private Context context;
    private ArrayList<String> sliderImage;
    public ImageSliderFromDb(Context context, ArrayList<String> sliderImage) {
        this.context = context;
        this.sliderImage = sliderImage;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.imageslider, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Log.d("tt", "onBindViewHolder: "+sliderImage.get(position));
        holder.bindSliderImage(sliderImage.get(position));
    }

    @Override
    public int getItemCount() {
        return sliderImage.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView mImageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imgslider);
        }

        public void bindSliderImage(String imageURL) {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            Log.d("test", "bindSliderImage: "+storage);
            StorageReference path = storage.getReferenceFromUrl(imageURL);
            Log.d("test", "bindSliderImage: "+path);
            path.getDownloadUrl().addOnSuccessListener(uri->{
                Glide.with(context)
                        .load(uri)
                        .into(mImageView);
            });
        }
    }
}
