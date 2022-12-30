package com.blogspot.softlabsja.e_ticketreservationsystem.TicketHistory;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.blogspot.softlabsja.e_ticketreservationsystem.BusList.BusListAdapter;
import com.blogspot.softlabsja.e_ticketreservationsystem.BusList.BusListModel;
import com.blogspot.softlabsja.e_ticketreservationsystem.BusList.HomeActivity;
import com.blogspot.softlabsja.e_ticketreservationsystem.R;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TicketHistoryActivity extends AppCompatActivity {
    String BookedHistoryByUserUrl;
    RecyclerView recyclerView;
    ArrayList<TicketHistoryModel> arrayList = new ArrayList<TicketHistoryModel>();
    String p_id;
    public static final String LOGIN_DETAILS = "LoginDetails";
    ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_history);



        SharedPreferences prefs = getSharedPreferences(LOGIN_DETAILS, MODE_PRIVATE);
        p_id = prefs.getString("id", "");

        recyclerView = findViewById(R.id.recyclerView);
        backBtn = findViewById(R.id.backBtn);

        getBookedHistoryByUser();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    void getBookedHistoryByUser() {
        SharedPreferences getIP = getSharedPreferences("IP", MODE_PRIVATE);
        String ipAddress = getIP.getString("ip", "");
        BookedHistoryByUserUrl = "http://"+ipAddress+"/E_TicketReservation/etiket/BookedHistoryByUser.php";
        arrayList.clear();
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BookedHistoryByUserUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("error").equals("true")) {
                                Toast.makeText(TicketHistoryActivity.this, jsonObject.getString("message") + "", Toast.LENGTH_LONG).show();
                            } else {
                                for (int i = 0;i<jsonObject.length();i++){
                                    JSONObject jsonObject1 = new JSONObject(jsonObject.getString(i+""));
                                    arrayList.add(new TicketHistoryModel(
                                            jsonObject1.getString("bus_no"),
                                            jsonObject1.getString("starting_point"),
                                            jsonObject1.getString("ending_point"),
                                            jsonObject1.getString("t_price"),
                                            jsonObject1.getString("seat"),
                                            jsonObject1.getString("booked_date"),
                                            jsonObject1.getString("validity")));
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                        TicketHistoryAdapter adapter = new TicketHistoryAdapter(arrayList, TicketHistoryActivity.this);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(TicketHistoryActivity.this));
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(TicketHistoryActivity.this, error + "", Toast.LENGTH_LONG).show();
            }
        }){
            @NotNull
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("p_id", p_id);
                return data;
            }
        };
        queue.add(stringRequest);
    }
}