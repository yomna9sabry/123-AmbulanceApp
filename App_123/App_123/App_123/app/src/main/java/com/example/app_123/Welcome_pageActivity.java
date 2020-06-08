package com.example.app_123;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class Welcome_pageActivity extends AppCompatActivity {
    Button btn_register;
    Button btn_submit;
    ImageView image_driver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);
        getSupportActionBar().hide();
        image_driver=findViewById(R.id.driver);
        image_driver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Welcome_pageActivity.this,Driver_Login.class));
            }
        });
        Button btn_dial=findViewById(R.id.btn_call);
        btn_dial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent call_intent=new Intent(Intent.ACTION_CALL);
                call_intent.setData(Uri.parse("tel:123"));
                if(ActivityCompat.checkSelfPermission(Welcome_pageActivity.this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
                    return;
                }
                startActivity(call_intent);

            }
        });
        btn_register=findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Welcome_pageActivity.this,Sign_UpActivity.class));
            }
        });
        btn_submit=findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Welcome_pageActivity.this,EmergencyActivity.class));
            }
        });
    }




}
