package com.example.mylocationapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class UpdateActivity extends AppCompatActivity {

    ImageButton notificationBtn, homeBtn, deleteBtn , clearBtn;
    FloatingActionButton floatingActionUpdateButton;
    ImageButton locationBtn;
    TextView date2, time2, range2, latitudeIsAdded2, longitudeIsAdded2, textRange;
    EditText title2, description2;
    SeekBar seekBar;
    int seekBarValue;
    //CheckBox notificationBox, voiceBox, vibrateBox;

    String  Sid, Sdate, Stime, Stitle, Sdescription, Slat, Slon, Srange, Snoti, Svoice, Svibrate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        date2 = findViewById(R.id.textViewDate2);
        time2 = findViewById(R.id.textViewTime2);
        title2 = findViewById(R.id.editTextTitle2);
        description2 = findViewById(R.id.editTextDescription2);
        latitudeIsAdded2 = findViewById(R.id.addLatitudeTxtBox2);
        longitudeIsAdded2 = findViewById(R.id.addLongitudeTxtBox2);
        range2 = findViewById(R.id.textViewRange2);


        textRange = (TextView) findViewById(R.id.textViewRange);
        seekBar = (SeekBar) findViewById(R.id.seekBarId);
        //notificationBox = findViewById(R.id.checkBoxNotification2);
        //voiceBox = findViewById(R.id.checkBoxVoice2);
        //vibrateBox = findViewById(R.id.checkBoxVibrate2);
        floatingActionUpdateButton = findViewById(R.id.fab2);
        locationBtn=(ImageButton) findViewById(R.id.addLocationBtn2);

        clearBtn = (ImageButton) findViewById(R.id.clearBtn);
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        notificationBtn = (ImageButton) findViewById(R.id.notificationBtn);
        notificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), NotificationActivity.class);
                startActivity(i);
            }
        });

        homeBtn = (ImageButton) findViewById(R.id.homeBtn);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), HomeAcitvity.class);
                startActivity(i);
            }
        });

        //First this should be call
        getAndSetIntentData();

        floatingActionUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(UpdateActivity.this);
                //After this can be called
                myDB.updateData(Sid, Sdate, Stime, Stitle, Sdescription, Slat, Slon, Srange);

                Intent intent = new Intent(getApplicationContext(), NotificationActivity.class);
                startActivity(intent);
            }
        });

        locationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent locationIntent = new Intent(getApplicationContext(), UpdatedMapActivity.class);
                startActivity(locationIntent);
            }
        });

    }


    void getAndSetIntentData(){
        if(getIntent().hasExtra("id") && getIntent().hasExtra("date")
                && getIntent().hasExtra("time") && getIntent().hasExtra("title")
                && getIntent().hasExtra("description") && getIntent().hasExtra("lati")
                && getIntent().hasExtra("longi") && getIntent().hasExtra("range")) {

            //Getting data from intent
            Sid = getIntent().getStringExtra("id");
            Sdate = getIntent().getStringExtra("date");
            Stime = getIntent().getStringExtra("time");
            Stitle = getIntent().getStringExtra("title");
            Sdescription = getIntent().getStringExtra("description");
            Slat = getIntent().getStringExtra("lati");
            Slon = getIntent().getStringExtra("longi");
            Srange = getIntent().getStringExtra("range");
            //Snoti = getIntent().getStringExtra("noti");
            //Svoice = getIntent().getStringExtra("voice");
            //Svibrate = getIntent().getStringExtra("vibrate");


            //Setting Data Intent
            date2.setText(Sdate);
            time2.setText(Stime);
            title2.setText(Stitle);
            description2.setText(Sdescription);
            latitudeIsAdded2.setText(Slat);
            longitudeIsAdded2.setText(Slon);
            range2.setText(Srange);
            //notificationBox.setText(Snoti);
            //voiceBox.setText(Svoice);
            //vibrateBox.setText(Svibrate);

        }else{
            Toast.makeText(this, "No Data!", Toast.LENGTH_SHORT).show();
        }
    }
}