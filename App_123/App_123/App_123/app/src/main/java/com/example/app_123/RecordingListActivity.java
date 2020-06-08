package com.example.app_123;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.IOException;

import adapter.AudioListAdapter;

public class RecordingListActivity extends AppCompatActivity implements AudioListAdapter.OnItemClickListener {
    RecyclerView audio_list;
    private File[]allFiles;
    private AudioListAdapter adapter;
    private  boolean isPlaying=false;
    private MediaPlayer mediaPlayer=null;
    private  File fileToPlay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording_list);
        audio_list=findViewById(R.id.audio_list);
        String path=getApplicationContext().getExternalFilesDir("/").getAbsolutePath();
        File directory=new File(path);
        allFiles=directory.listFiles();
        adapter=new AudioListAdapter(RecordingListActivity.this,allFiles,this);
        audio_list.setHasFixedSize(true);
        audio_list.setLayoutManager(new LinearLayoutManager(this));
        audio_list.setAdapter(adapter);
    }

    @Override
    public void mClickListener(File file, int position) {
        if(isPlaying){
            stopAudio();
            isPlaying=false;
            playAudio(fileToPlay);
        }else {
            fileToPlay=file;
            playAudio(fileToPlay);
            isPlaying=true;
        }
    }
    private void stopAudio() {
        isPlaying=true;

    }

    private void playAudio(File fileToPlay) {
        mediaPlayer=new MediaPlayer();
        try {
            mediaPlayer.setDataSource(fileToPlay.getAbsolutePath());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        isPlaying=true;
    }
}
