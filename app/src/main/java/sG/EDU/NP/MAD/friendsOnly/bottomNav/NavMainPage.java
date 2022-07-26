package sG.EDU.NP.MAD.friendsOnly.bottomNav;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import sG.EDU.NP.MAD.friendsOnly.R;

public class NavMainPage extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    ChatFragment chatFragment = new ChatFragment();
    StoryFragment storyFragment = new StoryFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_main_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        // using toolbar as ActionBar
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Chat");

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,chatFragment).commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.page_1:
                        getSupportActionBar().setTitle("Chat");
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,chatFragment).commit();
                        return true;
                    case R.id.page_2:
                        getSupportActionBar().setTitle("Story");
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,storyFragment).commit();
                        return true;
                }

                return false;
            }
        });
    }
}