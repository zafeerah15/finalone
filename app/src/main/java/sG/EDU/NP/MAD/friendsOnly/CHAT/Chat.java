package sG.EDU.NP.MAD.friendsOnly.CHAT;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;
import sG.EDU.NP.MAD.friendsOnly.MemoryData;
import sG.EDU.NP.MAD.friendsOnly.R;
//import com.devlomi.record_view.OnRecordListener;

public class Chat extends AppCompatActivity {
//    ///---------------------By Syafiq---------------------------V
//    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
//    private static final String[] permissions = {RECORD_AUDIO};
//    private boolean audioRecordingPermissionGranted = false;
//    private static final String LOG_TAG = "AudioRecordTest";
//    String AudioSavePathInDevice = null;
//    public static final int RequestPermissionCode = 1;
//    String RandomAudioFileName = "ABCDEFGHIJKLMNOP";
//    Button startRecordingButton, stopRecordingButton, playRecordingButton, stopPlayingButton;;
//    MediaRecorder recorder;
//    MediaPlayer player;
//    Random random ;
    private Button record;
//    ///---------------------By Syafiq---------------------------^

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    private final List<Chat_List> chatLists = new ArrayList<>();

    private FirebaseAuth mAuth;
    private String chatKey;
    String getUserMobile = "";
    private RecyclerView chattingRecyclerView;
    private sG.EDU.NP.MAD.friendsOnly.CHAT.Chat_Adapter chatAdapter;
    private boolean loadingFirstTime = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chat);

//        ///---------------------By Syafiq (Recording of audio)---------------------------V
        record = (Button) findViewById(R.id.activity_main_record);
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openrecording();
            }
        });
