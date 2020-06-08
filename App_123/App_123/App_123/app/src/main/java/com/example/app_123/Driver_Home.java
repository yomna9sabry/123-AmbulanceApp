package com.example.app_123;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

public class Driver_Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;

  TextView text_location1,  text_location2,text_location3,text_location4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver__home);

        drawerLayout=findViewById(R.id.drawer);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        actionBarDrawerToggle=new ActionBarDrawerToggle(Driver_Home.this,drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        NavigationView navigationView=findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);
        text_location1=findViewById(R.id.location1);
        text_location1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Driver_Home.this,LocationActivity.class));
            }
        });
        text_location2=findViewById(R.id.location2);
        text_location2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Driver_Home.this,LocationActivity.class));
            }
        });
        text_location3=findViewById(R.id.location3);
        text_location3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Driver_Home.this,LocationActivity.class));
            }
        });
        text_location4=findViewById(R.id.location4);
        text_location4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Driver_Home.this,LocationActivity.class));
            }
        });

    }
    @Override

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.driver_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id=menuItem.getItemId();
        if(id==R.id.report){
            startActivity(new Intent(Driver_Home.this,Driver_Home2.class));
        }
        if(id==R.id.setting){
            startActivity(new Intent(Driver_Home.this,Driver_Home2.class));
        }
        if(id==R.id.contact_us){
            startActivity(new Intent(Driver_Home.this,Driver_Home2.class));
        }
        return true;
    }
}
