package com.blogspot.softlabsja.e_ticketreservationadmin.BusList.AddUpdateBus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.blogspot.softlabsja.e_ticketreservationadmin.BusList.BusListActivity;
import com.blogspot.softlabsja.e_ticketreservationadmin.BusList.BusListAdapter;
import com.blogspot.softlabsja.e_ticketreservationadmin.BusList.BusListModel;
import com.blogspot.softlabsja.e_ticketreservationadmin.R;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddUpdateBusActivity extends AppCompatActivity {

    TextView title;
    ImageView backBtn;
    String toolbarTitle,startingTime = "",endingTime = "", operation;
    EditText startingPointEtx,endingPointEtx,ticketPriceEtx,rowEtx,seatAvailableEtx;
    AppCompatButton setStartingTime,setEndingTime,addBtn;
    LinearLayout seatAvailableHide;
    String getRow,getBusNo,getStartingPoint,getEndingPoint,getTicketPrice,getSeatAvailable,getStartingTime,getEndingTime;
    int N = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_update_bus);

        toolbarTitle = getIntent().getStringExtra("title");
        operation = getIntent().getStringExtra("operation");
        if(toolbarTitle.equals("Update")){
            try {
                N = 1;
                getRow = getIntent().getStringExtra("row");
                getBusNo = getIntent().getStringExtra("bus_no");
                getStartingPoint = getIntent().getStringExtra("starting_point");
                getEndingPoint = getIntent().getStringExtra("ending_point");
                getTicketPrice = getIntent().getStringExtra("t_price");
                getSeatAvailable = getIntent().getStringExtra("seat_available");
                getStartingTime = getIntent().getStringExtra("starting_time");
                getEndingTime = getIntent().getStringExtra("arrival_time");
            }catch (Exception ignored){
            }
        }

        title = findViewById(R.id.title);
        backBtn = findViewById(R.id.backBtn);

        startingPointEtx = findViewById(R.id.startingPointEtx);
        endingPointEtx = findViewById(R.id.endingPointEtx);
        ticketPriceEtx = findViewById(R.id.ticketPriceEtx);
        rowEtx = findViewById(R.id.rowEtx);
        setStartingTime = findViewById(R.id.setStartingTime);
        setEndingTime = findViewById(R.id.setEndingTime);
        addBtn = findViewById(R.id.addBtn);
        seatAvailableEtx = findViewById(R.id.seatAvailableEtx);
        seatAvailableHide = findViewById(R.id.seatAvailableHide);

        title.setText(toolbarTitle);

        if(N == 1){
            seatAvailableHide.setVisibility(View.VISIBLE);
            seatAvailableEtx.setText(getSeatAvailable);
            startingPointEtx.setText(getStartingPoint);
            endingPointEtx.setText(getEndingPoint);
            ticketPriceEtx.setText(getTicketPrice);
            rowEtx.setText(getRow);
            setStartingTime.setText(getStartingTime);
            setEndingTime.setText(getEndingTime);
            startingTime = getStartingTime;
            endingTime = getEndingTime;
        }


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddUpdateBusActivity.this, BusListActivity.class);
                startActivity(intent);
                finish();
            }
        });

        setStartingTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date(1);
            }
        });

        setEndingTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date(2);
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (startingPointEtx.getText().toString().trim().equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), " Enter Starting Point", Toast.LENGTH_SHORT).show();
                } else if (endingPointEtx.getText().toString().trim().equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Enter Ending Point", Toast.LENGTH_SHORT).show();
                } else if (ticketPriceEtx.getText().toString().trim().equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Enter Ticket Price", Toast.LENGTH_SHORT).show();
                } else if (rowEtx.getText().toString().trim().equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Enter Row", Toast.LENGTH_SHORT).show();
                } else if (startingTime.equals("")) {
                    Toast.makeText(getApplicationContext(), "Select Starting Time", Toast.LENGTH_SHORT).show();
                } else if (endingTime.equals("")) {
                    Toast.makeText(getApplicationContext(), "Select Ending Time", Toast.LENGTH_SHORT).show();
                } else {
                    submit(N);
                }
            }
        });
    }

    private void Date(int n) {
        Calendar calendar = Calendar.getInstance();
        int YEAR = calendar.get(Calendar.YEAR);
        int MONTH = calendar.get(Calendar.MONTH);
        int DATE = calendar.get(Calendar.DATE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(Calendar.YEAR, year);
                calendar1.set(Calendar.MONTH, month);
                calendar1.set(Calendar.DATE, date);
                String dateText = DateFormat.format("yyyy-MM-dd", calendar1).toString();
                if(n==1){
                    startingTime = dateText + " ";
                }else{
                    endingTime = dateText + " ";
                }
                Time(n);
            }
        }, YEAR, MONTH, DATE);
        datePickerDialog.show();
    }

    private void Time(int n) {
        Calendar calendar = Calendar.getInstance();
        int HOUR = calendar.get(Calendar.HOUR);
        int MINUTE = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(Calendar.HOUR, hour);
                calendar1.set(Calendar.MINUTE, minute);
                String timeText = DateFormat.format("h:mm:ss", calendar1).toString();
                if(n==1){
                    startingTime += timeText;
                    setStartingTime.setText(startingTime);
                }else{
                    endingTime += timeText;
                    setEndingTime.setText(endingTime);
                }
            }
        }, HOUR, MINUTE, true);
        timePickerDialog.show();
    }

    private void submit(int n){
        int seat = Integer.parseInt(rowEtx.getText().toString().trim())*4;
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
                                Toast.makeText(AddUpdateBusActivity.this, jsonObject.getString("message") + "", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(AddUpdateBusActivity.this, jsonObject.getString("message") + "", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(AddUpdateBusActivity.this, BusListActivity.class);
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
                Toast.makeText(AddUpdateBusActivity.this, error + "", Toast.LENGTH_LONG).show();
            }
        }){
            @NotNull
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();

                if(n == 1){
                    data.put("bus_no", getBusNo);
                    data.put("starting_point", startingPointEtx.getText().toString().trim());
                    data.put("ending_point", endingPointEtx.getText().toString().trim());
                    data.put("starting_time", startingTime);
                    data.put("arrival_time", endingTime);
                    data.put("seat_available", seatAvailableEtx.getText().toString().trim());
                    data.put("t_price", ticketPriceEtx.getText().toString().trim());
                    data.put("row", rowEtx.getText().toString().trim());
                }else{
                    data.put("starting_point", startingPointEtx.getText().toString().trim());
                    data.put("ending_point", endingPointEtx.getText().toString().trim());
                    data.put("starting_time", startingTime);
                    data.put("arrival_time", endingTime);
                    data.put("seat_available", seat+"");
                    data.put("t_price", ticketPriceEtx.getText().toString().trim());
                    data.put("row", rowEtx.getText().toString().trim());
                }
                return data;
            }
        };
        queue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AddUpdateBusActivity.this, BusListActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }
}