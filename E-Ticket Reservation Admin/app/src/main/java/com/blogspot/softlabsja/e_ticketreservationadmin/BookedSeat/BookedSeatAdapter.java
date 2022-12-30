package com.blogspot.softlabsja.e_ticketreservationadmin.BookedSeat;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.blogspot.softlabsja.e_ticketreservationadmin.BookedSeat.UpdateBookedSeat.UpdateBookedSeatActivity;
import com.blogspot.softlabsja.e_ticketreservationadmin.R;
import com.blogspot.softlabsja.e_ticketreservationadmin.UserList.AddUpdateUser.AddUpdateUserActivity;
import com.blogspot.softlabsja.e_ticketreservationadmin.UserList.UserListActivity;
import com.blogspot.softlabsja.e_ticketreservationadmin.UserList.UserListAdapter;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;


public class BookedSeatAdapter extends RecyclerView.Adapter<BookedSeatAdapter.ViewHolder>{

    ArrayList<BookedSeatModel> arrayList;
    Context context;

    public BookedSeatAdapter(ArrayList<BookedSeatModel> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public BookedSeatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.item_booked_seat, parent, false);
        return new BookedSeatAdapter.ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull BookedSeatAdapter.ViewHolder holder, int position) {
        holder.ID.setText(arrayList.get(position).getId());
        holder.busID.setText("Bus ID: #"+arrayList.get(position).getBusNo());
        holder.pId.setText("Passenger ID: #"+arrayList.get(position).getpId());
        holder.seat.setText("Booked Seat: "+arrayList.get(position).getSeat());


        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        try {
            calendar.setTime(simpleDateFormat.parse(arrayList.get(position).getBookedDate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String date = (String) DateFormat.format("E, d MMM yyyy h:mm a", calendar);
        holder.booked_date.setText("Booked: "+date);

        Calendar calendar2 = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        try {
            calendar2.setTime(simpleDateFormat2.parse(arrayList.get(position).getValidity()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String date2 = (String) DateFormat.format("E, d MMM yyyy h:mm a", calendar2);
        holder.validity.setText("Validity: "+date2);

        if(position%3==0){
            holder.image.setBackgroundResource(R.drawable.green);
        }else if(position%3==1){
            holder.image.setBackgroundResource(R.drawable.red);
        }else {
            holder.image.setBackgroundResource(R.drawable.yellow);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UpdateBookedSeatActivity.class);
                intent.putExtra("title","Update");
                intent.putExtra("id",arrayList.get(position).getId());
                intent.putExtra("bus_no",arrayList.get(position).getBusNo());
                intent.putExtra("p_id",arrayList.get(position).getpId());
                intent.putExtra("seat",arrayList.get(position).getSeat());
                intent.putExtra("booked_date",arrayList.get(position).getBookedDate());
                intent.putExtra("validity",arrayList.get(position).getValidity());
                context.startActivity(intent);
                ((Activity)context).finish();
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Dialog(arrayList.get(position).getId(),arrayList.get(position).getBusNo());
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView ID, busID, pId,seat,booked_date,validity;
        RelativeLayout image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            ID = itemView.findViewById(R.id.ID);
            busID = itemView.findViewById(R.id.busID);
            pId = itemView.findViewById(R.id.pId);
            seat = itemView.findViewById(R.id.seat);
            booked_date = itemView.findViewById(R.id.booked_date);
            validity = itemView.findViewById(R.id.validity);
        }
    }

    private void Dialog(String id,String BusNo){
        new AlertDialog.Builder(context)
                .setIcon(R.drawable.ic_alert)
                .setTitle("Are you sure you want to Delete this item?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        delete(id,BusNo);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();
    }

    private void delete(String id,String BusNo){
        SharedPreferences getIP = context.getSharedPreferences("IP", MODE_PRIVATE);
        String ipAddress = getIP.getString("ip", "");
        String BusListUrl = "http://"+ ipAddress +"/E_TicketReservation/admin/DeleteBookedSeat.php";
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
                                Intent intent = new Intent(context, BookedSeatActivity.class);
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
                data.put("id", id);
                data.put("bus_no", BusNo);
                return data;
            }
        };
        queue.add(stringRequest);
    }
}
