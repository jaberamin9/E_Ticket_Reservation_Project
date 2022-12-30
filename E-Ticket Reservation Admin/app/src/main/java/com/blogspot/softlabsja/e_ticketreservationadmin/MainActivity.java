package com.blogspot.softlabsja.e_ticketreservationadmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.widget.ContentLoadingProgressBar;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.blogspot.softlabsja.e_ticketreservationadmin.Dashboard.DashboardActivity;
import com.wang.avi.AVLoadingIndicatorView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText email, pass;
    Button loginBtn;
    TextView changeIp,createDB;
    ContentLoadingProgressBar loadingBtn;
    AVLoadingIndicatorView loading;
    LinearLayout login_box;

    public static final String IP = "IP";
    SharedPreferences.Editor ipEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = findViewById(R.id.email);
        pass = findViewById(R.id.pass);
        loginBtn = findViewById(R.id.loginBtn);
        loadingBtn = findViewById(R.id.loadingBtn);
        loading = findViewById(R.id.loading);
        login_box = findViewById(R.id.login_box);
        changeIp = findViewById(R.id.changeIp);
        createDB = findViewById(R.id.createDB);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.backgroundColor2));
        }

        if (loadingBtn != null) {
            loadingBtn.setIndeterminate(true);
            loadingBtn.getIndeterminateDrawable().setColorFilter(0xCDC51162, android.graphics.PorterDuff.Mode.MULTIPLY);
        }


        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                loading.hide();
                login_box.setVisibility(View.VISIBLE);

                loginBtn.setEnabled(true);
                loginBtn.setTextColor(Color.parseColor("#FFFFFFFF"));
                loginBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        loadingBtn.show();
                        loginBtn.setTextColor(Color.parseColor("#8effffff"));

                        if (email.getText().toString().trim().equalsIgnoreCase("")) {
                            Toast.makeText(getApplicationContext(), "Enter Your Email", Toast.LENGTH_SHORT).show();
                            loadingBtn.hide();
                            loginBtn.setTextColor(Color.parseColor("#FFFFFFFF"));
                        } else if (pass.getText().toString().trim().equalsIgnoreCase("")) {
                            Toast.makeText(getApplicationContext(), "Enter Your Password", Toast.LENGTH_SHORT).show();
                            loadingBtn.hide();
                            loginBtn.setTextColor(Color.parseColor("#FFFFFFFF"));
                        } else {
                            loginBtn.setEnabled(false);
                            login(email.getText().toString().trim(), pass.getText().toString().trim());
                        }
                    }
                });


            }
        }, 2000);


        changeIp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangeIP();
            }
        });
        createDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateDB();
            }
        });
    }

    void login(String email, String pass) {
        SharedPreferences getIP = getSharedPreferences(IP, MODE_PRIVATE);
        String ipAddress = getIP.getString("ip", "");
        String loginUrl = "http://"+ipAddress+"/E_TicketReservation/admin/adminLogin.php";
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, loginUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("error").equals("true")) {
                                loginBtn.setEnabled(true);
                                loadingBtn.hide();
                                loginBtn.setTextColor(Color.parseColor("#FFFFFFFF"));
                                Toast.makeText(MainActivity.this, jsonObject.getString("message") + "", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(MainActivity.this, jsonObject.getString("message") + "", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loginBtn.setEnabled(true);
                loadingBtn.hide();
                loginBtn.setTextColor(Color.parseColor("#FFFFFFFF"));
                Toast.makeText(MainActivity.this, error + "", Toast.LENGTH_LONG).show();
            }
        }) {
            @NotNull
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("email", email);
                data.put("password", pass);
                return data;
            }
        };
        queue.add(stringRequest);
    }


    private void ChangeIP() {

        Dialog dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.change_ip);
        dialog.setCanceledOnTouchOutside(true);

        EditText ipAd = dialog.findViewById(R.id.ipAd);
        AppCompatButton saveBtn = dialog.findViewById(R.id.saveBtn);

        SharedPreferences getIP = getSharedPreferences(IP, MODE_PRIVATE);
        String ipAddress = getIP.getString("ip", "");
        if(!ipAddress.equals("")){
            ipAd.setText(ipAddress);
        }
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ipEditor = getSharedPreferences(IP, MODE_PRIVATE).edit();
                ipEditor.putString("ip", ipAd.getText().toString().trim());
                ipEditor.apply();
                dialog.dismiss();
            }
        });

        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();

    }

    void CreateDB() {
        SharedPreferences getIP = getSharedPreferences(IP, MODE_PRIVATE);
        String ipAddress = getIP.getString("ip", "");
        String createDBUrl = "http://"+ipAddress+"/E_TicketReservation/admin/createDBandTable.php";
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, createDBUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Toast.makeText(MainActivity.this, jsonObject.getString("message") + "", Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, e + "", Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error + "", Toast.LENGTH_LONG).show();
            }
        });
        queue.add(stringRequest);
    }
}