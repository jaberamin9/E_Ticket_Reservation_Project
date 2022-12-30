package com.blogspot.softlabsja.e_ticketreservationadmin.BookedSeat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.blogspot.softlabsja.e_ticketreservationadmin.BookedSeat.UpdateBookedSeat.UpdateBookedSeatActivity;
import com.blogspot.softlabsja.e_ticketreservationadmin.R;
import com.blogspot.softlabsja.e_ticketreservationadmin.UserList.UserListActivity;
import com.blogspot.softlabsja.e_ticketreservationadmin.UserList.UserListAdapter;
import com.blogspot.softlabsja.e_ticketreservationadmin.UserList.UserListModel;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BookedSeatActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<BookedSeatModel> arrayList = new ArrayList<BookedSeatModel>();
    ImageView backBtn,deleteBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booked_seat);

        recyclerView = findViewById(R.id.recyclerView);
        backBtn = findViewById(R.id.backBtn);
        deleteBtn = findViewById(R.id.deleteBtn);

        BookedSeatList();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteDialog();
            }
        });
    }

    void BookedSeatList() {
        SharedPreferences getIP = getSharedPreferences("IP", MODE_PRIVATE);
        String ipAddress = getIP.getString("ip", "");
        String BusListUrl = "http://"+ipAddress+"/E_TicketReservation/admin/AllBookedSeat.php";
        arrayList.clear();
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, BusListUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("error").equals("true")) {
                                Toast.makeText(BookedSeatActivity.this, jsonObject.getString("message") + "", Toast.LENGTH_LONG).show();
                            } else {

                                for (int i = 0;i<jsonObject.length();i++){
                                    JSONObject jsonObject1 = new JSONObject(jsonObject.getString(i+""));
                                    arrayList.add(new BookedSeatModel(
                                            jsonObject1.getString("id"),
                                            jsonObject1.getString("bus_no"),
                                            jsonObject1.getString("p_id"),
                                            jsonObject1.getString("seat"),
                                            jsonObject1.getString("booked_date"),
                                            jsonObject1.getString("validity")));
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                        BookedSeatAdapter adapter = new BookedSeatAdapter(arrayList, BookedSeatActivity.this);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(BookedSeatActivity.this));
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(BookedSeatActivity.this, error + "", Toast.LENGTH_LONG).show();
            }
        });
        queue.add(stringRequest);
    }

    private void DeleteDialog() {

        Dialog dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.delete);
        dialog.setCanceledOnTouchOutside(true);

        RadioGroup radioGroup = dialog.findViewById(R.id.radioGroup);
        EditText idEtx = dialog.findViewById(R.id.idEtx);
        AppCompatButton deleteBtn = dialog.findViewById(R.id.deleteBtn);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = dialog.findViewById(checkedId);
                if(radioButton.getText().equals("By Bus ID")){
                    deleteBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (idEtx.getText().toString().trim().equalsIgnoreCase("")) {
                                Toast.makeText(getApplicationContext(), "Enter ID", Toast.LENGTH_SHORT).show();
                            } else {
                                Delete(1,idEtx.getText().toString().trim());
                                dialog.dismiss();
                            }
                        }
                    });
                }else {
                    deleteBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (idEtx.getText().toString().trim().equalsIgnoreCase("")) {
                                Toast.makeText(getApplicationContext(), "Enter ID", Toast.LENGTH_SHORT).show();
                            } else {
                                Delete(2, idEtx.getText().toString().trim());
                                dialog.dismiss();
                            }
                        }
                    });
                }
            }
        });

        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();

    }
    private void Delete(int n,String id){
        SharedPreferences getIP = getSharedPreferences("IP", MODE_PRIVATE);
        String ipAddress = getIP.getString("ip", "");
        String BusListUrl;
        if(n == 1){
            BusListUrl = "http://"+ ipAddress +"/E_TicketReservation/admin/DeleteBookedSeatByBus.php";
        }else {
            BusListUrl = "http://"+ ipAddress +"/E_TicketReservation/admin/DeleteBookedSeatByPassenger.php";
        }
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BusListUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("error").equals("true")) {
                                Toast.makeText(BookedSeatActivity.this, jsonObject.getString("message") + "", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(BookedSeatActivity.this, jsonObject.getString("message") + "", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(BookedSeatActivity.this, BookedSeatActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(BookedSeatActivity.this, e + "", Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(BookedSeatActivity.this, error + "", Toast.LENGTH_LONG).show();
            }
        }){
            @NotNull
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                if(n == 1){
                    data.put("bus_no", id);
                }else {
                    data.put("p_id", id);
                }
                return data;
            }
        };
        queue.add(stringRequest);
    }
}