package com.example.mylocationapp;

import android.Manifest;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.protobuf.StringValue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener, GoogleMap.OnMarkerDragListener {



    private static final String TAG = "MapActivity";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE =1234;
    private static final float DEFAULT_ZOOM = 18f;

    double end_latitude=0.0;
    double end_longitude=0.0;
    double latitude=0.0;
    double longitude=0.0;
    double addMarker_endLatitude=0.0;
    double addMarker_endLongitude=0.0;
    double alarm_location_latitude = 0;
    double alarm_location_longitutde = 0;
    String distance;
    final static int REQUEST_CODE = 1 ;
    boolean state = false ;
    long circleEnterTime;
    PendingIntent pendingIntent;

    //widgets
    private SearchView mSearchText;
    private ImageView mGps;
    SeekBar seekBar;
    private int radius;
    Circle circle;

    //vars
    private Boolean mLocationPermissionGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mfusedLocationProviderClient;
    MarkerOptions origin, destination;


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        Toast.makeText(this, "Map is Ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady : map is ready");
        mMap = googleMap;

        if (mLocationPermissionGranted) {
            getDeviceLocation();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            init(); //Search bar
        }

        //Add marker option
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                //creating marker
                MarkerOptions markerOptions = new MarkerOptions();

                //set marker position
                markerOptions.position(latLng);

                //assign latitude and longitude to variables
                addMarker_endLatitude = markerOptions.getPosition().latitude;
                addMarker_endLongitude = markerOptions.getPosition().longitude;

                //System.out.println("Latitude is AAAAAAAAAAAAAAAAAAAAA : "+String.valueOf(addMarker_endLatitude));
                //System.out.println("Latitude is BBBBBBBBBBBBBBBBBBBB : "+String.valueOf(addMarker_endLongitude));

                Intent sendLatlngIntent = new Intent(getApplicationContext(), BottomSheetAcitvity.class);
                sendLatlngIntent.putExtra("Marker_Lat", String.valueOf((double) Math.round(addMarker_endLatitude*1000)/1000));
                sendLatlngIntent.putExtra("Marker_Long", String.valueOf((double) Math.round(addMarker_endLongitude*1000)/1000));
                startActivity(sendLatlngIntent);

                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(MapActivity.this);
                bottomSheetDialog.setContentView(R.layout.activity_bottom_sheet);

                //bottomSheetLat.setText(Double.toString(addMarker_endLatitude));
                //bottomSheetLong.setText(String.format("%.3f", addMarker_endLongitude));

                //clear previous clicked position
                mMap.clear();

                //zoom the marker
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM));

                //add marker on the map
                mMap.addMarker(markerOptions);


                /* **** Get the distance between the marker and the my location **** */
                MarkerOptions mo = new MarkerOptions();
                mo.title("Distance");

                float result[] = new float[10];
                Location.distanceBetween(latitude, longitude, addMarker_endLatitude, addMarker_endLongitude, result);

                distance = String.format("%.1f", result[0]/1000);
                //set latitude and longitude on the marker
                markerOptions.title("Distance :" +distance);

                origin = new MarkerOptions().position(new LatLng(latitude, longitude)).title("My Location").snippet("origin");
                destination = new MarkerOptions().position(new LatLng(addMarker_endLatitude, addMarker_endLongitude)).title("Latitude : "+latLng.latitude + ", Longitude : "+latLng.longitude).snippet("distance : "+distance+ " Km");
                mMap.addMarker(destination);
                mMap.addMarker(origin);

                Toast.makeText(MapActivity.this, "Distance : "+distance+" Km", Toast.LENGTH_SHORT).show();


                showCircle(addMarker_endLatitude,addMarker_endLongitude);
                /* **** End **** */


                if(IsInCircle()){
                    System.out.println("insideeeeeeeeeeeeeeeeeeeeee");
                    Alarm_set(circleEnterTime);
                }
                else{
                    System.out.println("oooooooooooooooooooooutside");
                }

            }
        });

        mMap.setOnMarkerClickListener(this);
        mMap.setOnMarkerDragListener(this);
    }



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        mSearchText = (SearchView) findViewById(R.id.input_search);
        mGps = (ImageView) findViewById(R.id.ic_gps);
        seekBar = (SeekBar) findViewById(R.id.seekBarId);

        //bottomSheetLat = (TextView) findViewById(R.id.latitude_txt_box);
        //bottomSheetLong = (TextView) findViewById(R.id.longitude_txt_box);
        //distance = (TextView) findViewById(R.id.distanceLevel);
        getLocationPermission();

        //status bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }

    //Search Bar
    private void init(){
        Log.d(TAG, "init: initializing");

        mSearchText.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                String location = mSearchText.getQuery().toString();
                List<Address> addressList = null;

                if (location != null || !location.equals("")){
                    geoLocate();

                    MarkerOptions mo = new MarkerOptions();
                    mo.title("Distance");

                    float result[] = new float[10];
                    Location.distanceBetween(latitude, longitude, end_latitude, end_longitude, result);

                    distance = String.format("%.1f", result[0]/1000);

                    origin = new MarkerOptions().position(new LatLng(latitude, longitude)).title("My Location").snippet("origin");
                    destination = new MarkerOptions().position(new LatLng(end_latitude, end_longitude)).title(mSearchText.toString()).snippet("distance : "+distance+ " Km");
                    mMap.addMarker(destination);
                    mMap.addMarker(origin);

                    Toast.makeText(MapActivity.this, "Distance : "+distance+" Km", Toast.LENGTH_SHORT).show();
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });


        mGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked gps icon");
                getDeviceLocation();
            }
        });
    }

    private void geoLocate(){
        Log.d(TAG, "geoLocate: geolocating");

        String searchString = mSearchText.getQuery().toString();

        Geocoder geocoder =  new Geocoder(MapActivity.this);
        List <Address> list = new ArrayList<>();

        try {
            list = geocoder.getFromLocationName(searchString, 1);
        }catch(IOException e){
            Log.d(TAG, "geoLocate: IOException" + e.getMessage());
        }

        Address address = list.get(0);
        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
        mMap.addMarker(new MarkerOptions().position(latLng).title(searchString));
        end_latitude = address.getLatitude();
        end_longitude = address.getLongitude();
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM));
        mMap.clear();
        showCircle(end_latitude, end_longitude);
    }
    //End of the Search Bar


    //This method track the current location of the device
    private void getDeviceLocation(){
        Log.d(TAG, "getDeviceLocation : getting the device current location");
        mfusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            if(mLocationPermissionGranted){
                Task location = mfusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        Location currentLocation = (Location) task.getResult();
                        if(task.isSuccessful() && task.getResult() != null){
                            Log.d(TAG, "onComplete: Found Location!");

                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM, "My Location");
                            //mMap.animateCamera( CameraUpdateFactory.zoomTo( 16.0f ) );  // zoom in
                            //assign the latitude and longitude of the current location to the variables
                            latitude=currentLocation.getLatitude();
                            longitude=currentLocation.getLongitude();
                        }
                        else{
                            Log.d(TAG, "onComplete: Current Location is Null");
                            Toast.makeText(MapActivity.this, "Unable to get the Current Location, turn on GPS", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }catch(SecurityException e){
            Log.e(TAG, "getDeviceLocation : SecurityException: "+ e.getMessage());
        }
    }

    private void moveCamera(LatLng latLng, float zoom, String title){
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude+ ", lng: " + latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        if (!title.equals("My Location")){
            MarkerOptions options = new MarkerOptions().position(latLng).title(title);
            mMap.addMarker(options);
        }
    }

    private void initMap(){
        Log.d(TAG, "initMap : initialising map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapActivity.this);
    }

    //get permission method
    private void getLocationPermission(){
        Log.d(TAG, "getLocationPermission :  getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(), COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionGranted=true;
                initMap();
            }
            else{
                ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        }
        else{
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "onRequestPermissionsResult : called.");
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++){
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionGranted = false;
                            return;
                        }
                    }
                    mLocationPermissionGranted = true;
                    //initialise our map
                    initMap();
                }
            }
        }
    }

    private void showCircle(double lat, double lon){
        //Bundle extras = getIntent().getExtras();
        //double rad = extras.getDouble("Radius");
        CircleOptions circleOptions = new CircleOptions()
                .center(new LatLng(lat, lon))
                .radius(170)
                .strokeColor(Color.rgb(205,91,69))
                .fillColor(Color.argb(70, 255, 114, 86));

        circle = mMap.addCircle(circleOptions);
    }

    // Checks whether user is inside of circle or not
    public boolean IsInCircle(){
        float distance[] ={0,0,0};
        Location.distanceBetween(latitude, longitude, addMarker_endLatitude, addMarker_endLongitude, distance);

        //Location.distanceBetween(latitude, longitude, circle.getCenter().latitude, circle.getCenter().longitude, distance);
        if( distance[0] > circle.getRadius())
            return false;
        else{
            circleEnterTime = System.currentTimeMillis();
            //System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX : "+circleEnterTime);
            //Alarm_set(circleEnterTime);
            return true;
        }
    }

    private void Alarm_set(long timeInMillis) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, MyBroadcastReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent);
        Toast.makeText(this, "Your Alarm is Set", Toast.LENGTH_SHORT).show();
    }


    //-----------After Alarm Set ---------------------
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data); ///////
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            if (data.hasExtra("alarm_location_latitude") && data.hasExtra("alarm_location_longitude")) {
                state = true;/*
                alarm_location_latitude = data.getExtras().getDouble("alarm_location_latitude");
                alarm_location_longitutde = data.getExtras().getDouble("alarm_location_longitude");

                location = new LatLng(alarm_location_latitude, alarm_location_longitutde);
                mMap.addMarker(new MarkerOptions().position(location).title("Alarm Location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(location));

                // Add a circle of radius 100 meter
                circle = mMap.addCircle(new CircleOptions()
                        .center(new LatLng(alarm_location_latitude, alarm_location_longitutde))
                        .radius(100).strokeColor(Color.RED).fillColor(Color.argb(50, 255, 114, 86)));
                */
                //--------------- Check user is in Range or Not after 5 Seconds --------
                final Handler handler = new Handler();
                final int delay = 5000; //milliseconds
                handler.postDelayed(new Runnable() {
                    public void run() {
                        //do something
                        getDeviceLocation();
                        if (IsInCircle()) {
                            if (state == true) {
                                Intent intent = new Intent(getApplicationContext(), MyBroadcastReceiver.class);
                                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                                        getApplicationContext(), 234324243, intent, 0);
                                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                                alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
                                        + (100), pendingIntent);
                                state = false;
                            }
                        }
                        handler.postDelayed(this, delay);
                    }
                }, delay);


            }
        }

    }




    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        return false;
    }

    @Override
    public void onMarkerDrag(@NonNull Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(@NonNull Marker marker) {

    }

    @Override
    public void onMarkerDragStart(@NonNull Marker marker) {

    }

    //Method that add the bottom sheet
    /*private void showDialogSheet() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_sheet);

        //bottomSheetLong.setText(Double.toString(addMarker_endLatitude));

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }*/
}
