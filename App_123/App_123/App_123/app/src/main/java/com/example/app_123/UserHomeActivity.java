package com.example.app_123;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.os.Looper;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import adapter.AudioListAdapter;

public class UserHomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener , AudioListAdapter.OnItemClickListener {
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Button btn_send,btn_record, btn_stop, btn_play, btn_loc;
    protected Context context;
    String lat;
    String provider;
    protected boolean gps_enabled, network_enabled;
    TextView take_photo, longtudeTextView, latitudeTextView;
    String requestUrl = "http://192.168.1.107/send_request.php";
    Bitmap bmp;
    private static final int PERMISSION_CODE = 1000;
    ImageView image_upload;
    private static final int IMAGE_CAPTURE_CODE = 1001;
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;

    Button btn_list;
    Chronometer Timer;
    TextView txt_record;
    ProgressBar progressBar;
    private  boolean mstartRecording=true;
    private  boolean mstoptRecording=true;
    long TimeWhenPaused=0;
    private  boolean isRecording=false;
    private  String recordpermission=Manifest.permission.RECORD_AUDIO;
    private int PERMISSION_CODE_RECORD=21;
    private MediaRecorder mediaRecorder;
    private  String recordFile;
    private AudioListAdapter adapter;
    RecyclerView audio_list;
    private File[]allFiles;
    String recordPath;
    File directory;

