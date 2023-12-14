package com.example.epilog;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;

import android.app.AlertDialog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.KeyEvent;
import android.widget.Toast;

import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.epilog.databinding.ActivityMakeblogBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.skydoves.balloon.ArrowOrientation;
import com.skydoves.balloon.ArrowPositionRules;
import com.skydoves.balloon.Balloon;
import com.skydoves.balloon.BalloonAnimation;
import com.skydoves.balloon.BalloonSizeSpec;

import org.checkerframework.checker.units.qual.A;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;

import me.relex.circleindicator.CircleIndicator3;


public class makeblog extends AppCompatActivity {
    int cnt = 0;
    private static final String TAG = "MultiImageActivity";
    ArrayList<Bitmap> BitmapList;     // 이미지의 bitmap 담을 ArrayList 객체
    ArrayList<String> firestoreurl = new ArrayList<>();
    ArrayList<Uri> uriArrayList;
    private EditText loc;
    private ActivityMakeblogBinding binding;
    private AlertDialog back;
    private AlertDialog locdialog;
    private FirebaseFirestore db;
    private int defFeel = 0;
    private int defWeather = 0;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    private SharedPreferences preferences;
    private String onlydate;
    Calendar calendar = Calendar.getInstance();
    int y = calendar.get(Calendar.YEAR);
    int m = calendar.get(Calendar.MONTH);
    int d = calendar.get(Calendar.DAY_OF_MONTH);

    // Function to get the day of the week as a string
    private String getDayOfWeek(Calendar calendar) {
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        if(dayOfWeek == 1) return "(일)";
        if(dayOfWeek == 2) return "(월)";
        if(dayOfWeek == 3) return "(화)";
        if(dayOfWeek == 4) return "(수)";
        if(dayOfWeek == 5) return "(목)";
        if(dayOfWeek == 6) return "(금)";
        if(dayOfWeek == 7) return "(토)";

        return "";

        // Format the date to get the day of the week
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
//        return simpleDateFormat.format(calendar.getTime());
    }

    private DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (dialog == back && which == DialogInterface.BUTTON_POSITIVE) {
                finish();
            } else if (dialog == locdialog && which == DialogInterface.BUTTON_POSITIVE) {
                binding.locationbtn.setText(loc.getText().toString());
            }
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            back = new AlertDialog.Builder(this)
                    .setTitle("아직 일기를 작성하지 않았어요!")
                    .setMessage("지금 나가면 일기가 저장되지 않아요.")
                    .setPositiveButton("나가기", dialogListener)
                    .setNegativeButton("돌아가기", null)
                    .create();
            back.show();
            return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMakeblogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Log.d(TAG, "onCreate: ");
        Balloon balloondate = new Balloon.Builder(this)
                .setText("클릭하여 날짜를 변경하세요.")
                .setArrowSize(10)
                .setArrowOrientation(ArrowOrientation.BOTTOM)
                .setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
                .setWidth(BalloonSizeSpec.WRAP)
                .setHeight(BalloonSizeSpec.WRAP)
                .setTextSize(15f)
                .setCornerRadius(4f)
                .setAlpha(0.9f)
                .setAutoDismissDuration(1500L)
                .setTextColorResource(android.R.color.white)
                .setBackgroundColorResource(R.color.black)
                .build();
        Balloon balloonfeelweather = new Balloon.Builder(this)
                .setText("클릭하여 날씨/감정을 변경하세요.")
                .setArrowSize(10)
                .setArrowOrientation(ArrowOrientation.BOTTOM)
                .setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
                .setWidth(BalloonSizeSpec.WRAP)
                .setHeight(BalloonSizeSpec.WRAP)
                .setTextSize(15f)
                .setCornerRadius(4f)
                .setAutoDismissDuration(1500L)
                .setAlpha(0.9f)
                .setTextColorResource(android.R.color.white)
                .setBackgroundColorResource(R.color.black)
                .build();

        Intent intent = getIntent();
        preferences = getSharedPreferences("UserInfo", MODE_PRIVATE);

