package sG.EDU.NP.MAD.friendsOnly.bottomNav;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

import sG.EDU.NP.MAD.friendsOnly.LikesPage;
import sG.EDU.NP.MAD.friendsOnly.R;
import sG.EDU.NP.MAD.friendsOnly.ToDoActivity;

public class NavMainPage extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    private FloatingActionButton ToDoFunc;
    ChatFragment chatFragment = new ChatFragment();
    StoryFragment storyFragment = new StoryFragment();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_main_page);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        ActionBar actionBar = getSupportActionBar();



        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,chatFragment).commit();
        actionBar.setTitle("Chat");
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.page_1:
                        actionBar.setTitle("Chat");
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,chatFragment).commit();
                        return true;
                    case R.id.page_2:
                        actionBar.setTitle("Story");
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,storyFragment).commit();
                        return true;
                }

                return false;
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // R.menu.mymenu is a reference to an xml file named mymenu.xml which should be inside your res/menu directory.
        // If you don't have res/menu, just create a directory named "menu" inside res
        getMenuInflater().inflate(R.menu.fav_page, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_name) {
            Intent intent = new Intent(NavMainPage.this, LikesPage.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

}