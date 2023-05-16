package com.example.mylocationapp;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class UpdatedBottomSheetAcitvity extends AppCompatActivity implements View.OnClickListener {


    TextView bottomSheetLat;
    TextView bottomSheetLong;
    String lat, lon;
    Button useLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_sheet);

        bottomSheetLong = (TextView) findViewById(R.id.longitude_txt_box);
        bottomSheetLat = (TextView) findViewById(R.id.latitude_txt_box);
        useLocation = (Button) findViewById(R.id.getLocationBtn);

        useLocation.setOnClickListener(this);


        Bundle bundle = getIntent().getExtras();
        lat = bundle.getString("Marker_Lat");
        lon = bundle.getString("Marker_Long");
        bottomSheetLat.setText(lat);
        bottomSheetLong.setText(lon);


/*
        final Dialog dialog = new Dialog(getApplicationContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_bottom_sheet);

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);*/
    }


    public void showDialogSheet() {

        final Dialog dialog = new Dialog(getApplicationContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_bottom_sheet);

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    @Override
    public void onClick(View view) {
        Intent sendLocationIntent = new Intent(getApplicationContext(), UpdateActivity.class);
        sendLocationIntent.putExtra("Marker_Latitude", String.valueOf(lat));
        sendLocationIntent.putExtra("Marker_Longitude", String.valueOf(lon));
        startActivity(sendLocationIntent);
    }
}