        if (intent.getStringExtra("date") == null) {
            Log.d(TAG, "onCreate: " + null);
            calendar.set(y, m, d);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            onlydate = sdf.format(calendar.getTime());
            String dayOfWeek = getDayOfWeek(calendar);
            binding.datebtn.setText(y + "년 " + (m + 1) + "월 " + d + "일 " + dayOfWeek);
        } else {
            Log.d(TAG, "onCreate: !null");
            String str = intent.getStringExtra("date");
            String[] integers = str.split(" ");
            y = Integer.parseInt(integers[0]);
            m = Integer.parseInt(integers[1]) - 1;
            d = Integer.parseInt(integers[2]);
            calendar.set(y, m, d);
            onlydate = new SimpleDateFormat("yyyyMMdd").format(calendar.getTime());
            String dayOfWeek = getDayOfWeek(calendar);
            binding.datebtn.setText(y + "년 " + (m + 1) + "월 " + d + "일 " + dayOfWeek);
        }

        binding.datebtn.setOnClickListener(v -> {
            DatePickerDialog dateDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear,
                                              int dayOfMonth) {
                            // Create a Calendar object and set the selected date
                            Calendar selectedDate = Calendar.getInstance();
                            selectedDate.set(year, monthOfYear, dayOfMonth);
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                            onlydate = sdf.format(selectedDate.getTime());
                            // Get the day of the week as a string (e.g., "Monday")
                            String dayOfWeek = getDayOfWeek(selectedDate);

                            // Update the text on your button
                            binding.datebtn.setText(year + "년 " + (monthOfYear + 1) + "월 " + dayOfMonth + "일 " + dayOfWeek);
                        }
                    }, y, m, d);
            dateDialog.show();
        });

        binding.locationbtn.setOnClickListener(v -> {
            loc = new EditText(this);
            locdialog = new AlertDialog.Builder(this)
                    .setTitle("어디에 다녀오셨나요?")
                    .setView(loc)
                    .setPositiveButton("확인", dialogListener)
                    .setNegativeButton("취소", null)
                    .show();
        });


        binding.makesuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (String.valueOf(binding.blogtext.getText()).equals("") || String.valueOf(binding.title.getText()).equals("") || binding.addimg.getText().equals("사진 추가")) {
                    Toast.makeText(getApplicationContext(), "일기를 확인해주세요!", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(makeblog.this);
                    AlertDialog loading = builder.create();
                    loading.setTitle("일기를 서버에 올리는중입니다...");
                    loading.show();
                    loading.setCancelable(false);
                    db = FirebaseFirestore.getInstance();
                    String selectedDate = binding.datebtn.getText().toString();
                    String title = binding.title.getText().toString();
                    String location = binding.locationbtn.getText().toString();
                    String blogcontent = binding.blogtext.getText().toString();
                    Map<String, Object> blogdata = new HashMap<>();
                    blogdata.put("content", blogcontent);
                    blogdata.put("date", selectedDate);
                    blogdata.put("location", location);
                    blogdata.put("title", title);
                    blogdata.put("emotion", defFeel);
                    blogdata.put("weather", defWeather);
                    blogdata.put("timestamp", onlydate);
                    blogdata.put("comments", new ArrayList<>());
                    blogdata.put("likes", new ArrayList<>());
                    blogdata.put("path",preferences.getString("uid","")+"_"+onlydate);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
                    sdf.setTimeZone(TimeZone.getTimeZone("GMT+9"));
                    blogdata.put("now",sdf.format(new Date()));
                    // preferences에서 uid를 가져오기
                    String uid = preferences.getString("uid", "");

                    // HashMap을 사용하여 이미지 URL 저장
                    // Map<Integer, String> photoUrls = new HashMap<>();

                    for (int i = 0; i < BitmapList.size(); i++) {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();

                        // 각 사진에 대해 다른 파일명 생성
                        String photoFileName = "photo_" + i + ".jpg";

                        // 경로 설정 (images/uid/yyyy-mm-dd/photo_0.jpg, images/uid/yyyy-mm-dd/photo_1.jpg, ...)
                        String path = "images/" + uid + "/" + onlydate + "/" + photoFileName;

                        // 현재 순번의 Bitmap 압축 및 업로드
                        Bitmap bm = BitmapList.get(i);
                        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] data = baos.toByteArray();

                        // Storage에 업로드
                        StorageReference storageRef = storage.getReference();
                        StorageReference btmap = storageRef.child(path);
                        UploadTask uploadTask = btmap.putBytes(data);

                        // 업로드 성공 시 이미지 URL을 HashMap에 저장

                        uploadTask.addOnSuccessListener(taskSnapshot -> {
                            // 이미지 업로드가 성공하면 해당 이미지의 다운로드 URL을 가져와서 저장
                            String puturl = taskSnapshot.getMetadata().getReference().toString();
                            firestoreurl.add(puturl);
                            Log.d(TAG, "bitmapUpload: " + puturl);
                            if (firestoreurl.size() == BitmapList.size()) {
                                blogdata.put("imgUrls", firestoreurl);
                                blogdata.put("uid", preferences.getString("uid", ""));
                                db.collection("diary")
                                        .document(preferences.getString("uid", "") + "_" + onlydate)
                                        .set(blogdata)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Log.d(TAG, "onSuccess: db저장완료");
                                                loading.dismiss();
                                                Toast.makeText(getApplicationContext(), "일기를 완성했어요!", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(getApplicationContext(), Basic_BlogView.class);
                                                intent.putExtra("uid", preferences.getString("uid", ""));
                                                intent.putExtra("date", onlydate);
                                                startActivity(intent);
                                                finish();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.d(TAG, "onFailure: db저장실패");
                                            }
                                        });
                            }
                        }).addOnFailureListener(exception -> {
                            // Handle unsuccessful uploads
                            Log.d(TAG, "onFailure: fail");
                        });
                    }
                }
            }
        });

        binding.feelWeather.setOnClickListener(view -> {
            ChooseFeelWeatherDialog dialog = new ChooseFeelWeatherDialog(this);
            dialog.setCustom(new CustomListener() {
                @Override
                public void doneClicked(int feelid, int weatherid) {
                    if (feelid == R.id.laughradio) {
                        binding.feel.setImageResource(R.drawable.laugh);
                        defFeel = 0;
                    } else if (feelid == R.id.madradio) {
                        binding.feel.setImageResource(R.drawable.mad);
                        defFeel = 1;
                    } else if (feelid == R.id.smileradio) {
                        binding.feel.setImageResource(R.drawable.smile);
                        defFeel = 2;
                    } else if (feelid == R.id.sadradio) {
                        binding.feel.setImageResource(R.drawable.sad);
                        defFeel = 3;
                    } else if (feelid == R.id.sosoradio) {
                        binding.feel.setImageResource(R.drawable.soso);
                        defFeel = 4;
                    }
                    if (weatherid == R.id.rainyradio) {
                        binding.weather.setImageResource(R.drawable.rainy);
                        defWeather = 0;
                    } else if (weatherid == R.id.sunnyradio) {
                        binding.weather.setImageResource(R.drawable.sunny);
                        defWeather = 1;
                    } else if (weatherid == R.id.cloudyradio) {
                        binding.weather.setImageResource(R.drawable.cloudy);
                        defWeather = 2;
                    } else if (weatherid == R.id.windyradio) {
                        binding.weather.setImageResource(R.drawable.windy);
                        defWeather = 3;
                    } else if (weatherid == R.id.snowyradio) {
                        binding.weather.setImageResource(R.drawable.snowy);
                        defWeather = 4;
                    }
                }
            });
            dialog.show();
        });

        binding.help.setOnClickListener(vv -> {
            balloondate.showAlignTop(binding.datebtn);
            balloonfeelweather.showAlignTop(binding.weather);
        });

        binding.addimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BitmapList = new ArrayList<>();
                uriArrayList = new ArrayList<>();
                pickMultipleMedia.launch(new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                        .build());
