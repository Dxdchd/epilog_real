package com.example.epilog;

// BottomSheetFragment.java

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.checkerframework.checker.units.qual.K;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;

public class BottomSheetComments extends BottomSheetDialogFragment {

    private RecyclerView recyclerView;
    private CircleImageView imageView;
    private String diaryId;
    private List<Map<String, String>> comments = new ArrayList<>();

    public BottomSheetComments(String diaryId) {
        this.diaryId = diaryId;
        getDiary();
        // Required empty public constructor
    }

    private void getDiary() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.collection("diary").document(this.diaryId);
        documentReference.get().addOnSuccessListener(documentSnapshot -> {
            Log.d("TAG", "onSuccess: " + documentSnapshot);
            FromDb fromDb = documentSnapshot.toObject(FromDb.class);
            this.comments = fromDb.getComments();
            Log.d("TAG", fromDb.getComments().toString()+comments.size());
            YourAdapter adapter = new YourAdapter(); // Replace with your adapter class

            // Set up RecyclerView with the adapter
            recyclerView.setAdapter(adapter);

            // Set the layout manager
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        });
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if (getActivity() instanceof OnCommentDismissListener) {
            ((OnCommentDismissListener) getActivity()).onCommentDismiss(/*new comment count*/);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.commentslayout, container, false);

        SharedPreferences preferences = getContext().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);

        recyclerView = view.findViewById(R.id.commentRecycler);
        imageView = view.findViewById(R.id.myProfile);
        Log.d("test", "comment bottom sheet: " + diaryId);

        FirebaseFirestore.getInstance().collection("user").document(preferences.getString("uid", "")).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String url = (String) documentSnapshot.get("profile");
                if (url != null) {
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    Log.d("test", "bindSliderImage: " + storage);
                    StorageReference path = storage.getReferenceFromUrl(url);
                    Log.d("test", "bindSliderImage: " + path);
                    path.getDownloadUrl().addOnSuccessListener(uri -> {
                        Glide.with(view)
                                .load(uri)
                                .into(imageView);
                    });
                }
            }
        });

        // Initialize the adapter
        YourAdapter adapter = new YourAdapter(); // Replace with your adapter class

        // Set up RecyclerView with the adapter
        recyclerView.setAdapter(adapter);

        // Set the layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        view.findViewById(R.id.putCommnet).setOnClickListener(view1 -> {
            EditText editText = view.findViewById(R.id.comment);
            String content = editText.getText().toString();
            List<Map<String, String>> comments = this.comments;
            Map<String, String> comment = new HashMap<>();
            comment.put("uid", preferences.getString("uid", ""));
            comment.put("content", content);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+9"));
            comment.put("now", sdf.format(new Date()));
            comments.add(comment);
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference documentReference = db.collection("diary").document(this.diaryId);
            Map<String, Object> data = new HashMap<>();
            data.put("comments", comments);
            documentReference.update(data);
            editText.setText("");
            // Set up RecyclerView with the adapter
            recyclerView.setAdapter(adapter);
            // Set the layout manager
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            Toast.makeText(getContext(), "댓글을 작성했어요.", Toast.LENGTH_SHORT).show();
        });

        return view;
    }

    // Inner class for the adapter
    private class YourAdapter extends RecyclerView.Adapter<YourAdapter.ViewHolder> {

        // Your data source (replace with your data structure)


        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comments, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//            holder.te
            //holder.textView.setText(dataSet[position]);
            holder.textView.setText(comments.get(position).get("content"));
            holder.time.setText(convertToTimeAgo(comments.get(position).get("now")));
            FirebaseFirestore.getInstance().collection("user").document(comments.get(position).get("uid")).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    holder.name.setText(documentSnapshot.get("nickname").toString());
                    String url = (String) documentSnapshot.get("profile");
                    if(url!=null){
                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        Log.d("test", "bindSliderImage: " + storage);
                        StorageReference path = storage.getReferenceFromUrl(url);
                        Log.d("test", "bindSliderImage: " + path);
                        path.getDownloadUrl().addOnSuccessListener(uri -> {
                            Glide.with(getContext())
                                    .load(uri)
                                    .into(holder.circleImageView);
                        });
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            Log.d("TAG", "getItemCount: " + comments.size());
            return comments.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView textView;
            TextView name;
            TextView time;
            CircleImageView circleImageView;

            ViewHolder(View view) {
                super(view);
                time = view.findViewById(R.id.commentTime);
                name = view.findViewById(R.id.mainnickname);
                textView = view.findViewById(R.id.commentContent); // Replace with your item layout's TextView ID
                circleImageView = view.findViewById(R.id.commentfriendprofile);
            }
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
        return currentTimeUTC;
    }
}


