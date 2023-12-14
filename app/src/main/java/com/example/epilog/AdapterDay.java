package com.example.epilog;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AdapterDay extends RecyclerView.Adapter<AdapterDay.DayView> {
    private final int ROW = 6;
    private Context context2;
    private Context context;
    private final int tempMonth;
    private final List<Date> dayList;
    private ArrayList<FromDb> fromDbs;
    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");


    public AdapterDay(int tempMonth, List<Date> dayList, View view) {
        this.tempMonth = tempMonth;
        this.dayList = dayList;
        this.context2 = view.getContext();
    }

    public class DayView extends RecyclerView.ViewHolder {
        public View layout;
        public TextView textView;
        public ImageView imageView;
        private String mainimg;

        public DayView(View layout) {
            super(layout);
            this.layout = layout;
            this.textView = layout.findViewById(R.id.item_day_text);
            this.imageView = layout.findViewById(R.id.backimg);
            this.imageView.setClipToOutline(true);

        }
    }

    @Override
    public DayView onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.calanderday, parent, false);
        return new DayView(view);
    }

    @Override
    public void onBindViewHolder(DayView holder, int position) {
        DocumentReference documentReference = db.collection("diary").document(currentUser.getUid() + "_" + sdf.format(dayList.get(position)));
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    Log.d("task", "onComplete: "+task);
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("document", "onComplete: "+document);
                        FromDb fromDb = document.toObject(FromDb.class);
                        holder.mainimg = fromDb.getImgUrls().get(0);
                        context = holder.itemView.getContext();
                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        Log.d("test", "bindSliderImage: " + storage);
                        StorageReference path = storage.getReferenceFromUrl(holder.mainimg);
                        Log.d("test", "bindSliderImage: " + path);
                        path.getDownloadUrl().addOnSuccessListener(uri -> {
                            Glide.with(context)
                                    .load(uri)
                                    .centerCrop()
                                    .into(holder.imageView);
                        });
                    }
                    else{
                        Log.d("documentnull", "onComplete: ");
                    }
                }
            }
        });
        holder.layout.findViewById(R.id.item_day_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference documentReference = db.collection("diary").document(currentUser.getUid() + "_" + sdf.format(dayList.get(position)));
                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                FromDb fromDb = document.toObject(FromDb.class);
                                Intent intent = new Intent(context2, Basic_BlogView.class);
                                intent.putExtra("uid", fromDb.getUid());
                                intent.putExtra("date", fromDb.getTimestamp());
                                context2.startActivity(intent);
                            }
                            else{
                                Log.d("TAG", "onComplete: "+dayList.get(position));
                                Intent intent = new Intent(context2, makeblog.class);
                                intent.putExtra("date",new SimpleDateFormat("yyyy MM dd").format(dayList.get(position)));
                                context2.startActivity(intent);
                            }
                        }
                    }
                });
            }
        });
        holder.textView.setText(String.valueOf(dayList.get(position).getDate()));
        holder.textView.setTextColor((position % 7 == 0) ? Color.RED : ((position % 7 == 6) ? Color.BLUE : Color.BLACK));
        if (tempMonth != dayList.get(position).getMonth()) {
            holder.layout.findViewById(R.id.backimg).setAlpha(0.0f);
            holder.layout.findViewById(R.id.backimg).setAlpha(0.5f);
            holder.layout.findViewById(R.id.item_day_text).setAlpha(0.4f);
        }
    }

    @Override
    public int getItemCount() {
        return ROW * 7;
    }
}
