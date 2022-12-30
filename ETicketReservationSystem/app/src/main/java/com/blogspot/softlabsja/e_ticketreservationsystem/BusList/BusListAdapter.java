package com.blogspot.softlabsja.e_ticketreservationsystem.BusList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.softlabsja.e_ticketreservationsystem.BuyTicket.BuyTicketActivity;
import com.blogspot.softlabsja.e_ticketreservationsystem.R;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

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
        holder.startTime.setText("S T: "+LocalTime.parse(parts[1], DateTimeFormatter.ofPattern("HH:mm:ss")).format(DateTimeFormatter.ofPattern("hh:mm a")));
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
                Intent intent = new Intent(context, BuyTicketActivity.class);
                intent.putExtra("row",arrayList.get(position).getRow());
                intent.putExtra("bus_no",arrayList.get(position).getBusNo());
                intent.putExtra("st_point",arrayList.get(position).getStartingPoint());
                intent.putExtra("en_point",arrayList.get(position).getEndingPoint());
                intent.putExtra("date",parts[0]);
                intent.putExtra("time",LocalTime.parse(parts[1], DateTimeFormatter.ofPattern("HH:mm:ss")).format(DateTimeFormatter.ofPattern("hh:mm a"))+" - "+
                        LocalTime.parse(parts1[1], DateTimeFormatter.ofPattern("HH:mm:ss")).format(DateTimeFormatter.ofPattern("hh:mm a")));
                intent.putExtra("tPrice",arrayList.get(position).getTicketPrice());
                context.startActivity(intent);
                ((Activity)context).finish();
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
}
