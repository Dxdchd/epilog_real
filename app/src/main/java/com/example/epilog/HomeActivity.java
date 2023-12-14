package com.example.epilog;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.epilog.Fragment.blog;
import com.example.epilog.Fragment.calander;
import com.example.epilog.Fragment.friendslist;
import com.example.epilog.Fragment.settings;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.epilog.databinding.ActivityHomeBinding;
import com.google.android.material.navigation.NavigationBarView;

public class HomeActivity extends AppCompatActivity {

    private Fragment fragment_blog;
    private Fragment fragment_calander;
    private Fragment fragment_friendslist;
    private Fragment fragment_settings;
    private BottomNavigationView bottomNavigationView;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        preferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
        String uid = preferences.getString("uid", "");
        Bundle bundle = new Bundle();
        bundle.putString("uid", uid);

        fragment_blog = new blog();
        fragment_friendslist = new friendslist();
        fragment_calander = new calander();
        fragment_settings = new settings();
        fragment_blog.setArguments(bundle);

        bottomNavigationView = findViewById(R.id.nav_view);


        getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, fragment_blog).commitNowAllowingStateLoss();

        bottomNavigationView = findViewById(R.id.nav_view);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.bloglist) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, fragment_blog).commitAllowingStateLoss();
                } else if (item.getItemId() == R.id.calander) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, fragment_calander).commitAllowingStateLoss();
                } else if (item.getItemId() == R.id.setting) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, fragment_settings).commitAllowingStateLoss();
                } else {
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, fragment_friendslist).commitAllowingStateLoss();
                }
                return true;
            }
        });

    }

}