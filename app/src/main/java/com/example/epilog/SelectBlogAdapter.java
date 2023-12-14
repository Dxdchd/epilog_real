package com.example.epilog;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SelectBlogAdapter extends RecyclerView.Adapter<SelectBlogAdapter.ViewHolder> {

    private Context context;
    public static List<FromDb> blogRecyclerLists;
    private List<String> documentID = new ArrayList<>();
    private List<ViewHolder> holders = new ArrayList<>();

    public SelectBlogAdapter(List<FromDb> blogRecyclerLists) {
        this.blogRecyclerLists = blogRecyclerLists;
    }
    @NonNull
    @Override
    public SelectBlogAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.booklistitem, parent, false);
        return new SelectBlogAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectBlogAdapter.ViewHolder holder, int position) {
        holders.add(position, holder);
        holder.date.setText(blogRecyclerLists.get(position).getDate());
        holder.title.setText(blogRecyclerLists.get(position).getTitle());
        holder.url = blogRecyclerLists.get(position).getImgUrls().get(0);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        Log.d("test", "bindSliderImage: " + storage);
        StorageReference path = storage.getReferenceFromUrl(holder.url);
        Log.d("test", "bindSliderImage: " + path);
        context = holder.itemView.getContext();
        path.getDownloadUrl().addOnSuccessListener(uri -> {
            Glide.with(context)
                    .load(uri)
                    .centerCrop()
                    .into(holder.imageView);
        });

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    for(int i = 0; i < holders.size(); i++){
                        if(i != position){
                            holders.get(i).checkBox.setChecked(false);
                        }
                    }
                    SelectBlogToBook.paths.clear();
                    SelectBlogToBook.paths.add(blogRecyclerLists.get(position).getPath());
                }
                else{
                    SelectBlogToBook.paths.remove(blogRecyclerLists.get(position).getPath());

                }
            }
        });
        holder.view.setOnClickListener(v->{
            if(holder.checkBox.isChecked()){
                holder.checkBox.setChecked(false);
            }
            else{
                holder.checkBox.setChecked(true);
            }
        });
    }

    @Override
    public int getItemCount() {
        return blogRecyclerLists.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView date;
        private TextView title;
        private String url;
        private ImageView imageView;
        private CheckBox checkBox;
        private View view;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            date = itemView.findViewById(R.id.booklistdate);
            title = itemView.findViewById(R.id.booklisttitle);
            imageView = itemView.findViewById(R.id.booklistimage);
            checkBox = itemView.findViewById(R.id.blogchk);
        }
    }
}
