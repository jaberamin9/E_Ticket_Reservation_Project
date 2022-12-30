package com.blogspot.softlabsja.e_ticketreservationadmin.UserList;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.blogspot.softlabsja.e_ticketreservationadmin.R;
import com.blogspot.softlabsja.e_ticketreservationadmin.UserList.AddUpdateUser.AddUpdateUserActivity;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import static android.content.Context.MODE_PRIVATE;


public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder>{

    ArrayList<UserListModel> arrayList;
    Context context;

    public UserListAdapter(ArrayList<UserListModel> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public UserListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.item_user_list, parent, false);
        return new UserListAdapter.ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserListAdapter.ViewHolder holder, int position) {
        holder.idTxt.setText("ID: "+arrayList.get(position).getId());
        holder.userNameTxt.setText("User Name: "+arrayList.get(position).getUserName());
        holder.emailTxt.setText("Email: "+arrayList.get(position).getEmail());

        if(position%9==0){
            holder.image.setImageResource(R.drawable.u1);
        }else if(position%9==1){
            holder.image.setImageResource(R.drawable.u2);
        }else if(position%9==2) {
            holder.image.setImageResource(R.drawable.u3);
        }else if(position%9==3) {
            holder.image.setImageResource(R.drawable.u4);
        }else if(position%9==4) {
            holder.image.setImageResource(R.drawable.u5);
        }else if(position%9==5) {
            holder.image.setImageResource(R.drawable.u6);
        }else if(position%9==6) {
            holder.image.setImageResource(R.drawable.u7);
        }else if(position%9==7) {
            holder.image.setImageResource(R.drawable.u8);
        }else {
            holder.image.setImageResource(R.drawable.u9);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddUpdateUserActivity.class);
                intent.putExtra("title","Update");
                intent.putExtra("operation","updatePassenger");
                intent.putExtra("id",arrayList.get(position).getId());
                intent.putExtra("userName",arrayList.get(position).getUserName());
                intent.putExtra("email",arrayList.get(position).getEmail());
                intent.putExtra("password",arrayList.get(position).getPassword());
                context.startActivity(intent);
                ((Activity)context).finish();
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Dialog(arrayList.get(position).getId());
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView idTxt, userNameTxt, emailTxt;
        ImageView image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            idTxt = itemView.findViewById(R.id.idTxt);
            userNameTxt = itemView.findViewById(R.id.userNameTxt);
            emailTxt = itemView.findViewById(R.id.emailTxt);
        }
    }

    private void Dialog(String id){
        new AlertDialog.Builder(context)
                .setIcon(R.drawable.ic_alert)
                .setTitle("Are you sure you want to Delete this User?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        delete(id);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();
    }

    private void delete(String id){
        SharedPreferences getIP = context.getSharedPreferences("IP", MODE_PRIVATE);
        String ipAddress = getIP.getString("ip", "");
        String BusListUrl = "http://"+ ipAddress +"/E_TicketReservation/admin/deletePassenger.php";
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
                                Intent intent = new Intent(context, UserListActivity.class);
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
                return data;
            }
        };
        queue.add(stringRequest);
    }
}