//        ActivityCompat.requestPermissions(this, permissions,
//                REQUEST_RECORD_AUDIO_PERMISSION);
//
//        startRecordingButton = (Button) findViewById(R.id.activity_main_record);
//        stopRecordingButton = (Button) findViewById(R.id.activity_main_stop);
//        playRecordingButton = (Button) findViewById(R.id.activity_main_play);
//        stopPlayingButton = (Button) findViewById(R.id.activity_main_stop_playing);
//
////        stopRecordingButton.setEnabled(false);
////        playRecordingButton.setEnabled(false);
////        stopPlayingButton.setEnabled(false);
//
//        random = new Random();
//
//        startRecordingButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(checkPermission()) {
//
////                    AudioSavePathInDevice =
////                            getExternalCacheDir().getAbsolutePath() + "/" +
////                                    CreateRandomAudioFileName(5) + "AudioRecording.3gp";
//
//
//                    MediarecorderReady();
//
//                    try {
//                        recorder.prepare();
//                        recorder.start();
//                    } catch (IllegalStateException e) {
//                        //TODO Auto-generated catch block
//                        e.printStackTrace();
//                    } catch (IOException e) {
//                        //TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
//
////                    startRecordingButton.setEnabled(false);
////                    stopRecordingButton.setEnabled(true);
//
//                    Toast.makeText(Chat.this, "Recording started",
//
//                            Toast.LENGTH_LONG).show();
//                } else {
//                    requestPermission();
//                }
//            }
//        });
//
//        stopRecordingButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                recorder.stop();
//                recorder.release();
//                recorder = null;
////                stopRecordingButton.setEnabled(false);
////                playRecordingButton.setEnabled(true);
////                startRecordingButton.setEnabled(true);
////                stopPlayingButton.setEnabled(false);
//
//                Toast.makeText(Chat.this, "Recording Completed",
//                        Toast.LENGTH_LONG).show();
//            }
//        });
//
//        playRecordingButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) throws IllegalArgumentException,
//                    SecurityException, IllegalStateException {
//
////                stopRecordingButton.setEnabled(false);
////                startRecordingButton.setEnabled(false);
////                stopRecordingButton.setEnabled(true);
//
//                player = new MediaPlayer();
//                try {
//                    player.setDataSource(getRecordingFilePath());
//                    player.prepare();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                player.start();
//                Toast.makeText(Chat.this, "Recording Playing",
//                        Toast.LENGTH_LONG).show();
//            }
//        });
//
//        stopPlayingButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                stopRecordingButton.setEnabled(false);
////                startRecordingButton.setEnabled(true);
////                stopPlayingButton.setEnabled(false);
////                playRecordingButton.setEnabled(true);
//
//                if(player.isPlaying()){
//                    player.pause();
//                    player.release();
//                    MediarecorderReady();
//                }
//            }
//        });
//
//        ///---------------------By Syafiq---------------------------^

        final ImageView backBtn = findViewById(R.id.backBtn);
        final TextView nameTV = findViewById(R.id.user_name);
        final EditText messageEditText = findViewById(R.id.messageEditTxt);
        final CircleImageView profilePic = findViewById(R.id.profilepicture);
        final ImageView sendBtn = findViewById(R.id.sendBtn);

        //Recyclerview
        chattingRecyclerView = findViewById(R.id.chattingRecyclerView);
        // get data from messages adapter class
        final String getName = getIntent().getStringExtra("name");
        final String getProfilePic = getIntent().getStringExtra("profile_pic");
        chatKey = getIntent().getStringExtra("chat_key");
        final String getMobile = getIntent().getStringExtra("mobile");

        //Firebase get instance
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        // get user mobile from memory
        //getUserMobile = MemoryData.getData(Chat.this);
        getUserMobile = currentUser.getDisplayName();

        nameTV.setText(getName);

        //Profile picture loading default
        if (!getProfilePic.isEmpty()) {
            Picasso.get().load(getProfilePic).into(profilePic);
        }


        chattingRecyclerView.setHasFixedSize(true);
        chattingRecyclerView.setLayoutManager(new LinearLayoutManager(Chat.this));


        chatAdapter = new Chat_Adapter(chatLists, Chat.this);

        chattingRecyclerView.setAdapter(chatAdapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {



                if (chatKey.isEmpty()) {
                    // generate chat key. default chatKey is 1
                    chatKey = "1";

                    if (snapshot.hasChild("chat")) {
                        chatKey = String.valueOf(snapshot.child("chat").getChildrenCount() + 1);
                    }
                }

                chatKey = snapshot.child("chat").hasChild(getMobile + getUserMobile) ? getMobile + getUserMobile : getUserMobile + getMobile;


//                Toast.makeText(Chat.this, chatKey, Toast.LENGTH_SHORT).show();
                if (snapshot.hasChild("chat")) {
                    if (snapshot.child("chat").child(chatKey).hasChild("messages")) {
                        chatLists.clear();
                        for (DataSnapshot messagesSnapshot : snapshot.child("chat").child(chatKey).child("messages").getChildren()) {

                            if (messagesSnapshot.hasChild("msg") && messagesSnapshot.hasChild("mobile")) {

                                final String messageTimestamps = messagesSnapshot.getKey();
                                final String getMobile = messagesSnapshot.child("mobile").getValue(String.class);
                                final String getMsg = messagesSnapshot.child("msg").getValue(String.class);

                                Timestamp timestamp = new Timestamp(Long.parseLong(messageTimestamps));

                                Date date = new Date(timestamp.getTime());
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                                SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("hh:mm aa", Locale.getDefault());

                                simpleDateFormat.setTimeZone(TimeZone.getDefault());
                                simpleTimeFormat.setTimeZone(TimeZone.getDefault());


                                Chat_List chatList = new Chat_List(getMobile, getName, getMsg, simpleDateFormat.format(date), simpleTimeFormat.format(date));
                                chatLists.add(chatList);

                                if (loadingFirstTime || Long.parseLong(messageTimestamps) > Long.parseLong(MemoryData.getLastMsgTS(Chat.this, chatKey))) {

                                    loadingFirstTime = false;

                                    MemoryData.saveLastMsgTS(messageTimestamps, chatKey, Chat.this);
                                    chatAdapter.updateChatList(chatLists);

                                    chattingRecyclerView.scrollToPosition(chatLists.size() - 1);
                                }
                            }


                        }

                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Send button onclick listener, send to firebase instance
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String getTxtMessage = messageEditText.getText().toString();


                //get current timestamps
                final String currentTimestamp = String.valueOf(System.currentTimeMillis());


                databaseReference.child("chat").child(chatKey).child("user_1").setValue(getUserMobile);
                databaseReference.child("chat").child(chatKey).child("user_2").setValue(getMobile);
                databaseReference.child("chat").child(chatKey).child("messages").child(currentTimestamp).child("msg").setValue(getTxtMessage);
                databaseReference.child("chat").child(chatKey).child("messages").child(currentTimestamp).child("mobile").setValue(getUserMobile);

                // clear edit text
                messageEditText.setText("");
            }

        });

        //Back button to go back to previous page
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
//    ///---------------------By Syafiq (For recording of audio)---------------------------V
    public void openrecording(){
        Intent record = new Intent(this, recording.class);
        startActivity(record);
    }
//    public String CreateRandomAudioFileName(int string){
//        StringBuilder stringBuilder = new StringBuilder( string );
//        int i = 0 ;
//        while(i < string ) {
//            stringBuilder.append(RandomAudioFileName.charAt(random.nextInt(RandomAudioFileName.length())));
//
//            i++ ;
//        }
//        return stringBuilder.toString();
//    }
//
//    private void requestPermission() {
//        ActivityCompat.requestPermissions(Chat.this, new
//                String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, RequestPermissionCode);
//    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String permissions[],
//                                           int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode) {
//            case RequestPermissionCode:
//                if (grantResults.length> 0) {
//                    boolean StoragePermission = grantResults[0] ==
//                            PackageManager.PERMISSION_GRANTED;
//                    boolean RecordPermission = grantResults[1] ==
//                            PackageManager.PERMISSION_GRANTED;
//
//                    if (StoragePermission && RecordPermission) {
//                        Toast.makeText(Chat.this, "Permission Granted",
//                                Toast.LENGTH_LONG).show();
//                    } else {
//                        Toast.makeText(Chat.this,"Permission Denied",
//                                Toast.LENGTH_LONG).show();
//                    }
//                }
//                break;
//        }
//    }
//    private void MediarecorderReady() {
//        recorder=new MediaRecorder();
//        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
//        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
//        recorder.setOutputFile(getRecordingFilePath());
//    }
//
////    private void stopRecording() {
////        if (recorder != null) {
////            recorder.stop();
////            recorder.release();
////            recorder = null;
////        }
////        Toast.makeText(this, "Recording has stopped",Toast.LENGTH_LONG).show();
////    }
////    private void playRecording() {
////        player = new MediaPlayer();
////        try {
////            player.setDataSource(getRecordingFilePath());
////            player.prepare();
////            player.start();
////            Toast.makeText(this, "Recording is playing",Toast.LENGTH_LONG).show();
////        } catch (IOException e) {
////            Log.e(LOG_TAG, "prepare() failed");
////        }
////    }
////
////    private void stopPlaying() {
////        if (player != null) {
////            player.release();
////            player = null;
////        }
////        Toast.makeText(this, "Recording has stopped playing",Toast.LENGTH_LONG).show();
////    }
//    private String getRecordingFilePath(){
//        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
//        File musicDirectory = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
//        File file = new File(musicDirectory, CreateRandomAudioFileName(5) + "AudioRecording.3gp");
//        return file.getPath();
//    }
//    public boolean checkPermission() {
//        int result = ContextCompat.checkSelfPermission(getApplicationContext(),
//                WRITE_EXTERNAL_STORAGE);
//        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(),
//                RECORD_AUDIO);
//        return result == PackageManager.PERMISSION_GRANTED &&
//                result1 == PackageManager.PERMISSION_GRANTED;
//    }
//    ///---------------------By Syafiq---------------------------^
}