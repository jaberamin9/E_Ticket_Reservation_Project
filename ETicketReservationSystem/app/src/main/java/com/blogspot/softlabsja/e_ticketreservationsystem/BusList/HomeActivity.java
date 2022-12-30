package com.blogspot.softlabsja.e_ticketreservationsystem.BusList;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.blogspot.softlabsja.e_ticketreservationsystem.MainActivity;
import com.blogspot.softlabsja.e_ticketreservationsystem.R;
import com.blogspot.softlabsja.e_ticketreservationsystem.TicketHistory.TicketHistoryActivity;
import com.infideap.drawerbehavior.AdvanceDrawerLayout;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    String BusListUrl;
    RecyclerView recyclerView;
    ArrayList<BusListModel> arrayList = new ArrayList<BusListModel>();
    String email;
    public static final String LOGIN_DETAILS = "LoginDetails";
    SharedPreferences.Editor editor;
    TextView userName;

    AdvanceDrawerLayout drawerLayout;
    Toolbar toolbar;

    LinearLayout logout,ticketHistory,filter,expand;

    String orderBy = "bus_no",type = "asc";
    int f=0;
    ImageView upDown;
    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        SharedPreferences prefs = getSharedPreferences(LOGIN_DETAILS, MODE_PRIVATE);
        email = prefs.getString("email", "");

        recyclerView = findViewById(R.id.recyclerView);
        userName = findViewById(R.id.userName);
        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar_main);
        logout = findViewById(R.id.logout);
        ticketHistory = findViewById(R.id.ticketHistory);
        filter = findViewById(R.id.filter);
        upDown = findViewById(R.id.upDown);
        expand = findViewById(R.id.expand);
        radioGroup = findViewById(R.id.radioGroup);

        //------------------------------------------Drawer Layout-------------------------------------------
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        drawerLayout.setScrimColor(Color.TRANSPARENT);
        //navView.setNavigationItemSelectedListener(this);
        drawerLayout.useCustomBehavior(Gravity.START);
        drawerLayout.useCustomBehavior(Gravity.END);
        //--------------------------------------------------------------------------------------------------

        getLoginUserData();
        BusList();

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearSharedPreferences(HomeActivity.this);
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        ticketHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, TicketHistoryActivity.class);
                startActivity(intent);
            }
        });
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(f%2==0){
                    upDown.setImageResource(R.drawable.ic_down);
                    expand.setVisibility(View.VISIBLE);
                }else{
                    upDown.setImageResource(R.drawable.ic_up);
                    expand.setVisibility(View.GONE);
                }
                f++;
            }
        });


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = findViewById(checkedId);
                if(radioButton.getText().equals("Order By Bus ID - Asc")){
                    orderBy = "bus_no";
                    type = "asc";
                }else if(radioButton.getText().equals("Order By Bus ID - Desc")){
                    orderBy = "bus_no";
                    type = "desc";
                }else if(radioButton.getText().equals("Order By Price - Asc")){
                    orderBy = "t_price";
                    type = "asc";
                }else if(radioButton.getText().equals("Order By Price - Desc")){
                    orderBy = "t_price";
                    type = "desc";
                }else if(radioButton.getText().equals("Order By Seat Available - Asc")){
                    orderBy = "seat_available";
                    type = "asc";
                }else {
                    orderBy = "seat_available";
                    type = "desc";
                }
                BusList();
            }
        });
    }

    public static void clearSharedPreferences(Context ctx){
        File dir = new File(ctx.getFilesDir().getParent() + "/shared_prefs/");
        String[] children = dir.list();
        for (int i = 0; i < children.length; i++) {
            // clear each preference file
            if(!children[i].equals("IP.xml")) {
                ctx.getSharedPreferences(children[i].replace(".xml", ""), Context.MODE_PRIVATE).edit().clear().apply();
                //delete the file
                new File(dir, children[i]).delete();
            }
        }
    }

    void BusList() {
        SharedPreferences getIP = getSharedPreferences("IP", MODE_PRIVATE);
        String ipAddress = getIP.getString("ip", "");
        BusListUrl = "http://"+ipAddress+"/E_TicketReservation/etiket/busList.php";
        arrayList.clear();
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BusListUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("error").equals("true")) {
                                Toast.makeText(HomeActivity.this, jsonObject.getString("message") + "", Toast.LENGTH_LONG).show();
                            } else {

                                for (int i = 0;i<jsonObject.length();i++){
                                    JSONObject jsonObject1 = new JSONObject(jsonObject.getString(i+""));
                                    arrayList.add(new BusListModel(
                                            jsonObject1.getString("bus_no"),
                                            jsonObject1.getString("starting_point"),
                                            jsonObject1.getString("ending_point"),
                                            jsonObject1.getString("starting_time"),
                                            jsonObject1.getString("arrival_time"),
                                            jsonObject1.getString("seat_available"),
                                            jsonObject1.getString("t_price"),
                                            jsonObject1.getString("row")));
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                        BusListAdapter adapter = new BusListAdapter(arrayList, HomeActivity.this);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(HomeActivity.this, error + "", Toast.LENGTH_LONG).show();
            }
        }){
            @NotNull
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("orderBy", orderBy);
                data.put("type", type);
                return data;
            }
        };
        queue.add(stringRequest);
    }

    void getLoginUserData(){
        SharedPreferences getIP = getSharedPreferences("IP", MODE_PRIVATE);
        String ipAddress = getIP.getString("ip", "");
        String LoginUserDataUrl = "http://"+ipAddress+"/E_TicketReservation/etiket/LoginUserData.php";
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, LoginUserDataUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("error").equals("true")) {
                                Toast.makeText(HomeActivity.this, jsonObject.getString("message") + "", Toast.LENGTH_LONG).show();
                            } else {
                                editor = getSharedPreferences(LOGIN_DETAILS, MODE_PRIVATE).edit();
                                editor.putString("id", jsonObject.getJSONArray("info").get(0) + "");
                                editor.putString("username", jsonObject.getJSONArray("info").get(1) + "");
                                editor.apply();
                                userName.setText("Hello "+jsonObject.getJSONArray("info").get(1));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            System.out.println(e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(HomeActivity.this, error + "", Toast.LENGTH_LONG).show();
            }
        }){
            @NotNull
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("email", email);
                return data;
            }
        };
        queue.add(stringRequest);
    }
}