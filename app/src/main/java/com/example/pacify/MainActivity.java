package com.example.pacify;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView pacify;
    private TextView pacify2;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pacify);

        pacify = findViewById(R.id.login);
        pacify.setText("Login");

        pacify2 = findViewById(R.id.register);
        pacify2.setText("Register");

    }


    public void register(View view) {
        Intent intent = new Intent(MainActivity.this,
                RegistrationActivity.class);
        startActivity(intent);
    }

    public void login(View view) {
        Intent intent = new Intent(MainActivity.this,
                LoginActivity.class);
        startActivity(intent);
    }

}
