package com.example.pacify;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    private TextView home;
    private TextView home2;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        home = findViewById(R.id.measure);
        home.setText("Measure");

        home2 = findViewById(R.id.pss);
        home2.setText("PSS Test");

        firebaseAuth = FirebaseAuth.getInstance();


        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_nav);

        bottomNavigationView.setOnItemSelectedListener(
                new BottomNavigationView.OnItemSelectedListener()
                {

                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId())
                        {
                            case R.id.action_favourites:

                                Intent resultIntent = new Intent(HomeActivity.this, StoredResultsActivity.class);
                                startActivity(resultIntent);

                                Toast.makeText(getApplicationContext(),
                                        "Stored Results",
                                        Toast.LENGTH_SHORT).show();
                                break;

                            case R.id.action_home:
                                Toast.makeText(getApplicationContext(),
                                        "Home",
                                        Toast.LENGTH_SHORT).show();
                                break;

                            case R.id.action_signout:
                                Toast.makeText(getApplicationContext(),
                                        "Logged Out",
                                        Toast.LENGTH_SHORT).show();

                                final FirebaseAuth firebaseAuth;
                                firebaseAuth = FirebaseAuth.getInstance();
                                firebaseAuth.signOut();

                                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                                break;
                        }
                        return true;
                    }
                });
    }

    public void measure(View view) {
        Intent intent = new Intent(HomeActivity.this,
                CameraActivity.class);
        startActivity(intent);
    }

    public void pssTest(View view)
    {
        Intent intent = new Intent(HomeActivity.this,
                PSSActivity.class);
        startActivity(intent);
    }


}
