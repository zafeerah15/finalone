package sG.EDU.NP.MAD.friendsOnly;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import sG.EDU.NP.MAD.friendsOnly.story.StoryAdapter;
import sG.EDU.NP.MAD.friendsOnly.story.StoryList;

public class LikesPage extends AppCompatActivity {

    private List<StoryList> storyLists = new ArrayList<>();
    private StorageReference mediaRef;
    private FirebaseAuth mAuth;
    private RecyclerView storyRecyclerView;
    private StoryAdapter storyAdapter;
    Session session;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_likes_page);
        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.refresh_story);
        storyRecyclerView = findViewById(R.id.storyRecyclerView);

        // calling the action bar
        ActionBar actionBar = getSupportActionBar();

        actionBar.setTitle("Likes");

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
                pullToRefresh.setRefreshing(false);
            }
        });


        session = new Session(LikesPage.this);
        storyRecyclerView.setHasFixedSize(true);
        storyRecyclerView.setLayoutManager(new LinearLayoutManager(LikesPage.this));
        // set adapter to recyclerview
        storyAdapter = new StoryAdapter(storyLists, LikesPage.this);
        storyRecyclerView.setAdapter(storyAdapter);

        getData();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void getData() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        final String getUser = currentUser.getDisplayName();
        storyLists.clear();

        databaseReference.child("users").child(getUser).child("likes").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@androidx.annotation.NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {


                } else {
                    ArrayList frenz = new ArrayList<String>();
                    frenz = (ArrayList) task.getResult().getValue();
                    Log.d("testt", getUser);
                    Query postAscending = databaseReference.child("stories").orderByChild("date");

                    ArrayList finalFrenz = frenz;

                    postAscending.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {

                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                final String userId = dataSnapshot.child("userId").getValue(String.class);
                                final String key = dataSnapshot.getKey();
                                Log.d("testttt", key);
                                if ((finalFrenz !=null && finalFrenz.contains(key))) {
                                    final String getName = dataSnapshot.child("username").getValue(String.class);
                                    final String getProfilePic = dataSnapshot.child("prof_url").getValue(String.class);
                                    final String getstory_url = dataSnapshot.child("story_url").getValue(String.class);
                                    final String caption = dataSnapshot.child("caption").getValue(String.class);
                                    final Long time = dataSnapshot.child("date").getValue(Long.class);

                                    StoryList storyList = new StoryList(key, userId, getName, getProfilePic, getstory_url, caption, true, time);
                                    storyLists.add(storyList);
                                }
                            }
                            Collections.reverse(storyLists);
                            storyAdapter.updateData(storyLists);
                        }

                        @Override
                        public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {

                        }
                    });
                }

            }
        });


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); // go back previous activity
        finish();
        return super.onSupportNavigateUp();
    }

}