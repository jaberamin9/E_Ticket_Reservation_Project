package com.blogspot.softlabsja.e_ticketreservationadmin.Dashboard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.blogspot.softlabsja.e_ticketreservationadmin.BookedSeat.BookedSeatActivity;
import com.blogspot.softlabsja.e_ticketreservationadmin.BusList.BusListActivity;
import com.blogspot.softlabsja.e_ticketreservationadmin.R;
import com.blogspot.softlabsja.e_ticketreservationadmin.UserList.UserListActivity;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DashboardActivity extends AppCompatActivity {

    RelativeLayout vehicleCRUD,passengerCRUD,bookedSeatsCRUD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        vehicleCRUD = findViewById(R.id.vehicleCRUD);
        passengerCRUD = findViewById(R.id.passengerCRUD);
        bookedSeatsCRUD = findViewById(R.id.bookedSeatsCRUD);

        vehicleCRUD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, BusListActivity.class);
                startActivity(intent);
            }
        });
        passengerCRUD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, UserListActivity.class);
                startActivity(intent);
            }
        });
        bookedSeatsCRUD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, BookedSeatActivity.class);
                startActivity(intent);
            }
        });
    }
}