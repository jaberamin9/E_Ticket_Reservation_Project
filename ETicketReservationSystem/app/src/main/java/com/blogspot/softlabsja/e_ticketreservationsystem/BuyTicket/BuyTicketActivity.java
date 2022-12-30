package com.blogspot.softlabsja.e_ticketreservationsystem.BuyTicket;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
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

import static androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE;

public class BuyTicketActivity extends AppCompatActivity {

    String BookedSeatUrl;
    RecyclerView recyclerView;
    ArrayList<BuyTicketModel> arrayList = new ArrayList<BuyTicketModel>();
    ArrayList<String> bookedList = new ArrayList<String>();
    ArrayList<String> MyBookedList = new ArrayList<String>();
    String bus_no,p_id,stPoint_i,enPoint_i,date_i,time_i,tPrice_i;
    int len;
    Button buyTicket;
    public static final String LOGIN_DETAILS = "LoginDetails";
    SharedPreferences.Editor editor;

    TextView stPoint,enPoint,date,time,bus_id,tPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_ticket);


        SharedPreferences prefs = getSharedPreferences(LOGIN_DETAILS, MODE_PRIVATE);
        p_id = prefs.getString("id", "");
        buyTicket = findViewById(R.id.buyTicket);

        stPoint = findViewById(R.id.stPoint);
        enPoint = findViewById(R.id.enPoint);
        date = findViewById(R.id.date);
        time = findViewById(R.id.time);
        bus_id = findViewById(R.id.bus_id);
        tPrice = findViewById(R.id.tPrice);


        try {
            len = Integer.parseInt(getIntent().getStringExtra("row"));
            bus_no = getIntent().getStringExtra("bus_no");

            stPoint_i = getIntent().getStringExtra("st_point");
            enPoint_i = getIntent().getStringExtra("en_point");
            date_i = getIntent().getStringExtra("date");
            time_i = getIntent().getStringExtra("time");
            tPrice_i = getIntent().getStringExtra("tPrice");

            if(!bus_no.equals("")) {
                editor = getSharedPreferences(LOGIN_DETAILS, MODE_PRIVATE).edit();
                editor.putString("len", getIntent().getStringExtra("row"));
                editor.putString("bus_no", bus_no);

                editor.putString("st_point", stPoint_i);
                editor.putString("en_point", enPoint_i);
                editor.putString("date", date_i);
                editor.putString("time", time_i);
                editor.putString("tPrice", tPrice_i);
                editor.apply();
            }
        }catch (Exception ignored){
        }

        if(bus_no==null){
            SharedPreferences prefs2 = getSharedPreferences(LOGIN_DETAILS, MODE_PRIVATE);
            len = Integer.parseInt(prefs2.getString("len", ""));
            bus_no = prefs2.getString("bus_no", "");

            stPoint_i = prefs2.getString("st_point","");
            enPoint_i = prefs2.getString("en_point","");
            date_i = prefs2.getString("date","");
            time_i = prefs2.getString("time","");
            tPrice_i = prefs2.getString("tPrice","");
        }


        bus_id.setText("#"+bus_no);
        stPoint.setText(stPoint_i);
        enPoint.setText(enPoint_i);
        date.setText(date_i);
        time.setText(time_i);
        tPrice.setText("$"+tPrice_i);

        recyclerView = findViewById(R.id.recyclerView);
        getMyBookedSeat();
        getBookedSeat();
    }
    void getMyBookedSeat() {
        SharedPreferences getIP = getSharedPreferences("IP", MODE_PRIVATE);
        String ipAddress = getIP.getString("ip", "");
        String MyBookedSeatUrl = "http://"+ipAddress+"/E_TicketReservation/etiket/userBookedSeat.php";
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MyBookedSeatUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            for (int i = 0; i < jsonObject.getJSONArray("info").length(); i++) {
                                MyBookedList.add(jsonObject.getJSONArray("info").get(i) + "");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(BuyTicketActivity.this, error + "", Toast.LENGTH_LONG).show();
            }
        }) {
            @NotNull
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("p_id", p_id);
                data.put("bus_no", bus_no);
                return data;
            }
        };
        queue.add(stringRequest);
    }

    void getBookedSeat() {
        SharedPreferences getIP = getSharedPreferences("IP", MODE_PRIVATE);
        String ipAddress = getIP.getString("ip", "");
        BookedSeatUrl = "http://"+ipAddress+"/E_TicketReservation/etiket/bookedSeat.php";
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BookedSeatUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            for (int i = 0; i < jsonObject.getJSONArray("seat").length(); i++) {
                                bookedList.add(jsonObject.getJSONArray("seat").get(i) + "");
                            }

                            for (int i = 0; i < len; i++) {
                                arrayList.add(new BuyTicketModel(i + ""));
                            }
                            BuyTicketAdapter adapter = new BuyTicketAdapter(arrayList, BuyTicketActivity.this, bookedList,buyTicket,bus_no,p_id,MyBookedList);
                            recyclerView.setAdapter(adapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(BuyTicketActivity.this));
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(BuyTicketActivity.this, error + "", Toast.LENGTH_LONG).show();
            }
        }) {
            @NotNull
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("bus_no", bus_no);
                return data;
            }
        };
        queue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(BuyTicketActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }
}