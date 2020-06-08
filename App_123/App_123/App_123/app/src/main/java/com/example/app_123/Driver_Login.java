package com.example.app_123;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Driver_Login extends AppCompatActivity  {
    EditText edit_username,edit_password;
    Button btn_login;
   TextView text_scan,text_username,text_password;
   String username,password;
   private String loginUrl;
    IntentIntegrator qrScan;
    AlertDialog.Builder alert;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver__login);
        // loginUrl = "http://192.168.1.106/test2.php";
        getSupportActionBar().hide();
        edit_username = findViewById(R.id.edit_username);
        edit_password = findViewById(R.id.edit_password);
        btn_login = findViewById(R.id.btn_login);
        text_username = findViewById(R.id.text_username);
        text_password = findViewById(R.id.text_password);
        text_scan = findViewById(R.id.scan);
        qrScan = new IntentIntegrator(this);
        alert = new AlertDialog.Builder(this);
        loginUrl = "http://10.0.2.2/test2.php";

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = edit_username.getText().toString();
                password = edit_password.getText().toString();

                if (username.equals("") && password.equals("")) {
                    alert.setTitle("خطأ !");
                    displayAlert("برجاء ادخال اسم مستخدم وكلمة مرور صحية");


                } else {


                     StringRequest request = new StringRequest(Request.Method.POST, loginUrl,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String s) {

                                    try {
                                        JSONArray jsonArray = new JSONArray(s);
                                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                                        String code = jsonObject.getString("code");
                                        if (code.equals("login_failed"))
                                        {
                                            //Toast.makeText(Driver_Login.this, "Error ", Toast.LENGTH_SHORT).show();
                                            alert.setTitle("Login Error.....");
                                            displayAlert(jsonObject.getString("message"));
                                        }
                                        else
                                        {
                                            Toast.makeText(Driver_Login.this, "success ", Toast.LENGTH_SHORT).show();
                                          Intent intent = new Intent(Driver_Login.this,Driver_Home.class);
                                            Bundle bundle = new Bundle();
                                            bundle.putString("name",jsonObject.getString("name"));
                                         // bundle.putString("password",jsonObject.getString("password"));
                                           intent.putExtras(bundle);
                                            startActivity(intent);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }




                                }

                            }, new Response.ErrorListener() {


                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Toast.makeText(Driver_Login.this, volleyError.toString() , Toast.LENGTH_SHORT).show();
                            //  Toast.makeText(Driver_Login.this, "خطأ في اسم المستخدم او كلمة المرور", Toast.LENGTH_SHORT).show();

                        }
                    })

                    {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> params = new HashMap<String, String>();
                            params.put("username",username);
                            params.put("password",password);

                            return params;
                        }
                    };
                     MySingletone.getInstance(Driver_Login.this).addToRequestQueue(request);

                    request.setRetryPolicy(new RetryPolicy() {
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
            }
        });

    }

public void displayAlert(String message)
{
    alert.setMessage(message);
    alert.setPositiveButton("ok", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            edit_username.setText("");
            edit_password.setText("");
        }
    });
    AlertDialog alertDialog = alert.create();
    alertDialog.show();
}
    }
