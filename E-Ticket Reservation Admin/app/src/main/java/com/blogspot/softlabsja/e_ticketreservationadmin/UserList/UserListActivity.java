package com.blogspot.softlabsja.e_ticketreservationadmin.UserList;

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
import com.blogspot.softlabsja.e_ticketreservationadmin.BusList.BusListActivity;
import com.blogspot.softlabsja.e_ticketreservationadmin.BusList.BusListAdapter;
import com.blogspot.softlabsja.e_ticketreservationadmin.BusList.BusListModel;
import com.blogspot.softlabsja.e_ticketreservationadmin.R;
import com.blogspot.softlabsja.e_ticketreservationadmin.UserList.AddUpdateUser.AddUpdateUserActivity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserListActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<UserListModel> arrayList = new ArrayList<UserListModel>();
    ImageView backBtn,addBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        recyclerView = findViewById(R.id.recyclerView);
        backBtn = findViewById(R.id.backBtn);
        //addBtn = findViewById(R.id.addBtn);

        UserList();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
//        addBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(UserListActivity.this, AddUpdateUserActivity.class);
//                intent.putExtra("title","Add");
//                intent.putExtra("operation","addPassenger");
//                startActivity(intent);
//                finish();
//            }
//        });
    }

    void UserList() {
        SharedPreferences getIP = getSharedPreferences("IP", MODE_PRIVATE);
        String ipAddress = getIP.getString("ip", "");
        String BusListUrl = "http://"+ipAddress+"/E_TicketReservation/admin/getAllPassenger.php";
        arrayList.clear();
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, BusListUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("error").equals("true")) {
                                Toast.makeText(UserListActivity.this, jsonObject.getString("message") + "", Toast.LENGTH_LONG).show();
                            } else {

                                for (int i = 0;i<jsonObject.length();i++){
                                    JSONObject jsonObject1 = new JSONObject(jsonObject.getString(i+""));
                                    arrayList.add(new UserListModel(
                                            jsonObject1.getString("id"),
                                            jsonObject1.getString("username"),
                                            jsonObject1.getString("email"),
                                            jsonObject1.getString("password")));
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                        UserListAdapter adapter = new UserListAdapter(arrayList, UserListActivity.this);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(UserListActivity.this));
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UserListActivity.this, error + "", Toast.LENGTH_LONG).show();
            }
        });
        queue.add(stringRequest);
    }
}