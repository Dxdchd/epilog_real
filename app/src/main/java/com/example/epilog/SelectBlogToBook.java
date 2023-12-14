package com.example.epilog;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.epilog.databinding.ActivitySelectBlogToBookBinding;
import com.example.epilog.databinding.PdflayoutBinding;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class SelectBlogToBook extends AppCompatActivity {
    public static List<String> paths = new ArrayList<>();
    private SharedPreferences preferences;
    private FirebaseFirestore firestore;
    private String uid;
    private List<String> finishedPdfs = new ArrayList<>();
    private Document document;

    private Runnable checkPdfExportFinished = () -> {
        while(true){
            Log.d("pdfCnt", String.valueOf(finishedPdfs.size()));
            if(finishedPdfs.size() == paths.size()){
                document.close();
                finish();
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
        firestore = FirebaseFirestore.getInstance();

        uid = preferences.getString("uid", "");

        ActivitySelectBlogToBookBinding activitySelectBlogToBookBinding = ActivitySelectBlogToBookBinding.inflate(getLayoutInflater());
        setContentView(activitySelectBlogToBookBinding.getRoot());

        requestForStoragePermissions();

        RecyclerView recyclerView = activitySelectBlogToBookBinding.makebookrecycler;
        ImageButton backBtn = activitySelectBlogToBookBinding.makebookback;
        backBtn.setOnClickListener(v -> {
            finish();
        });

        Button nextBtn = activitySelectBlogToBookBinding.makeNext;
        nextBtn.setOnClickListener(v -> {
            document = new Document();
            String dirpath = Environment.getExternalStorageDirectory().toString();

            File dir = new File(Environment.getExternalStorageDirectory() + File.separator + "epilog_saved");
            if(!dir.exists()){
                dir.mkdirs();
            }


            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+9"));
            try {
                PdfWriter.getInstance(document, new FileOutputStream(dirpath + "/epilog_saved/"+sdf.format(new Date())+".pdf"));
            } catch (DocumentException e) {
                throw new RuntimeException(e);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }

            document.open();
            for (String path : paths) {
                PdflayoutBinding bd = PdflayoutBinding.inflate(getLayoutInflater());
                DocumentReference documentReference = firestore.collection("diary").document(path);
                documentReference.get().addOnSuccessListener(documentSnapshot -> {
                    FromDb fromDb = documentSnapshot.toObject(FromDb.class);
                    bd.datebtn.setText(fromDb.getDate());
                    bd.title.setText(fromDb.getTitle());
                    bd.locationbtn.setText(fromDb.getLocation());
                    bd.content.setText(fromDb.getContent());
//                    bd.blogViewScrollView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
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
                    LinearLayout layout = bd.getRoot();
                    layout.setLayoutParams(new ViewGroup.LayoutParams(2000, 3000));
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    Log.d("test", "bindSliderImage: " + storage);
                    StorageReference pa = storage.getReferenceFromUrl(fromDb.getImgUrls().get(0));
                    Log.d("test", "bindSliderImage: " + pa);
                    pa.getDownloadUrl().addOnSuccessListener(uri -> {
                        Glide.with(bd.getRoot())
                                .load(uri)
                                .centerCrop()
                                .addListener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        Log.d("TAG", "onLoadFailed: ");
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                        Log.d("TAG", "onResourceReady: ");
                                        Log.d("TAG", "onCreate: ");
                                        layout.post(() -> {
                                            Log.d("TAG", "onCreate: ");
                                            int width = 2000;
                                            int height = 3000;
                                            Log.d("TAG", "onCreate: " + width + " " + height);
                                            Bitmap bm = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                                            Canvas canvas = new Canvas(bm);
                                            Drawable bgDrawable = layout.getBackground();
                                            if (bgDrawable != null) {
                                                bgDrawable.draw(canvas);
                                            } else {
                                                canvas.drawColor(Color.WHITE);
                                            }
                                            layout.draw(canvas);

                                            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                                            bm.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

                                            File f = new File(Environment.getExternalStorageDirectory() + File.separator + "image.jpg");
                                            try {
                                                f.createNewFile();
                                                FileOutputStream fo = new FileOutputStream(f);
                                                fo.write(bytes.toByteArray());

                                                Image image = Image.getInstance(f.toString());
                                                float scaler = ((document.getPageSize().getWidth() - document.leftMargin()
                                                        - document.rightMargin() - 0) / image.getWidth()) * 100;
                                                image.scalePercent(scaler);
                                                image.setAlignment(Image.ALIGN_CENTER | Image.ALIGN_TOP);
                                                document.add(image);
                                                document.newPage();
                                                finishedPdfs.add(path);
                                                f.delete();
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        });
                                        return false;
                                    }
                                })
                                .into(bd.viewpager);
                        setContentView(layout);
                        layout.setVisibility(View.INVISIBLE);
                    });
                });
            }

            Thread thread = new Thread(checkPdfExportFinished);
            thread.start();
        });

        firestore.collection("diary")
                .whereEqualTo("uid", uid)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<FromDb> items = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            FromDb fromDb = document.toObject(FromDb.class);
                            items.add(fromDb);
                        }

                        SelectBlogAdapter adapter = new SelectBlogAdapter(items);
                        RecyclerView.LayoutManager mlayoutManager = new GridLayoutManager(getApplicationContext(), 3);
                        recyclerView.setLayoutManager(mlayoutManager);
                        recyclerView.setAdapter(adapter);
                    }
                });
    }

    private void requestForStoragePermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager())
                return;
        } else {
            return;
        }

        ActivityResultLauncher<Intent> storageActivityResultLauncher =
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                        o -> {
                            if (!Environment.isExternalStorageManager()) {
                                Toast.makeText(SelectBlogToBook.this, "저장 공간 접근 권한이 거부되었어요.", Toast.LENGTH_SHORT).show();
                            }
                        });

        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
        Uri uri = Uri.fromParts("package", this.getPackageName(), null);
        intent.setData(uri);
        storageActivityResultLauncher.launch(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(SelectBlogToBook.this, "/sdcard/epilog_saved 폴더에 저장되었어요.", Toast.LENGTH_SHORT).show();
        paths.clear();
    }


}