    private  boolean isPlaying=false;
    private MediaPlayer mediaPlayer=null;
    private  File fileToPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
        getCurrentLocation();
        drawerLayout = findViewById(R.id.drawer);
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("myfile",MODE_PRIVATE);
        final String id = preferences.getString("ID","0");
//        allFiles =directory.listFiles();
        getSupportActionBar().setDisplayShowTitleEnabled(false); getSupportActionBar().setDisplayShowTitleEnabled(false); getSupportActionBar().setDisplayHomeAsUpEnabled(true); getSupportActionBar().setHomeButtonEnabled(true);
        actionBarDrawerToggle = new ActionBarDrawerToggle(UserHomeActivity.this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        NavigationView navigationView = findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);
        longtudeTextView = findViewById(R.id.text_view_longitude);
        latitudeTextView = findViewById(R.id.text_view_latitude);
        take_photo = findViewById(R.id.take_photo); btn_record = findViewById(R.id.btn_record2);
        Timer=findViewById(R.id.timer); txt_record=findViewById(R.id.txt_record); progressBar=findViewById(R.id.progress);
        image_upload = findViewById(R.id.imageView);
        btn_send = findViewById(R.id.btn_send);
        audio_list=findViewById(R.id.audio_list);
        String path=getApplicationContext().getExternalFilesDir("/").getAbsolutePath();
        File directory=new File(path);
        allFiles=directory.listFiles();
        adapter=new AudioListAdapter(UserHomeActivity.this,allFiles,this);
        audio_list.setHasFixedSize(true);
        audio_list.setLayoutManager(new LinearLayoutManager(this));
        audio_list.setAdapter(adapter);
        btn_record.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)

            @Override
            public void onClick(View v) {

                if(isRecording){
                    //stop recording
                    stopRecording();
                    //btn_record.setDr(getResources().getDrawable(R.drawable.ic_stop,null));
                    txt_record.setText(" Recording Stopped");
                    isRecording=false;
                }else {
                    //start recording
                    if (checkpermission()) {
                        startRecording();
                        //  btn_record.setImageDrawable(getResources().getDrawable(R.drawable.ic_mic, null));
                        txt_record.setText("Start Recording");
                        isRecording = true;

                    }
                }
            }
        });

        takephoto();
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StringRequest stringRequest = new StringRequest(Request.Method.POST, requestUrl,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try { JSONArray jsonArray = new JSONArray(response);
                                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                                    String Success = jsonObject.getString("result");
                                    if (Success.equals(1))
                                    { Toast.makeText(UserHomeActivity.this, "Request Sent", Toast.LENGTH_LONG).show(); }
                                    else

                                    {
                                         //Toast.makeText(context, "Request doesn't sent", Toast.LENGTH_SHORT).show();
                                        Toast.makeText(UserHomeActivity.this, response, Toast.LENGTH_SHORT).show();

                                    }

                                } catch (JSONException e)
                                { Toast.makeText(UserHomeActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                    e.printStackTrace(); }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(UserHomeActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();

                        String longitude = longtudeTextView.getText().toString();
                        String latitude = latitudeTextView.getText().toString();
                        String imageData = imageToString(bmp);
                     //  String audioData  = encodeAudio(recordPath);

                       params.put("sender_id",id );
                    //   params.put("record",audioData );
                       params.put("image", imageData);
                       params.put("origin_latitude", latitude);
                       params.put("origin_longitude", longitude);

                        return params;
                    }
                };
                MySingletone.getInstance(UserHomeActivity.this).addToRequestQueue(stringRequest);
                stringRequest.setRetryPolicy(new RetryPolicy() {
                    @Override
                    public int getCurrentTimeout() {
                        return 30000;
                    }

                    @Override
                    public int getCurrentRetryCount() {
                        return 30000;
                    }

                    @Override
                    public void retry(VolleyError error) throws VolleyError {

                    }
                });


            }
        });

    }

    private boolean checkpermission() {
        if(ActivityCompat.checkSelfPermission(getApplicationContext(), recordpermission)== PackageManager.PERMISSION_GRANTED){
            return  true;
        }else {
            ActivityCompat.requestPermissions(UserHomeActivity.this, new String[]{recordpermission},PERMISSION_CODE_RECORD);
            return  false;
        }
    }

    private void startRecording() {
        Timer.setBase(SystemClock.elapsedRealtime());
        Timer.start();
        recordPath=getApplicationContext().getExternalFilesDir("/").getAbsolutePath();

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

    private void getCurrentLocation() {
        final LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationServices.getFusedLocationProviderClient(UserHomeActivity.this).requestLocationUpdates(locationRequest,
                new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        LocationServices.getFusedLocationProviderClient(UserHomeActivity.this)
                                .removeLocationUpdates(this);
                        if (locationResult != null && locationResult.getLocations().size() > 0) {
                            int latestLocationIndex = locationResult.getLocations().size() - 1;
                            double latitude =
                                    locationResult.getLocations().get(latestLocationIndex).getLatitude();
                            double longitude =
                                    locationResult.getLocations().get(latestLocationIndex).getLongitude();

                            latitudeTextView.setText(latitude + "");
                            longtudeTextView.setText((longitude + ""));
                        }
                        super.onLocationResult(locationResult);

                    }
                }, Looper.getMainLooper());

    }

    private void takephoto() {
        take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //IF system >=marshmallow request permission
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (getApplicationContext().checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED || getApplicationContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permission, PERMISSION_CODE);
                    } else {
                        //permission granted
                        opencamera();
                    }

                } else {
                    //system os<marshmallow
                    opencamera();
                }
            }
        });
    }

    private void opencamera() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,IMAGE_CAPTURE_CODE); }

    public  void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == IMAGE_CAPTURE_CODE) {

                Bundle bundle = data.getExtras();
                bmp = (Bitmap) bundle.get("data");
                image_upload.setImageBitmap(bmp);
            }
        }
    }
    @Override

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (id == R.id.nav_home) {
            startActivity(new Intent(UserHomeActivity.this, UserHomeActivity.class));
        }
        if (id == R.id.nav_report) {
            startActivity(new Intent(UserHomeActivity.this, UserHomeActivity.class));
        }
        if (id == R.id.nav_contact) {
            startActivity(new Intent(UserHomeActivity.this, UserHomeActivity.class));
        }
        if (id == R.id.nav_instruction) {
            startActivity(new Intent(UserHomeActivity.this, UserHomeActivity.class));
        }
        return true;
    }
    //handle permission
    public String encodeAudio(String selectedPath) {



        File file = new File(Environment.getExternalStorageDirectory() + "/hello-4.wav");
        byte[] bytes = new byte[(int) file.length()];

        String encoded = Base64.encodeToString(bytes, Base64.DEFAULT);


        return encoded;

    }



    private String imageToString(Bitmap bitmap)
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
        byte[] imageBytes = outputStream.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes,Base64.DEFAULT);
        return encodedImage;
    }
    @Override

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permission from popup was granted
                    opencamera();
                } else {
                    //permission from popup was denied
                    Toast.makeText(getApplicationContext(), "Permission Denied..", Toast.LENGTH_SHORT).show();

                }
            }

        }
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
