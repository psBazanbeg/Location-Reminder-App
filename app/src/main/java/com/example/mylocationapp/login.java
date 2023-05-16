package com.example.mylocationapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class login extends AppCompatActivity {

    private EditText title,des;
    private TextView create;
    private Button login;
    private Context context;
    private MyDatabaseHelper dbHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = this;
        dbHandler = new MyDatabaseHelper(context);
        title = (EditText) findViewById(R.id.name);
        des = (EditText) findViewById(R.id.userpass);
        login = findViewById(R.id.button3);
        create= findViewById(R.id.textView);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),signin.class);
                startActivity(intent);

            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = title.getText().toString();
                String pass = des.getText().toString();

                if (user.equals("") || pass.equals("")){
                    Toast.makeText(login.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();}
                else {
                    Boolean checkuserpass = dbHandler.checkusernamepassword(user, pass);
                    if(checkuserpass == true) {
                        Toast.makeText(login.this, "sucsessful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(),HomeAcitvity.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(login.this, "Invalid", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });



    }
}