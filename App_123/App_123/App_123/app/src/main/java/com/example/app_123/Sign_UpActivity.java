package com.example.app_123;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Sign_UpActivity extends AppCompatActivity {
    EditText UsernameEditText,phoneEditText,nationalIdEditText;
    Button btn_submit,btn;
    ImageView image_upload;
    private static final int PERMISSION_CODE=1000;
    Uri image_uri;
    private static final int IMAGE_CAPTURE_CODE=1001;
    Integer REQUEST_CAMERA=1, SELECT_FILE=0;
    String register_url = "http://10.0.2.2/upload.php";
     Bitmap bmp;
    SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign__up);
        getSupportActionBar().hide();
        preferences = getSharedPreferences("myfile",MODE_PRIVATE);
        UsernameEditText=findViewById(R.id.edit_user);
        phoneEditText=findViewById(R.id.edit_phone);
        nationalIdEditText=findViewById(R.id.edit_national_id);
        btn= findViewById(R.id.button2);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = UsernameEditText.getText().toString();
                String phone = phoneEditText.getText().toString();
                String nationalId = nationalIdEditText.getText().toString();
                String imageData = imageToString(bmp);
               // Toast.makeText(Sign_UpActivity.this, ima, Toast.LENGTH_SHORT).show();

                //Toast.makeText(Sign_UpActivity.this, "user name = " +username + "phone = "+ phone + "id =" +nationalId +"image = " + imageData, Toast.LENGTH_SHORT).show();
            }
        });

        image_upload=findViewById(R.id.upload_img);
        image_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
            }
        });


        btn_submit=findViewById(R.id.btn_signup);
            btn_submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(UsernameEditText.getText().toString().equals("")){
                        UsernameEditText.setError("الرجاء ادخال اسم مناسب");
                    }
                    if(phoneEditText.getText().toString().equals("")){
                        phoneEditText.setError("الرجاء ادخال رقم هاتف مناسب");
                    }
                    if (nationalIdEditText.getText().toString().equals("")){
                        nationalIdEditText.setError("الرجاء ادخال رقم قومي صحيح");
                    }
                    else {

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, register_url,
                                new Response.Listener<String>() {
                                    @Override

                                    public void onResponse(String response) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            String Success = jsonObject.getString("success");
                                            if (Success.equals(1))
                                            {
                                                Toast.makeText(Sign_UpActivity.this, "Register Success", Toast.LENGTH_LONG).show();
                                            }

                                            Toast.makeText(Sign_UpActivity.this, response, Toast.LENGTH_LONG).show();
                                        } catch (JSONException e) {
                                            Toast.makeText(Sign_UpActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                            e.printStackTrace();
                                        }


                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(Sign_UpActivity.this, error.toString(), Toast.LENGTH_LONG).show();

                            }
                        }){
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String,String> params = new HashMap<String, String>();
                                 String username = UsernameEditText.getText().toString();
                               String phone = phoneEditText.getText().toString();
                               String nationalId = nationalIdEditText.getText().toString();
                                String imageData = imageToString(bmp);

                                params.put("name",username);
                                params.put("phone",phone);
                                params.put("id",nationalId);
                                params.put("id_photo",imageData);
                                save_id(nationalId);
                                return params;
                            }
                        };
                        MySingletone.getInstance(Sign_UpActivity.this).addToRequestQueue(stringRequest);
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
                                startActivity(new Intent(Sign_UpActivity.this,UserHomeActivity.class));
                    }

                }
            });

    }
    private void SelectImage() {

        final CharSequence[] items = {"Camera", "Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(Sign_UpActivity.this);
        builder.setTitle("Add Image");

        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (items[i].equals("Camera")) {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);

                } else if (items[i].equals("Gallery")) {

                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    //startActivityForResult(intent.createChooser(intent, "Select File"), SELECT_FILE);
                    startActivityForResult(intent,SELECT_FILE);

                } else if (items[i].equals("Cancel")) {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();
    }
    public  void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode,data);

        if(resultCode== Activity.RESULT_OK){

            if(requestCode==REQUEST_CAMERA){

                Bundle bundle = data.getExtras();
                bmp = (Bitmap) bundle.get("data");
                image_upload.setImageBitmap(bmp);

            }else if(requestCode==SELECT_FILE){
                Uri selectedImageUri = data.getData();
                try {
                    bmp = MediaStore.Images.Media.getBitmap(getContentResolver(),selectedImageUri);
                    image_upload.setImageBitmap(bmp);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
    private String imageToString(Bitmap bitmap)
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
        byte[] imageBytes = outputStream.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes,Base64.DEFAULT);
        return encodedImage;
    }
    public  void  save_id(String id){
        preferences.edit().putString("ID",id).apply();
    }
}
