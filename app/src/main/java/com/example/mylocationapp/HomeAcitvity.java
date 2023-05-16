package com.example.mylocationapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class HomeAcitvity extends AppCompatActivity {

    ImageButton notificationBtn, homeBtn, deleteBtn;
    FloatingActionButton floatingActionButton;
    TextView textDate, textTime, textRange, latitudeIsAdded, longitudeIsAdded;
    EditText title, description;
    CheckBox notificationBox, voiceBox, vibrateBox;
    SeekBar seekBar;
    ImageButton locationBtn;
    String lat, lon;
    double seekBarValue;

    public static final String TAG = "MainActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //status bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        floatingActionButton=(FloatingActionButton) findViewById(R.id.fab);
        locationBtn=(ImageButton) findViewById(R.id.addLocationBtn);

        textDate=(TextView) findViewById(R.id.textViewDate);
        textTime=(TextView) findViewById(R.id.textViewTime);
        title=(EditText) findViewById(R.id.editTextTitle);
        description=(EditText) findViewById(R.id.editTextDescription);
        notificationBox=(CheckBox) findViewById(R.id.checkBoxNotification);
        voiceBox=(CheckBox) findViewById(R.id.checkBoxVoice);
        vibrateBox=(CheckBox) findViewById(R.id.checkBoxVibrate);
        //End of the Status Bar

        textDate.setText(getCurrentTime());
        textTime.setText(getCurrentDate());
        //End of the Status Bar

        //Seek Bar Code
        textRange = (TextView) findViewById(R.id.textViewRange);
        seekBar = (SeekBar) findViewById(R.id.seekBarId);

        //get location from MapActivity
        latitudeIsAdded = (TextView) findViewById(R.id.addLatitudeTxtBox);
        longitudeIsAdded = (TextView) findViewById(R.id.addLongitudeTxtBox);

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

        deleteBtn = (ImageButton) findViewById(R.id.deleteBtn);
        /*deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), .class);
                startActivity(i);
            }
        });*/

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(HomeAcitvity.this);
                myDB.addReminder(textDate.getText().toString(),
                        textTime.getText().toString(),
                        title.getText().toString().trim(),
                        description.getText().toString().trim(),
                        Double.valueOf(latitudeIsAdded.getText().toString()),
                        Double.valueOf(longitudeIsAdded.getText().toString()),
                        seekBarValue,
                        notificationBox.getText().toString(),
                        voiceBox.getText().toString(),
                        vibrateBox.getText().toString());

                Intent locationIntent = new Intent(getApplicationContext(), NotificationActivity.class);
                startActivity(locationIntent);

                /*myDataBase mdb = new myDataBase(HomeAcitvity.this);
                mdb.addBook(title.getText().toString().trim(), description.getText().toString().trim());*/
            }
        });

        locationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent locationIntent = new Intent(getApplicationContext(), MapActivity.class);
                startActivity(locationIntent);
            }
        });


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                textRange.setText("Note : Matching Range in Meters ("+String.valueOf(i)+")");
                seekBarValue=i;

                /*Bundle bundle = new Bundle();
                bundle.putInt("Radius", seekBarValue);
                Intent circleRadiusIntent = new Intent(HomeAcitvity.this, MapActivity.class);
                circleRadiusIntent.putExtras(bundle);
                startActivity(circleRadiusIntent);*/
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        if(isServicesOk()){
            init();
        }


        //Set Location to the text view
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            lat = bundle.getString("Marker_Latitude");
            lon = bundle.getString("Marker_Longitude");
        }else{
            lat="";
            lon="";
        }
        latitudeIsAdded.setText(lat);
        longitudeIsAdded.setText(lon);
    }

    private String getCurrentDate() {
        return new SimpleDateFormat( "dd:MM:yyyy", Locale.getDefault()).format(new Date());
    }

    private String getCurrentTime() {
        return new SimpleDateFormat("hh:mm a",Locale.getDefault()).format(new Date());
    }

    private void init(){
        locationBtn = (ImageButton) findViewById(R.id.addLocationBtn);
        locationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeAcitvity.this, MapActivity.class);
                startActivity(intent);
            }
        });
    }

    public boolean isServicesOk(){
        Log.d(TAG, "isServicesOk: Checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(HomeAcitvity.this);

        if(available == ConnectionResult.SUCCESS){
            Log.d(TAG, "isServicesOk: Google Play Services are working");
            return true;
        }

        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            Log.d(TAG, "isServicesOk: An error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(HomeAcitvity.this, available, ERROR_DIALOG_REQUEST);
        }

        else{
            Toast.makeText(this, "You can't make map request", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}