//                Intent intent = new Intent(Intent.ACTION_PICK);
//                intent.setType("image/*");
//                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                pickImagesLauncher.launch(intent);
            }
        });

        binding.blogback.setOnClickListener(view -> {
            back = new AlertDialog.Builder(this)
                    .setTitle("아직 일기를 작성하지 않았어요!")
                    .setMessage("지금 나가면 일기가 저장되지 않아요.")
                    .setPositiveButton("나가기", dialogListener)
                    .setNegativeButton("돌아가기", null)
                    .create();
            back.show();
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    ActivityResultLauncher<PickVisualMediaRequest> pickMultipleMedia =
            registerForActivityResult(new ActivityResultContracts.PickMultipleVisualMedia(4), uris -> {
                // Callback is invoked after the user selects media items or closes the
                // photo picker.
                if (!uris.isEmpty()) {
                    uriArrayList = (ArrayList<Uri>) uris;
                    for (Uri uri : uriArrayList
                    ) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                            try {
                                BitmapList.add(ImageDecoder.decodeBitmap(ImageDecoder.createSource(getContentResolver(), uri)));
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        } else {
                            try {
                                BitmapList.add(MediaStore.Images.Media.getBitmap(getContentResolver(), uri));
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                    binding.viewpager.setOffscreenPageLimit(1);
                    binding.viewpager.setAdapter(new ImageSlider(this, BitmapList));
                    binding.viewpager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                        @Override
                        public void onPageSelected(int position) {
                            super.onPageSelected(position);
                            setCurrentIndicator(position);
                        }
                    });
                    setupIndicators(BitmapList.size());
                    binding.viewimg.setVisibility(View.INVISIBLE);
                    binding.addimg.setText("사진 변경");
                    Log.d("PhotoPicker", "Number of items selected: " + uris.size());
                    Log.d(TAG, ": " + uris);

                } else {
                    Log.d("PhotoPicker", "No media selected");
                }
            });

    //    private final ActivityResultLauncher<Intent> pickImagesLauncher =
//            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
//                    result -> {
//                        if (result.getResultCode() == RESULT_OK) {
//                            Intent data = result.getData();
//                            try {
//                                handleImageSelection(data);
//                            } catch (IOException e) {
//                                throw new RuntimeException(e);
//                            }
//                        }
//                    });
//
//    private void handleImageSelection(Intent data) throws IOException {
//        if (data == null) {
//            Toast.makeText(getApplicationContext(), "이미지를 선택하지 않았습니다.", Toast.LENGTH_LONG).show();
//        } else {
//            if (data.getClipData() == null) {
//                Log.e("single choice: ", String.valueOf(data.getData()));
//                Uri imageUri = data.getData();
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//                    BitmapList.add(ImageDecoder.decodeBitmap(ImageDecoder.createSource(getContentResolver(), imageUri)));
//                } else {
//                    BitmapList.add(MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri));
//                }
//
//            } else {
//                ClipData clipData = data.getClipData();
//                Log.e("clipData", String.valueOf(clipData.getItemCount()));
//
//                if (clipData.getItemCount() > 4) {
//                    Toast.makeText(getApplicationContext(), "사진은 4장까지 선택 가능합니다.", Toast.LENGTH_LONG).show();
//                } else {
//                    Log.e(TAG, "multiple choice");
//
//                    for (int i = 0; i < clipData.getItemCount(); i++) {
//                        Uri imageUri = clipData.getItemAt(i).getUri();
//                        try {
//                            Log.d(TAG, "handleImageSelection: " + imageUri.toString());
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//                                BitmapList.add(ImageDecoder.decodeBitmap(ImageDecoder.createSource(getContentResolver(), imageUri)));
//                            } else {
//                                BitmapList.add(MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri));
//                            }
//                        } catch (Exception e) {
//                            Log.e(TAG, "File select error", e);
//                        }
//                    }
//                    binding.viewpager.setOffscreenPageLimit(1);
//                    binding.viewpager.setAdapter(new ImageSlider(this, BitmapList));
//                    binding.viewpager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
//                        @Override
//                        public void onPageSelected(int position) {
//                            super.onPageSelected(position);
//                            setCurrentIndicator(position);
//                        }
//                    });
//                    setupIndicators(BitmapList.size());
//                    binding.viewimg.setVisibility(View.INVISIBLE);
//                    binding.addimg.setText("사진 변경");
//                }
//            }
//        }
//    }

    private void setupIndicators(int count) {
        if (cnt > 0) {
            binding.indicator.removeAllViews();
        }
        cnt = count;
        Log.d(TAG, "setupIndicators: " + count);
        ImageView[] indicators = new ImageView[count];
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(16, 8, 16, 8);
        Log.d(TAG, "setupIndicators: " + indicators.length);
        for (int i = 0; i < indicators.length; i++) {
            indicators[i] = new ImageView(this);
            indicators[i].setImageDrawable(ContextCompat.getDrawable(this,
                    R.drawable.bg_indicator_inactive));
            indicators[i].setLayoutParams(params);
            binding.indicator.addView(indicators[i]);
        }
        setCurrentIndicator(0);
    }

    private void setCurrentIndicator(int position) {
        int childCount = cnt;
        Log.d(TAG, "setCurrentIndicator: " + childCount);
        for (int i = 0; i < childCount; i++) {
            ImageView imageView = (ImageView) binding.indicator.getChildAt(i);
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
}

