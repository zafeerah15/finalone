package sG.EDU.NP.MAD.friendsOnly.bottomNav;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import sG.EDU.NP.MAD.friendsOnly.LikesPage;
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.fav_page, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_name) {
            startActivity(new Intent(NavMainPage.this, LikesPage.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}