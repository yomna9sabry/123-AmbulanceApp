package com.example.app_123;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.getbase.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import adapter.AudioListAdapter;

public class RecordActivity extends AppCompatActivity {
    FloatingActionButton btn_record;
    Button btn_list;
    Chronometer Timer;
    TextView txt_record;
    ProgressBar progressBar;
    private  boolean mstartRecording=true;
    private  boolean mstoptRecording=true;
    long TimeWhenPaused=0;
    private  boolean isRecording=false;
    private  String recordpermission=Manifest.permission.RECORD_AUDIO;
    private int PERMISSION_CODE=21;
    private MediaRecorder mediaRecorder;
    private  String recordFile;
    private AudioListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        btn_record=findViewById(R.id.btn_record);
        btn_list=findViewById(R.id.btn_list);
        btn_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RecordActivity.this,RecordingListActivity.class));
            }
        });
        Timer=findViewById(R.id.timer);
      //  txt_record=findViewById(R.id.txt_record);
        progressBar=findViewById(R.id.progress);
        btn_record.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                if(isRecording){
                    //stop recording
                    stopRecording();
                    btn_record.setImageDrawable(getResources().getDrawable(R.drawable.ic_stop,null));
                    txt_record.setText(" Recording Stopped");
                    isRecording=false;
                }else {
                    //start recording
                    if (checkpermission()) {
                        startRecording();
                        btn_record.setImageDrawable(getResources().getDrawable(R.drawable.ic_mic, null));
                        txt_record.setText("Start Recording");
                        isRecording = true;

                    }
                }
            }
        });
    }

    private void startRecording() {
        Timer.setBase(SystemClock.elapsedRealtime());
        Timer.start();
        String recordPath=getApplicationContext().getExternalFilesDir("/").getAbsolutePath();
        SimpleDateFormat formater=new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss", Locale.CANADA);
        Date now=new Date();
        recordFile="Recording_"+formater.format(now)+".3gp";
        mediaRecorder=new MediaRecorder() ;
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setOutputFile(recordPath+"/"+recordFile);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            mediaRecorder.prepare();
        } catch (IOException e) {

        }
        mediaRecorder.start();
    }

    private void stopRecording() {
        Timer.stop();
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder=null;
    }

    private boolean checkpermission() {
        if(ActivityCompat.checkSelfPermission(getApplicationContext(), recordpermission)== PackageManager.PERMISSION_GRANTED){
            return  true;
        }else {
            ActivityCompat.requestPermissions(RecordActivity.this, new String[]{recordpermission},PERMISSION_CODE);
            return  false;
        }
    }
}
