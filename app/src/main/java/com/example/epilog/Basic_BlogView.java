package com.example.epilog;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.epilog.databinding.ActivityBasicBlogViewBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Basic_BlogView extends AppCompatActivity implements OnCommentDismissListener {
    ActivityBasicBlogViewBinding bd;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    String doc;
    int cnt = 0;
    SharedPreferences preferences;
    String useruid;
    AlertDialog alertDialog;
    private FromDb fromDb;
    BottomSheetComments bottomSheetComments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd = ActivityBasicBlogViewBinding.inflate(getLayoutInflater());
        preferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
        useruid = preferences.getString("uid", "");
        setContentView(bd.getRoot());
        preferences.getString("uid", "");
        Intent intent = getIntent();
        doc = intent.getStringExtra("uid") + "_" + intent.getStringExtra("date");
        Log.d("test", "onCreate: " + doc);
        DocumentReference documentReference = db.collection("diary").document(doc);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("TAG", "onSuccess: " + documentSnapshot);
                fromDb = documentSnapshot.toObject(FromDb.class);
                if (useruid.equals(fromDb.getUid())) {
                    bd.toptitle.setText("나의 일기");
                    bd.deleteblog.setVisibility(View.VISIBLE);
                } else {
                    db.collection("user").document(fromDb.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            bd.toptitle.setText(documentSnapshot.get("nickname") + "님의 일기");
                        }
                    });
                }
                if (fromDb.getLikes().contains(useruid)) {
                    bd.heart.setImageResource(R.drawable.filledheart);
                }
                bd.datebtn.setText(fromDb.getDate());
                bd.title.setText(fromDb.getTitle());
                bd.locationbtn.setText(fromDb.getLocation());
                bd.content.setText(fromDb.getContent());
                bd.likecnt.setText(String.valueOf(fromDb.getLikes().size()));
                bd.commentcnt.setText(String.valueOf(fromDb.getComments().size()));
                switch (fromDb.getEmotion()) {
                    case 0:
                        bd.feel.setImageResource(R.drawable.laugh);
                        break;
                    case 1:
                        bd.feel.setImageResource(R.drawable.mad);
                        break;
                    case 2:
                        bd.feel.setImageResource(R.drawable.smile);
                        break;
                    case 3:
                        bd.feel.setImageResource(R.drawable.sad);
                        break;
                    default:
                        bd.feel.setImageResource(R.drawable.soso);
                        break;
                }
                switch (fromDb.getWeather()) {
                    case 0:
                        bd.weather.setImageResource(R.drawable.rainy);
                        break;
                    case 1:
                        bd.weather.setImageResource(R.drawable.sunny);
                        break;
                    case 2:
                        bd.weather.setImageResource(R.drawable.cloudy);
                        break;
                    case 3:
                        bd.weather.setImageResource(R.drawable.windy);
                        break;
                    default:
                        bd.weather.setImageResource(R.drawable.snowy);
                        break;
                }
                bd.viewpager.setOffscreenPageLimit(1);
                bd.viewpager.setAdapter(new ImageSliderFromDb(getApplicationContext(), fromDb.getImgUrls()));
                bd.viewpager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                    @Override
                    public void onPageSelected(int position) {
                        super.onPageSelected(position);
                        setCurrentIndicator(position);
                    }
                });
                setupIndicators(fromDb.getImgUrls().size());
            }
        });
        bd.heart.setOnClickListener(vvv -> {
            documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    List<String> likes = (List<String>) documentSnapshot.get("likes");
                    if (!likes.contains(useruid)) {
                        likes.add(useruid);
                        Map<String, Object> data = new HashMap<>();
                        data.put("likes", likes);
                        documentReference.update(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                bd.heart.setImageResource(R.drawable.filledheart);
                                bd.likecnt.setText(likes.size() + "");
                            }
                        });
                    } else {
                        likes.remove(useruid);
                        Map<String, Object> data = new HashMap<>();
                        data.put("likes", likes);
                        documentReference.update(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                bd.heart.setImageResource(R.drawable.nonfillheart);
                                bd.likecnt.setText(likes.size() +"");
                            }
                        });
                    }
                }
            });
        });
        bd.comment.setOnClickListener(vvvv -> {
            bottomSheetComments = new BottomSheetComments(doc);
            bottomSheetComments.show(getSupportFragmentManager(), "");
        });


        bd.blogback.setOnClickListener(v -> {
            finish();
        });
        bd.deleteblog.setOnClickListener(vv -> {
            AlertDialog.Builder dlg = new AlertDialog.Builder(this)
                    .setTitle("정말 일기를 삭제하시겠어요?")
                    .setNegativeButton("취소", null)
                    .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            db.collection("diary").document(doc).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    finish();
                                    Toast.makeText(getApplicationContext(), "일기가 삭제되었어요.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
            dlg.show();
        });
    }

    private void setupIndicators(int count) {
        if (cnt > 0) {
            bd.indicator.removeAllViews();
        }
        cnt = count;
        ImageView[] indicators = new ImageView[count];
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(16, 8, 16, 8);
        for (int i = 0; i < indicators.length; i++) {
            indicators[i] = new ImageView(this);
            indicators[i].setImageDrawable(ContextCompat.getDrawable(this,
                    R.drawable.bg_indicator_inactive));
            indicators[i].setLayoutParams(params);
            bd.indicator.addView(indicators[i]);
        }
        setCurrentIndicator(0);
    }

    private void setCurrentIndicator(int position) {
        int childCount = cnt;
        for (int i = 0; i < childCount; i++) {
            ImageView imageView = (ImageView) bd.indicator.getChildAt(i);
            if (i == position) {
                imageView.setImageDrawable(ContextCompat.getDrawable(
                        this,
                        R.drawable.bg_indicator_active
                ));
            } else {
                imageView.setImageDrawable(ContextCompat.getDrawable(
                        this,
                        R.drawable.bg_indicator_inactive
                ));
            }
        }
    }

    @Override
    public void onCommentDismiss() {
        DocumentReference documentReference = db.collection("diary").document(doc);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                fromDb = documentSnapshot.toObject(FromDb.class);
                bd.commentcnt.setText(fromDb.getComments().size()+"");
            }
        });
    }
}