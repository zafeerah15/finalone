package sG.EDU.NP.MAD.friendsOnly;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;


public class RadioActivity extends AppCompatActivity {

    //Mediaplayer
    private static MediaPlayer mediaPlayer = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radio);
        getSupportActionBar().hide();

        //Pause button
        ImageButton pausebutton = findViewById(R.id.pauseRadio);
        //Play button
        ImageButton playbutton = findViewById(R.id.playRadio);
        //Radio station name
        TextView radioTitle = findViewById(R.id.radioTitle);
        //Radio link reference to credit for copyright
        TextView reference = findViewById(R.id.reference);

        //Receive intent data
        Intent receivingData = getIntent();
        String radioLink = receivingData.getStringExtra("Link");
        String radioName = receivingData.getStringExtra("Title");
        Boolean radioInternet = receivingData.getBooleanExtra("IsOffline", false);

        //Setting reference
        reference.setText("Radio: " + radioLink);
        //Setting radioname
        radioTitle.setText(radioName);

        //Create new mediaplayer
        mediaPlayer = new MediaPlayer();

        //Plays online radio when clicked
        if (radioInternet == false) {
            Toast.makeText(RadioActivity.this, "Please wait after pressing play", Toast.LENGTH_SHORT).show();
            //When user clicks play button
            playbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = radioLink; // your URL here
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    pausebutton.setEnabled(true);
                    playbutton.setEnabled(false);

                    try {
                        mediaPlayer.setDataSource(url);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    }

                    //Reason for separating them into 2 tries is to know which is causing the error
                    //Buffering
                    try {
                        mediaPlayer.prepare(); // might take long! (for buffering, etc)
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    }

                    //Wait for the mediaplayer to prepare
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                        public void onPrepared(MediaPlayer mp) {
                            mediaPlayer.start();
                        }
                    });
                }
            });
        }

        //When offline song is clicked
        else{
            reference.setText("Music: https://www.bensound.com");
            playbutton.setOnClickListener(new View.OnClickListener() {
                int link;
                @Override
                public void onClick(View v) {
                    if(radioLink.equals("lovejazz")){
                        link = R.raw.lovejazz;
                    }
                    else if(radioLink.equals("memories"))
                    {
                        link = R.raw.memories;
                    }
                    else if(radioLink.equals("relaxing"))
                    {
                        link = R.raw.relaxing;
                    }
                    else if(radioLink.equals("romantic"))
                    {
                        link = R.raw.romantic;
                    }

                    //When user presses the play button, it gets disabled and pause button gets enabled
                    pausebutton.setEnabled(true);
                    playbutton.setEnabled(false);
                    mediaPlayer = MediaPlayer.create(getApplicationContext(),link);
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mediaPlayer.start();
                        }
                    });
                }
            });

        }

        //Stop button
        pausebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.pause();

                //When user presses pause button, it gets disabled and play button gets enabled
                playbutton.setEnabled(true);
                pausebutton.setEnabled(false);
                finish();
                startActivity(getIntent());
            }
        });


    }

    //When user goes exits radio activity page, radio stops. Will find a way to improve this feature
    @Override
    protected void onPause(){
        super.onPause();
        mediaPlayer.stop();
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

}
