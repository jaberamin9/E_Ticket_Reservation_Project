package com.blogspot.softlabsja.e_ticketreservationsystem.BuyTicket;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.blogspot.softlabsja.e_ticketreservationsystem.MainActivity;
import com.blogspot.softlabsja.e_ticketreservationsystem.R;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class BuyTicketAdapter extends RecyclerView.Adapter<BuyTicketAdapter.ViewHolder>{

    ArrayList<BuyTicketModel> arrayList;
    Context context;
    ArrayList<String> bookedList;
    int f=0;
    private final ArrayList<ArrayList<Integer>> select = new ArrayList<ArrayList<Integer>>();
    ArrayList<String> MyBookedList;
    Button buyTicket;
    String ro ,co,bus_no,p_id;

    public BuyTicketAdapter(ArrayList<BuyTicketModel> arrayList, Context context,ArrayList<String> bookedList,Button buyTicket,String bus_no,String p_id,ArrayList<String> MyBookedList) {
        this.arrayList = arrayList;
        this.context = context;
        this.bookedList = bookedList;
        this.buyTicket = buyTicket;
        this.bus_no = bus_no;
        this.p_id = p_id;
        this.MyBookedList = MyBookedList;
        for (int i = 0; i < arrayList.size(); i++) {
            ArrayList<Integer> arr = new ArrayList<>(4);
            for (int j = 0; j < 4; j++) {
                arr.add(0);
            }
            select.add(arr);
        }
    }


    @NonNull
    @Override
    public BuyTicketAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.item_buy_ticket, parent, false);
        return new BuyTicketAdapter.ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull BuyTicketAdapter.ViewHolder holder, int position) {

        for (int i = 0; i < select.get(position).size(); i++) {
            if (select.get(position).get(i) == 1) {
                if(i==0) {
                    holder.a.setImageResource(R.drawable.your_seat_img);
                    holder.b.setImageResource(R.drawable.available_img);
                    holder.c.setImageResource(R.drawable.available_img);
                    holder.d.setImageResource(R.drawable.available_img);
                    break;
                }else if(i == 1) {
                    holder.b.setImageResource(R.drawable.your_seat_img);
                    break;
                }else if (i == 2) {
                    holder.c.setImageResource(R.drawable.your_seat_img);
                    break;
                }else if (i == 3) {
                    holder.d.setImageResource(R.drawable.your_seat_img);
                    break;
                }else {
                    holder.a.setImageResource(R.drawable.your_seat_img);
                    holder.b.setImageResource(R.drawable.available_img);
                    holder.c.setImageResource(R.drawable.available_img);
                    holder.d.setImageResource(R.drawable.available_img);
                }
            } else {
                holder.a.setImageResource(R.drawable.available_img);
                holder.b.setImageResource(R.drawable.available_img);
                holder.c.setImageResource(R.drawable.available_img);
                holder.d.setImageResource(R.drawable.available_img);
            }
        }


        for (int i = 0; i < bookedList.size(); i++) {
            char col = bookedList.get(i).charAt(bookedList.get(i).length()-1);
            int row = Integer.parseInt(bookedList.get(i).substring(0,bookedList.get(i).length()-1))-1;
            if(col == 'A'){
                if(position == row) holder.a.setImageResource(R.drawable.booked_img);
            }else if(col=='B'){
                if(position == row) holder.b.setImageResource(R.drawable.booked_img);
            }else if(col=='C'){
                if(position == row) holder.c.setImageResource(R.drawable.booked_img);
            }else{
                if(position == row) holder.d.setImageResource(R.drawable.booked_img);
            }

            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(f==0) notifyDataSetChanged();
                    f++;
                }
            }, 500);

        }

        for (int i = 0; i < MyBookedList.size(); i++) {
            char col = MyBookedList.get(i).charAt(MyBookedList.get(i).length()-1);
            int row = Integer.parseInt(MyBookedList.get(i).substring(0,MyBookedList.get(i).length()-1))-1;
            if(col == 'A'){
                if(position == row) holder.a.setImageResource(R.drawable.your_seat_img);
            }else if(col=='B'){
                if(position == row) holder.b.setImageResource(R.drawable.your_seat_img);
            }else if(col=='C'){
                if(position == row) holder.c.setImageResource(R.drawable.your_seat_img);
            }else{
                if(position == row) holder.d.setImageResource(R.drawable.your_seat_img);
            }

            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(f==1) notifyDataSetChanged();
                    f++;
                }
            }, 500);

        }

        holder.id.setText((position+1)+"");

        holder.a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int k=0; k<select.size(); k++) {
                    ArrayList<Integer> arr = new ArrayList<>(4);
                    if(k==position) {
                        for (int j = 0; j < 4; j++) {
                            if(j==0)arr.add(1);
                            else arr.add(0);
                        }
                    } else {
                        for (int j = 0; j < 4; j++) {
                            arr.add(0);
                        }
                    }
                    select.set(k,arr);
                }
                ro = (position+1)+"";
                co = "A";
                notifyDataSetChanged();
            }
        });
        holder.b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int k=0; k<select.size(); k++) {
                    ArrayList<Integer> arr = new ArrayList<>(4);
                    if(k==position) {
                        for (int j = 0; j < 4; j++) {
                            if(j==1)arr.add(1);
                            else arr.add(0);
                        }
                    } else {
                        for (int j = 0; j < 4; j++) {
                            arr.add(0);
                        }
                    }
                    select.set(k,arr);
                }
                ro = (position+1)+"";
                co = "B";
                notifyDataSetChanged();
            }
        });
        holder.c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int k=0; k<select.size(); k++) {
                    ArrayList<Integer> arr = new ArrayList<>(4);
                    if(k==position) {
                        for (int j = 0; j < 4; j++) {
                            if(j==2)arr.add(1);
                            else arr.add(0);
                        }
                    } else {
                        for (int j = 0; j < 4; j++) {
                            arr.add(0);
                        }
                    }
                    select.set(k,arr);
                }
                ro = (position+1)+"";
                co = "C";
                notifyDataSetChanged();
            }
        });
        holder.d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int k=0; k<select.size(); k++) {
                    ArrayList<Integer> arr = new ArrayList<>(4);
                    if(k==position) {
                        for (int j = 0; j < 4; j++) {
                            if(j==3)arr.add(1);
                            else arr.add(0);
                        }
                    } else {
                        for (int j = 0; j < 4; j++) {
                            arr.add(0);
                        }
                    }
                    select.set(k,arr);
                }
                ro = (position+1)+"";
                co = "D";
                notifyDataSetChanged();
            }
        });

        buyTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ro != null && co != null) {
                    int flag = 0;
                    for (int i = 0; i < bookedList.size(); i++) {
                        String col = String.valueOf(bookedList.get(i).charAt(bookedList.get(i).length() - 1));
                        String row = bookedList.get(i).substring(0, bookedList.get(i).length() - 1);
                        if (row.equals(ro) && col.equals(co)) {
                            flag = 1;
                            break;
                        }
                    }
                    if (flag == 1) {
                        Toast.makeText(context, "Already Booked", Toast.LENGTH_SHORT).show();
                    } else {
                        ConfirmTicket(ro+co);
                    }
                }else{
                    Toast.makeText(context, "Select a Seat", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView id;
        ImageView a,b,c,d;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.id);
            a = itemView.findViewById(R.id.a);
            b = itemView.findViewById(R.id.b);
            c = itemView.findViewById(R.id.c);
            d = itemView.findViewById(R.id.d);
        }
    }

    void ConfirmTicket(String seat){

        SharedPreferences getIP = context.getSharedPreferences("IP", MODE_PRIVATE);
        String ipAddress = getIP.getString("ip", "");
        String buyTicketUrl = "http://"+ipAddress+"/E_TicketReservation/etiket/buyTicket.php";

        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, buyTicketUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Toast.makeText(context, jsonObject.getString("message") + "", Toast.LENGTH_LONG).show();
                            MyBookedList.add(seat);
                            Intent intent = new Intent(context,BuyTicketActivity.class);
                            context.startActivity(intent);
                            ((Activity)context).finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error + "", Toast.LENGTH_LONG).show();
            }
        }) {
            @NotNull
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("bus_no", bus_no);
                data.put("p_id", p_id);
                data.put("seat", seat);
                return data;
            }
        };
        queue.add(stringRequest);
    }
}
