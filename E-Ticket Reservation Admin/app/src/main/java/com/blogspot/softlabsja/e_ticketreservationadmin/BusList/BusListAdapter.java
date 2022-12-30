package com.blogspot.softlabsja.e_ticketreservationadmin.BusList;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.blogspot.softlabsja.e_ticketreservationadmin.BusList.AddUpdateBus.AddUpdateBusActivity;
import com.blogspot.softlabsja.e_ticketreservationadmin.R;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class BusListAdapter extends RecyclerView.Adapter<BusListAdapter.ViewHolder> {

    ArrayList<BusListModel> arrayList;
    Context context;

    public BusListAdapter(ArrayList<BusListModel> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public BusListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.item_bus_list, parent, false);
        return new BusListAdapter.ViewHolder(contactView);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull BusListAdapter.ViewHolder holder, int position) {
        holder.busID.setText("#"+arrayList.get(position).getBusNo());
        holder.availableSeat.setText("Available seat : "+arrayList.get(position).getSeatAvailable());

        String[] parts = arrayList.get(position).getStartingTime().split(" ");
        String[] parts1 = arrayList.get(position).getArrivalTime().split(" ");


        holder.price.setText("$"+arrayList.get(position).getTicketPrice());
        holder.date.setText("Date: "+parts[0]);
        holder.location.setText(arrayList.get(position).getStartingPoint()+" To "+arrayList.get(position).getEndingPoint());
        holder.startTime.setText("S T: "+ LocalTime.parse(parts[1], DateTimeFormatter.ofPattern("HH:mm:ss")).format(DateTimeFormatter.ofPattern("hh:mm a")));
        holder.arrivalTime.setText("E T: "+LocalTime.parse(parts1[1], DateTimeFormatter.ofPattern("HH:mm:ss")).format(DateTimeFormatter.ofPattern("hh:mm a")));


        if(position%3==0){
            holder.image.setImageResource(R.drawable.c1);
        }else if(position%3==1){
            holder.image.setImageResource(R.drawable.c2);
        }else {
            holder.image.setImageResource(R.drawable.c3);
        }

        int seat  = Integer.parseInt(arrayList.get(position).getSeatAvailable());

        if(seat<=0){
            holder.indicator.setBackgroundResource(R.drawable.red);
        }else if(seat<=10){
            holder.indicator.setBackgroundResource(R.drawable.yellow);
        }else {
            holder.indicator.setBackgroundResource(R.drawable.green);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddUpdateBusActivity.class);
                intent.putExtra("title","Update");
                intent.putExtra("operation","updateAddBus");
                intent.putExtra("row",arrayList.get(position).getRow());
                intent.putExtra("bus_no",arrayList.get(position).getBusNo());
                intent.putExtra("starting_point",arrayList.get(position).getStartingPoint());
                intent.putExtra("ending_point",arrayList.get(position).getEndingPoint());
                intent.putExtra("t_price",arrayList.get(position).getTicketPrice());
                intent.putExtra("seat_available",arrayList.get(position).getSeatAvailable());
                intent.putExtra("starting_time",arrayList.get(position).getStartingTime());
                intent.putExtra("arrival_time",arrayList.get(position).getArrivalTime());
                context.startActivity(intent);
                ((Activity)context).finish();
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Dialog(arrayList.get(position).getBusNo());
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView busID, availableSeat, date, startTime, arrivalTime,location,price;
        ImageView image;
        RelativeLayout indicator;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            busID = itemView.findViewById(R.id.busID);
            availableSeat = itemView.findViewById(R.id.availableSeat);
            date = itemView.findViewById(R.id.date);
            startTime = itemView.findViewById(R.id.startTime);
            arrivalTime = itemView.findViewById(R.id.arrivalTime);
            location = itemView.findViewById(R.id.location);
            price = itemView.findViewById(R.id.price);
            indicator = itemView.findViewById(R.id.indicator);
        }
    }

    private void Dialog(String BusNo){
        new AlertDialog.Builder(context)
                .setIcon(R.drawable.ic_alert)
                .setTitle("Are you sure you want to Delete this item?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        delete(BusNo);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();
    }

    private void delete(String BusNo){
        SharedPreferences getIP = context.getSharedPreferences("IP", MODE_PRIVATE);
        String ipAddress = getIP.getString("ip", "");
        String BusListUrl = "http://"+ ipAddress +"/E_TicketReservation/admin/deleteAddBus.php";
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BusListUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("error").equals("true")) {
                                Toast.makeText(context, jsonObject.getString("message") + "", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(context, jsonObject.getString("message") + "", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(context, BusListActivity.class);
                                context.startActivity(intent);
                                ((Activity)context).finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error + "", Toast.LENGTH_LONG).show();
            }
        }){
            @NotNull
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("bus_no", BusNo);
                return data;
            }
        };
        queue.add(stringRequest);
    }
}
