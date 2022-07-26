package sG.EDU.NP.MAD.friendsOnly.CHAT;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import sG.EDU.NP.MAD.friendsOnly.R;

public class recording extends AppCompatActivity {
    ///---------------------By Syafiq---------------------------V
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static final String[] permissions = {RECORD_AUDIO};
    private boolean audioRecordingPermissionGranted = false;
    private static final String LOG_TAG = "AudioRecordTest";
    public static final int RequestPermissionCode = 1;
    private File recordfile;
    private String filename;
    String RandomAudioFileName = "ABCDEFGHIJ012345";
    Button startRecordingButton, stopRecordingButton, playRecordingButton, stopPlayingButton;;
    MediaRecorder recorder;
    MediaPlayer player;
    Random random ;
    TextView recordingpath;
    boolean isrecording=false;
    boolean isplaying=false;

    TextView timer;
    int seconds=0;
    String path=null;
    int dummyseconds=0;
    int playableseconds=0;
    Handler handler;

    ExecutorService executorService = Executors.newSingleThreadExecutor();
    ///---------------------By Syafiq---------------------------^

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording);
        ///---------------------By Syafiq (Recording of audio)---------------------------V
        ActivityCompat.requestPermissions(this, permissions,
                REQUEST_RECORD_AUDIO_PERMISSION);

        startRecordingButton = (Button) findViewById(R.id.main_record);
        stopRecordingButton = (Button) findViewById(R.id.pause_record);
        playRecordingButton = (Button) findViewById(R.id.play_record);
        stopPlayingButton = (Button) findViewById(R.id.pause_play);
        final ImageView backBtn = findViewById(R.id.backBtn);
        recordingpath = findViewById(R.id.recording_path);
        timer = findViewById(R.id.timer);

//        stopRecordingButton.setEnabled(false);
//        playRecordingButton.setEnabled(false);
//        stopPlayingButton.setEnabled(false);

        random = new Random();
        player = new MediaPlayer();

        startRecordingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkPermission()) {

//                    AudioSavePathInDevice =
//                            getExternalCacheDir().getAbsolutePath() + "/" +
//                                    CreateRandomAudioFileName(5) + "AudioRecording.3gp";
                    if(!isrecording){
                        isrecording=true;
                        executorService.execute(new Runnable() {
                            @Override
                            public void run() {
                                path=getRecordingFilePath();
                                MediarecorderReady();
                                filename=path;
                                try {
                                    recorder.prepare();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                recorder.start();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        recordingpath.setText(filename);
                                        playableseconds=0;
                                        seconds=0;
                                        dummyseconds=0;
                                        runTimer();
                                    }
                                });

                            }
                        });
                    }
//                    startRecordingButton.setEnabled(false);
//                    stopRecordingButton.setEnabled(true);
                    Toast.makeText(recording.this, "Recording started",
                            Toast.LENGTH_LONG).show();
                }

                else {
                    requestPermission();
                }
            }
        });

        stopRecordingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        recorder.stop();
                        recorder.release();
                        recorder = null;
                        playableseconds=seconds;
                        dummyseconds=seconds;
                        seconds=0;
                        isrecording=false;

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                handler.removeCallbacksAndMessages(null);
                            }
                        });
                    }
                });




//                stopRecordingButton.setEnabled(false);
//                playRecordingButton.setEnabled(true);
//                startRecordingButton.setEnabled(true);
//                stopPlayingButton.setEnabled(false);

                Toast.makeText(recording.this, "Recording Completed",
                        Toast.LENGTH_LONG).show();
            }
        });

        playRecordingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) throws IllegalArgumentException,
                    SecurityException, IllegalStateException {

//                stopRecordingButton.setEnabled(false);
//                startRecordingButton.setEnabled(false);
//                stopRecordingButton.setEnabled(true);

                if(!isplaying){
                    if (path!=null){
                        try {
                            player.setDataSource(filename);
                            player.prepare();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"No Recording present", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    player.start();
                    isplaying=true;
                    Toast.makeText(recording.this, "Playing Recording",
                            Toast.LENGTH_LONG).show();
                    runTimer();

                }
            }
        });

        stopPlayingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                stopRecordingButton.setEnabled(false);
//                startRecordingButton.setEnabled(true);
//                stopPlayingButton.setEnabled(false);
//                playRecordingButton.setEnabled(true);

                player.stop();
                player.release();
                player=null;
                player= new MediaPlayer();
                isplaying=false;
                //MediarecorderReady();
                seconds=0;
                handler.removeCallbacksAndMessages(null);
            }
        });


        //Back button to go back to previous page
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ///---------------------By Syafiq---------------------------^
    }
    ///---------------------By Syafiq (For recording of audio)---------------------------V
    private void runTimer(){
        handler=new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                int minutes=(seconds%3600)/60;
                int scds =seconds%60;
                String time =String.format(Locale.getDefault(), "%02d:%02d", minutes,scds);
                timer.setText(time);

                if (isrecording || (isplaying && playableseconds != -1)){
                    seconds++;
                    playableseconds--;

                    if (playableseconds ==-1 && isplaying){
                        player.stop();
                        player.release();
                        isplaying=false;
                        player=null;
                        player= new MediaPlayer();
                        playableseconds=dummyseconds;
                        seconds=0;
                        handler.removeCallbacksAndMessages(null);
                        return;
                    }
                }
                handler.postDelayed(this,1000);
            }
        });
    }
    public String CreateRandomAudioFileName(int string){
        StringBuilder stringBuilder = new StringBuilder( string );
        int i = 0 ;
        while(i < string ) {
            stringBuilder.append(RandomAudioFileName.charAt(random.nextInt(RandomAudioFileName.length())));

            i++ ;
        }
        return stringBuilder.toString();
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(recording.this, new
                String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, RequestPermissionCode);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[],
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length> 0) {
                    boolean StoragePermission = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] ==
                            PackageManager.PERMISSION_GRANTED;

                    if (StoragePermission && RecordPermission) {
                        Toast.makeText(recording.this, "Permission Granted",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(recording.this,"Permission Denied",
                                Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }
    private void MediarecorderReady() {
        recorder=new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(getRecordingFilePath());
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
    }

    private String getRecordingFilePath(){
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        File recordpath = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        recordfile = new File(recordpath, "AudioRecording" + CreateRandomAudioFileName(2) + ".mp3");
        try {
            if(!recordpath.isDirectory()) {
                recordpath.mkdirs();
            }
            recordfile.createNewFile();

        } catch(Exception e) {
            e.printStackTrace();
        }
        return recordfile.getAbsolutePath();
    }
    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(),
                WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(),
                RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED;
    }
    ///---------------------By Syafiq---------------------------^
}