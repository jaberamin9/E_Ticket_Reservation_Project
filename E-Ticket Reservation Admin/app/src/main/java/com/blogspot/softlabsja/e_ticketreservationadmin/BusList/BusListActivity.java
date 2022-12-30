package com.blogspot.softlabsja.e_ticketreservationadmin.BusList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.blogspot.softlabsja.e_ticketreservationadmin.BusList.AddUpdateBus.AddUpdateBusActivity;
import com.blogspot.softlabsja.e_ticketreservationadmin.Dashboard.DashboardActivity;
import com.blogspot.softlabsja.e_ticketreservationadmin.R;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BusListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<BusListModel> arrayList = new ArrayList<BusListModel>();
    ImageView backBtn,addBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_list);

        recyclerView = findViewById(R.id.recyclerView);
        backBtn = findViewById(R.id.backBtn);
        addBtn = findViewById(R.id.addBtn);

        BusList();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BusListActivity.this, AddUpdateBusActivity.class);
                intent.putExtra("title","Add");
                intent.putExtra("operation","addBus");
                startActivity(intent);
                finish();
            }
        });
    }

    void BusList() {
        SharedPreferences getIP = getSharedPreferences("IP", MODE_PRIVATE);
        String ipAddress = getIP.getString("ip", "");
        String BusListUrl = "http://"+ipAddress+"/E_TicketReservation/etiket/busList.php";
        arrayList.clear();
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BusListUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("error").equals("true")) {
                                Toast.makeText(BusListActivity.this, jsonObject.getString("message") + "", Toast.LENGTH_LONG).show();
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
                        BusListAdapter adapter = new BusListAdapter(arrayList, BusListActivity.this);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(BusListActivity.this));
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(BusListActivity.this, error + "", Toast.LENGTH_LONG).show();
            }
        }){
            @NotNull
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("orderBy", "bus_no");
                data.put("type", "asc");
                return data;
            }
        };
        queue.add(stringRequest);
    }
}