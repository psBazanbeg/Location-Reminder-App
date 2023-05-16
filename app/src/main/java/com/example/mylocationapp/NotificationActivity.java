package com.example.mylocationapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class NotificationActivity extends AppCompatActivity {

    ImageButton notificationBtn, homeBtn, deleteBtn;
    RecyclerView recyclerView;
    FloatingActionButton addBtn;
    //ImageButton  bin;

    MyDatabaseHelper myDB;
    ArrayList<String> reminder_id, date, time, title, description, lati, longi, range;
    CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

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

        recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        addBtn = (FloatingActionButton) findViewById(R.id.fabAdd);
        //bin = (ImageButton) findViewById(R.id.bin);

        addBtn.setOnClickListener((view) -> {
            Intent intent = new Intent(NotificationActivity.this, HomeAcitvity.class);
            startActivity(intent);
        });

        myDB = new MyDatabaseHelper(NotificationActivity.this);
        reminder_id = new ArrayList<>();
        date = new ArrayList<>();
        time = new ArrayList<>();
        title = new ArrayList<>();
        description = new ArrayList<>();
        lati = new ArrayList<>();
        longi = new ArrayList<>();
        range = new ArrayList<>();

        storeDataInArrays();

        customAdapter = new CustomAdapter(NotificationActivity.this, this, reminder_id, date, time, title, description, lati, longi, range);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(NotificationActivity.this));

        /*bin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(), login.class);
                startActivity(i);
            }
        });*/


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            recreate();
        }
    }




    void storeDataInArrays(){
        Cursor cursor = myDB.readAllData();
        if(cursor.getCount() == 0)
            Toast.makeText(this, "No data!", Toast.LENGTH_SHORT).show();
        else{
            while (cursor.moveToNext()){
                reminder_id.add(cursor.getString(0));
                date.add(cursor.getString(1));
                time.add(cursor.getString(2));
                title.add(cursor.getString(3));
                description.add(cursor.getString(4));
                lati.add(cursor.getString(5));
                longi.add(cursor.getString(6));
                range.add(cursor.getString(7));
            }
        }
    }
}