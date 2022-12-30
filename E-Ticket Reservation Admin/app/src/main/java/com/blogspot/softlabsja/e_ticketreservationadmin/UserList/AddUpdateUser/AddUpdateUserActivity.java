package com.blogspot.softlabsja.e_ticketreservationadmin.UserList.AddUpdateUser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.blogspot.softlabsja.e_ticketreservationadmin.BusList.AddUpdateBus.AddUpdateBusActivity;
import com.blogspot.softlabsja.e_ticketreservationadmin.BusList.BusListActivity;
import com.blogspot.softlabsja.e_ticketreservationadmin.R;
import com.blogspot.softlabsja.e_ticketreservationadmin.UserList.UserListActivity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddUpdateUserActivity extends AppCompatActivity {
    TextView title;
    ImageView backBtn;
    String toolbarTitle, operation;
    EditText userNameEtx,emailEtx,passwordEtx;
    AppCompatButton addBtn;
    String getID,getUserName,getEmail,getPassword;
    int N = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_update_user);

        toolbarTitle = getIntent().getStringExtra("title");
        operation = getIntent().getStringExtra("operation");
        if(toolbarTitle.equals("Update")){
            try {
                N = 1;
                getID = getIntent().getStringExtra("id");
                getUserName = getIntent().getStringExtra("userName");
                getEmail = getIntent().getStringExtra("email");
                getPassword = getIntent().getStringExtra("password");
            }catch (Exception ignored){
            }
        }

        title = findViewById(R.id.title);
        backBtn = findViewById(R.id.backBtn);


        addBtn = findViewById(R.id.addBtn);
        userNameEtx = findViewById(R.id.userNameEtx);
        emailEtx = findViewById(R.id.emailEtx);
        passwordEtx = findViewById(R.id.passwordEtx);

        title.setText(toolbarTitle);

        if(N == 1){
            userNameEtx.setText(getUserName);
            emailEtx.setText(getEmail);
            passwordEtx.setText(getPassword);
        }


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddUpdateUserActivity.this, UserListActivity.class);
                startActivity(intent);
                finish();
            }
        });


        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (userNameEtx.getText().toString().trim().equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), " Enter User Name", Toast.LENGTH_SHORT).show();
                } else if (emailEtx.getText().toString().trim().equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Enter Email", Toast.LENGTH_SHORT).show();
                } else if (passwordEtx.getText().toString().trim().equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Enter Password", Toast.LENGTH_SHORT).show();
                } else {
                    submit(N);
                }
            }
        });
    }

    private void submit(int n){
        SharedPreferences getIP = getSharedPreferences("IP", MODE_PRIVATE);
        String ipAddress = getIP.getString("ip", "");
        String BusListUrl = "http://"+ ipAddress +"/E_TicketReservation/admin/"+ operation +".php";
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BusListUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("error").equals("true")) {
                                Toast.makeText(AddUpdateUserActivity.this, jsonObject.getString("message") + "", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(AddUpdateUserActivity.this, jsonObject.getString("message") + "", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(AddUpdateUserActivity.this, UserListActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(AddUpdateUserActivity.this, e+"", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddUpdateUserActivity.this, error + "", Toast.LENGTH_LONG).show();
            }
        }){
            @NotNull
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                if(n == 1){
                    data.put("id", getID);
                    data.put("username", userNameEtx.getText().toString().trim());
                    data.put("email", emailEtx.getText().toString().trim());
                    data.put("password", passwordEtx.getText().toString().trim());
                }else{
                    data.put("username", userNameEtx.getText().toString().trim());
                    data.put("email", emailEtx.getText().toString().trim());
                    data.put("password", passwordEtx.getText().toString().trim());
                }
                return data;
            }
        };
        queue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AddUpdateUserActivity.this, UserListActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }
}