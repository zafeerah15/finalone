package sG.EDU.NP.MAD.friendsOnly;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SelectRadioActivity extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseDatabase database;
    ArrayList<Radio> radioList = new ArrayList<>();
    RadioAdapter radioAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_radio);
        getSupportActionBar().hide();

        //To access firebase
        auth = FirebaseAuth.getInstance();
        //To access database
        database = FirebaseDatabase.getInstance("https://studyfi-19a30-default-rtdb.asia-southeast1.firebasedatabase.app/");

        //To reading or writing of data
        DatabaseReference reference = FirebaseDatabase.getInstance("https://studyfi-19a30-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("Radio");
        //receive events about data changes to user
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapShot: dataSnapshot.getChildren())
                {
                    Radio radio = new Radio();
                    radio.setRadioLink(snapShot.child("Link").getValue().toString());
                    radio.setOffline(Boolean.valueOf(snapShot.child("isOffline").getValue().toString()));
                    radio.setRadioName(snapShot.child("Name").getValue().toString());
                    radioList.add(radio);
                }
                radioAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Creating offline song objects

        Radio songOffline1 = new Radio();
        songOffline1.radioLink = "lovejazz";
        songOffline1.radioName = "Love Jazz";
        songOffline1.isOffline = true;

        Radio songOffline2 = new Radio();
        songOffline2.radioLink = "memories";
        songOffline2.radioName = "Memories";
        songOffline2.isOffline = true;

        Radio songOffline3 = new Radio();
        songOffline3.radioLink = "relaxing";
        songOffline3.radioName = "Relaxing Meditation";
        songOffline3.isOffline = true;

        Radio songOffline4 = new Radio();
        songOffline4.radioLink = "romantic";
        songOffline4.radioName = "Slow Jazz";
        songOffline4.isOffline = true;

        //Add songs to a list
        radioList.add(songOffline1);
        radioList.add(songOffline2);
        radioList.add(songOffline3);
        radioList.add(songOffline4);

        //Radio selecting recyclerview
        RecyclerView selectradiorv = findViewById(R.id.radiorv);
        radioAdapter = new RadioAdapter(radioList, getApplicationContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        selectradiorv.setLayoutManager(linearLayoutManager);
        selectradiorv.setAdapter(radioAdapter);

    }

    //Prevent activity from stacking

}
