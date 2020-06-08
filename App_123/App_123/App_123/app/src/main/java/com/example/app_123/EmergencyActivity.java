package com.example.app_123;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class EmergencyActivity extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    private static final int PERMISSION_CODE=1000;
    Uri image_uri;
    private static final int IMAGE_CAPTURE_CODE=1001;
    TextView take_photo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);

        drawerLayout=findViewById(R.id.drawer);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        actionBarDrawerToggle=new ActionBarDrawerToggle(EmergencyActivity.this,drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        NavigationView navigationView=findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);
        take_photo=findViewById(R.id.take_photo);
        TakePhoto();
    }

    private void TakePhoto() {
        take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //IF system >=marshmallow request permission
                if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.M){
                    if(checkSelfPermission(Manifest.permission.CAMERA)== PackageManager.PERMISSION_DENIED||checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_DENIED){
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
        image_uri=getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);

        //intent of camera
        Intent camerainten=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        camerainten.putExtra(MediaStore.EXTRA_OUTPUT,image_uri);
        startActivityForResult(camerainten,IMAGE_CAPTURE_CODE);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id= menuItem.getItemId();
        if(id==R.id.nav_home){
            startActivity(new Intent(EmergencyActivity.this,EmergencyActivity.class));
        }
        if(id==R.id.nav_report){
            startActivity(new Intent(EmergencyActivity.this,EmergencyActivity.class));
        }
        if(id==R.id.nav_contact){
            startActivity(new Intent(EmergencyActivity.this,EmergencyActivity.class));
        }
        if(id==R.id.nav_instruction){
            startActivity(new Intent(EmergencyActivity.this,EmergencyActivity.class));
        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
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
                    Toast.makeText(this, "Permission Denied..", Toast.LENGTH_SHORT).show();

                }
            }

        }
    }

}
