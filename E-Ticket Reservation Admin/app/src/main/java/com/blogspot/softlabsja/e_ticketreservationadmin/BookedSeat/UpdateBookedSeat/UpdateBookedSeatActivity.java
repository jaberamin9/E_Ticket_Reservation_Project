package com.blogspot.softlabsja.e_ticketreservationadmin.BookedSeat.UpdateBookedSeat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.blogspot.softlabsja.e_ticketreservationadmin.BookedSeat.BookedSeatActivity;
import com.blogspot.softlabsja.e_ticketreservationadmin.BusList.AddUpdateBus.AddUpdateBusActivity;
import com.blogspot.softlabsja.e_ticketreservationadmin.BusList.BusListActivity;
import com.blogspot.softlabsja.e_ticketreservationadmin.R;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class UpdateBookedSeatActivity extends AppCompatActivity {

    TextView title;
    ImageView backBtn;
    EditText busIDEtx,pIDEtx,bookedSeatEtx;
    AppCompatButton setBookedDate,setValidity,addBtn;
    String toolbarTitle,bookedDate = "",validity = "";
    String getID,getBusNo,getPID,getBookedSeat,getBookedDate,getValidity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_booked_seat);

        toolbarTitle = getIntent().getStringExtra("title");
        getID = getIntent().getStringExtra("id");
        getBusNo = getIntent().getStringExtra("bus_no");
        getPID = getIntent().getStringExtra("p_id");
        getBookedSeat = getIntent().getStringExtra("seat");
        getBookedDate = getIntent().getStringExtra("booked_date");
        getValidity = getIntent().getStringExtra("validity");

        title = findViewById(R.id.title);
        backBtn = findViewById(R.id.backBtn);
        busIDEtx = findViewById(R.id.busIDEtx);
        pIDEtx = findViewById(R.id.pIDEtx);
        bookedSeatEtx = findViewById(R.id.bookedSeatEtx);
        setBookedDate = findViewById(R.id.setBookedDate);
        setValidity = findViewById(R.id.setValidity);
        addBtn = findViewById(R.id.addBtn);


        title.setText(toolbarTitle);
        busIDEtx.setText(getBusNo);
        pIDEtx.setText(getPID);
        bookedSeatEtx.setText(getBookedSeat);
        setBookedDate.setText(getBookedDate);
        setValidity.setText(getValidity);
        bookedDate = getBookedDate;
        validity = getValidity;



        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UpdateBookedSeatActivity.this, BookedSeatActivity.class);
                startActivity(intent);
                finish();
            }
        });

        setBookedDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date(1);
            }
        });

        setValidity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date(2);
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (busIDEtx.getText().toString().trim().equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Enter Bus ID", Toast.LENGTH_SHORT).show();
                } else if (pIDEtx.getText().toString().trim().equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Enter Passenger ID", Toast.LENGTH_SHORT).show();
                } else if (bookedSeatEtx.getText().toString().trim().equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Enter Booked Seat", Toast.LENGTH_SHORT).show();
                }else if (bookedDate.equals("")) {
                    Toast.makeText(getApplicationContext(), "Select Booked Date", Toast.LENGTH_SHORT).show();
                } else if (validity.equals("")) {
                    Toast.makeText(getApplicationContext(), "Select Validity Date", Toast.LENGTH_SHORT).show();
                } else {
                    submit();
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
                    bookedDate = dateText + " ";
                }else{
                    validity = dateText + " ";
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
                    bookedDate += timeText;
                    setBookedDate.setText(bookedDate);
                }else{
                    validity += timeText;
                    setValidity.setText(validity);
                }
            }
        }, HOUR, MINUTE, true);
        timePickerDialog.show();
    }

    private void submit(){
        SharedPreferences getIP = getSharedPreferences("IP", MODE_PRIVATE);
        String ipAddress = getIP.getString("ip", "");
        String BusListUrl = "http://"+ ipAddress +"/E_TicketReservation/admin/UpdateBookedSeat.php";
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BusListUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("error").equals("true")) {
                                Toast.makeText(UpdateBookedSeatActivity.this, jsonObject.getString("message") + "", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(UpdateBookedSeatActivity.this, jsonObject.getString("message") + "", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(UpdateBookedSeatActivity.this, BookedSeatActivity.class);
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
                Toast.makeText(UpdateBookedSeatActivity.this, error + "", Toast.LENGTH_LONG).show();
            }
        }){
            @NotNull
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("id", getID);
                data.put("bus_no", busIDEtx.getText().toString().trim());
                data.put("p_id", pIDEtx.getText().toString().trim());
                data.put("seat", bookedSeatEtx.getText().toString().trim());
                data.put("booked_date", bookedDate);
                data.put("validity", validity);
                return data;
            }
        };
        queue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(UpdateBookedSeatActivity.this, BookedSeatActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }
}