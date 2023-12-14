package com.example.epilog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class FriendsCalandar extends AppCompatActivity {
    TextView textView;
    String nickname;
    String uid;
    ImageButton button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_calandar);
        textView = findViewById(R.id.friendcalandarname);
        button = findViewById(R.id.friendcalandarbackbtn);

        Intent intent = getIntent();
        nickname = intent.getStringExtra("nickname");
        uid = intent.getStringExtra("uid");
        textView.setText(nickname + "님의 캘린더");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        LinearLayoutManager monthListManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        FriendCalanderListAdapter monthListAdapter = new FriendCalanderListAdapter(nickname, uid);

        RecyclerView calendarCustom = findViewById(R.id.friendcalandarRecycler);
        calendarCustom.setLayoutManager(monthListManager);
        calendarCustom.setAdapter(monthListAdapter);
        calendarCustom.scrollToPosition(Integer.MAX_VALUE / 2);

        PagerSnapHelper snap = new PagerSnapHelper();
        snap.attachToRecyclerView(calendarCustom);
    }
}