package com.blogspot.softlabsja.e_ticketreservationsystem.TicketHistory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.blogspot.softlabsja.e_ticketreservationsystem.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class TicketHistoryAdapter extends RecyclerView.Adapter<TicketHistoryAdapter.ViewHolder>{

    ArrayList<TicketHistoryModel> arrayList;
    Context context;

    public TicketHistoryAdapter(ArrayList<TicketHistoryModel> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public TicketHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.item_booked_history, parent, false);
        return new TicketHistoryAdapter.ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketHistoryAdapter.ViewHolder holder, int position) {
        holder.busID.setText("#"+arrayList.get(position).getBusNo());
        holder.validity.setText("Validity: "+arrayList.get(position).getValidity());
        holder.bookedDate.setText("Booked Date: "+arrayList.get(position).getBookedDate());
        holder.bookedSeat.setText("Booked Seat: "+arrayList.get(position).getSeat());
        holder.price.setText("$"+arrayList.get(position).getTicketPrice());
        holder.location.setText(arrayList.get(position).getStartingPoint()+" To "+arrayList.get(position).getEndingPoint());

        if(position%3==0){
            holder.image.setImageResource(R.drawable.c1);
        }else if(position%3==1){
            holder.image.setImageResource(R.drawable.c2);
        }else {
            holder.image.setImageResource(R.drawable.c3);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());

        long t = Long.parseLong(arrayList.get(position).getValidity().replaceAll("\\W+", ""));
        long ct = Long.parseLong(currentDateandTime.replaceAll("_", ""));

        if(ct>t){
            holder.indicator.setBackgroundResource(R.drawable.red);
        }else{
            holder.indicator.setBackgroundResource(R.drawable.green);
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView busID, validity, bookedDate, bookedSeat,location,price;
        ImageView image;
        RelativeLayout indicator;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            busID = itemView.findViewById(R.id.busID);
            validity = itemView.findViewById(R.id.validity);
            bookedDate = itemView.findViewById(R.id.bookedDate);
            bookedSeat = itemView.findViewById(R.id.bookedSeat);
            location = itemView.findViewById(R.id.location);
            price = itemView.findViewById(R.id.price);
            indicator = itemView.findViewById(R.id.indicator);
        }
    }
}
