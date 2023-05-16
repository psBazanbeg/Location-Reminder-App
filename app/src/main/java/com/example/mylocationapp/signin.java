package com.example.mylocationapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class signin extends AppCompatActivity {

    private EditText user,pass;
    private Button signin;
    private Context context;
    private Long updateDate;
    private MyDatabaseHelper dbHandler;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        context = this;
        dbHandler = new MyDatabaseHelper(context);
        user = findViewById(R.id.username);
        pass = findViewById(R.id.pass);
        signin = findViewById(R.id.button2);


        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Data1 = user.getText().toString();
                String Data2 = pass.getText().toString();
               /* Intent intent = new Intent( HomeAcitvity.this, notofication.class);
                intent.putExtra("Data1",Data1);
                intent.putExtra("Data2",Data2);
                startActivity(intent);
               finish();*/
                ToDo toDo = new ToDo(Data1,Data2);
                dbHandler.addToDoUSER(toDo);
                startActivity(new Intent(context,login.class));

            }
        });
    }
}