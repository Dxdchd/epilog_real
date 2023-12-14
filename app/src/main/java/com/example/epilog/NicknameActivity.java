package com.example.epilog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NicknameActivity extends AppCompatActivity {
    private static final String TAG = "NicknameActivity";

    private String name;
    private Intent intent;
    private FirebaseFirestore db;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        db = FirebaseFirestore.getInstance();
        preferences = getSharedPreferences("UserInfo", MODE_PRIVATE);

        intent = getIntent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nickname);
        EditText editText = (EditText) findViewById(R.id.inputnick);
        editText.setText(intent.getStringExtra("nickname")); //뭔가 이 구문은 5자가 넘어가는 이름을 가져오면 오류가 발생할거같음
        TextView limit = (TextView) findViewById(R.id.limitnick);
        Button btn = (Button) findViewById(R.id.login_next);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 0) {
                    limit.setText("0/5");
                } else if (charSequence.length() == 1) {
                    limit.setText("1/5");
                } else if (charSequence.length() == 2) {
                    limit.setText("2/5");
                } else if (charSequence.length() == 3) {
                    limit.setText("3/5");
                } else if (charSequence.length() == 4) {
                    limit.setText("4/5");
                } else {
                    limit.setText("5/5");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (String.valueOf(editText.getText()).equals("")) {
                    Toast.makeText(getApplicationContext(), "닉네임을 입력해주세요!", Toast.LENGTH_SHORT).show();
                } else {
                    name = String.valueOf(editText.getText());

                    ArrayList<String>friends = new ArrayList<>();
                    Map<String, Object> user = new HashMap<>();
                    user.put("email", intent.getStringExtra("email"));
                    user.put("nickname", name);
                    user.put("friends",friends);
                    user.put("uid",intent.getStringExtra("uid"));

                    db.collection("user")
                            .document(intent.getStringExtra("uid"))
                            .set(user)
                            .addOnSuccessListener(new OnSuccessListener() {
                                @Override
                                public void onSuccess(Object o) {
                                    Log.d(TAG, "DocumentSnapshot added with ID: " + intent.getStringExtra("uid"));
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error adding document", e);
                                }
                            });

                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("uid", intent.getStringExtra("uid"));
                    editor.putString("nickname", name);
                    editor.putString("email", intent.getStringExtra("email"));
                    editor.commit();

                    Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}