package com.example.mylocationapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.ContentInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private Context context;
    Activity activity;
    private ArrayList reminder_id, date, time, title, description, lati, longi, range, noti, voice, vibrate;

    CustomAdapter (Activity activity, Context context, ArrayList reminder_id, ArrayList date, ArrayList time, ArrayList title, ArrayList description,
                   ArrayList lati, ArrayList longi, ArrayList range){

        this.activity = activity;
        this.context = context;
        this.reminder_id = reminder_id;
        this.date = date;
        this.time = time;
        this.title = title;
        this.description = description;
        this.lati = lati;
        this.longi = longi;
        this.range = range;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        holder.reminder_id_txt.setText(String.valueOf(reminder_id.get(position)));
        holder.reminder_date_txt.setText(String.valueOf(date.get(position)));
        holder.reminder_time_txt.setText(String.valueOf(time.get(position)));
        holder.reminder_title_txt.setText(String.valueOf(title.get(position)));
        holder.reminder_description_txt.setText(String.valueOf(description.get(position)));
        holder.reminder_lati_txt.setText(String.valueOf(lati.get(position)));
        holder.reminder_longi_txt.setText(String.valueOf(longi.get(position)));
        holder.reminder_range_txt.setText(String.valueOf(range.get(position)));

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateActivity.class);
                intent.putExtra("id", String.valueOf(reminder_id.get(0)));
                intent.putExtra("date", String.valueOf(date.get(1)));
                intent.putExtra("time", String.valueOf(time.get(2)));
                intent.putExtra("title", String.valueOf(title.get(3)));
                intent.putExtra("description", String.valueOf(description.get(4)));
                intent.putExtra("lati", String.valueOf(lati.get(5)));
                intent.putExtra("longi", String.valueOf(longi.get(6)));
                intent.putExtra("range", String.valueOf(range.get(7)));
                //intent.putExtra("noti", String.valueOf(noti.get(position)));
                //intent.putExtra("voice", String.valueOf(voice.get(position)));
                //intent.putExtra("vibrate", String.valueOf(vibrate.get(position)));

                context.startActivity(intent);

                //activity.startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return reminder_id.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView reminder_id_txt, reminder_date_txt, reminder_time_txt, reminder_title_txt, reminder_description_txt, reminder_range_txt, reminder_lati_txt, reminder_longi_txt;
        //CheckBox reminder_notification, reminder_voice, reminder_vibrate;
        LinearLayout mainLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            reminder_id_txt = itemView.findViewById(R.id.reminder_id_text);
            reminder_date_txt = itemView.findViewById(R.id.reminder_date);
            reminder_time_txt = itemView.findViewById(R.id.remidner_time);
            reminder_title_txt = itemView.findViewById(R.id.reminder_title);
            reminder_description_txt = itemView.findViewById(R.id.reminder_description);
            reminder_lati_txt = itemView.findViewById(R.id.reminder_lati);
            reminder_longi_txt = itemView.findViewById(R.id.reminder_longi);
            reminder_range_txt = itemView.findViewById(R.id.reminder_range);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}
