package com.example.app_123.ui.home;
import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.app_123.R;
import com.example.app_123.RecordActivity;

import java.io.IOException;


public class HomeFragment extends Fragment    {
    Button btn_record, btn_stop, btn_play;

    TextView take_photo;
    private boolean isRecording = false;
    private static String audioFilePath;

    private static MediaRecorder mediaRecorder;
    private static MediaPlayer mediaPlayer;

    private static final int PERMISSION_CODE=1000;
    Uri image_uri;
    private static final int IMAGE_CAPTURE_CODE=1001;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        take_photo=root.findViewById(R.id.take_photo);
        btn_record=root.findViewById(R.id.btn_record2);
        btn_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(getActivity(), RecordActivity.class));
            }
        });
       // btn_play=root.findViewById(R.id.btn_play2);
      //  btn_stop=root.findViewById(R.id.btn_stop2);
        /*
        if (!hasMicrophone())
        {
            btn_stop.setEnabled(false);
            btn_play.setEnabled(false);
            btn_record.setEnabled(false);
        }

         else {
            btn_play.setEnabled(false);
            btn_stop.setEnabled(false);
        }


         */
        /*
        audioFilePath =
                Environment.getExternalStorageDirectory().getAbsolutePath()
                        + "/myaudio.3gp";

         */
        /*
        btn_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)  {
                isRecording = true;
                btn_stop.setEnabled(true);
                btn_play.setEnabled(false);
                btn_record.setEnabled(false);

                try {
                    mediaRecorder = new MediaRecorder();
                    mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                    mediaRecorder.setOutputFile(audioFilePath);
                    mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                    mediaRecorder.prepare();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {

                    mediaRecorder.start();
                }

            }
        });

         */
        /*
        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btn_stop.setEnabled(false);
                btn_play.setEnabled(true);

                if (isRecording)
                {
                    btn_record.setEnabled(false);
                    mediaRecorder.stop();
                    mediaRecorder.release();
                    mediaRecorder = null;
                    isRecording = false;
                } else {
                    mediaPlayer.release();
                    mediaPlayer = null;
                    btn_record.setEnabled(true);
                }
            }
        });

         */
        /*
        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_play.setEnabled(false);
                btn_record.setEnabled(false);
                btn_stop.setEnabled(true);

                mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(audioFilePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mediaPlayer.start();
            }
        });

         */

        takephoto(root);
        return root;
    }



/*
    protected  boolean  hasMicrophone(){
        PackageManager pmanager = getActivity().getPackageManager();
        return pmanager.hasSystemFeature(
                PackageManager.FEATURE_MICROPHONE);
    }

 */



    public void takephoto(View root) {
        take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //IF system >=marshmallow request permission
                if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.M){
                    if(getActivity().checkSelfPermission(Manifest.permission.CAMERA)== PackageManager.PERMISSION_DENIED||getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_DENIED){
                        String []permission={Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permission,PERMISSION_CODE);
                    }else {
                        //permission granted
                        opencamera();
                    }

                }else {
                    //system os<marshmallow
                    opencamera();
                }
            }
        });
            }

    private void opencamera() {
        ContentValues values=new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"New picture");
        values.put(MediaStore.Images.Media.DESCRIPTION,"From the camera");
        image_uri=getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);

        //intent of camera
        Intent camerainten=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        camerainten.putExtra(MediaStore.EXTRA_OUTPUT,image_uri);
        startActivityForResult(camerainten,IMAGE_CAPTURE_CODE);
    }
    //handle permission


    @Override

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case  PERMISSION_CODE:{
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    //permission from popup was granted
                    opencamera();
                }else {
                    //permission from popup was denied
                    Toast.makeText(getActivity(), "Permission Denied..", Toast.LENGTH_SHORT).show();

                }
            }

        }
    }



    }

