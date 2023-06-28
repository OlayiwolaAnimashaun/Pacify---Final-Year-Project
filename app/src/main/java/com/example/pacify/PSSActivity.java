package com.example.pacify;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class PSSActivity extends AppCompatActivity {

    public FirebaseAuth mAuth;
    public FirebaseDatabase firebaseDatabase;
    public DatabaseReference databaseReference;
    public FirebaseAuth.AuthStateListener authStateListener;

    public int score = 0;
    public Button Btn;


    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pss);

        mAuth = FirebaseAuth.getInstance();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if(firebaseAuth.getCurrentUser() == null)
                {
                    Intent intent = new Intent(PSSActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        };


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.answers,
                R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        Spinner spinner1 = findViewById(R.id.spin1);
        spinner1.setAdapter(adapter);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position == 0)
                {
                    score = score;
                }
                if(position == 1)
                {
                    score = score + 1;
                }
                if (position == 2)
                {
                    score = score + 2;
                }
                if (position == 3)
                {
                    score = score + 3;
                }
                if (position == 4)
                {
                    score = score + 4;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });


        Spinner spinner2 = findViewById(R.id.spin2);
        spinner2.setAdapter(adapter);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0)
                {
                    score = score;
                }
                if(position == 1)
                {
                    score = score + 1;
                }
                if (position == 2)
                {
                    score = score + 2;
                }
                if (position == 3)
                {
                    score = score + 3;
                }
                if (position == 4)
                {
                    score = score + 4;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        Spinner spinner3 = findViewById(R.id.spin3);
        spinner3.setAdapter(adapter);
        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0)
                {
                    score = score;
                }
                if(position == 1)
                {
                    score = score + 1;
                }
                if (position == 2)
                {
                    score = score + 2;
                }
                if (position == 3)
                {
                    score = score + 3;
                }
                if (position == 4)
                {
                    score = score + 4;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner spinner4 = findViewById(R.id.spin4);
        spinner4.setAdapter(adapter);
        spinner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0)
                {
                    score = score + 4;
                }
                if (position == 1)
                {
                    score = score + 3;
                }
                if (position == 2)
                {
                    score = score + 2;
                }
                if (position == 3)
                {
                    score = score + 1;
                }
                if (position == 4)
                {
                    score = score;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                score = 0;
            }
        });

        Spinner spinner5 = findViewById(R.id.spin5);
        spinner5.setAdapter(adapter);
        spinner5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0)
                {
                    score = score + 4;
                }
                if (position == 1)
                {
                    score = score + 3;
                }
                if (position == 2)
                {
                    score = score + 2;
                }
                if (position == 3)
                {
                    score = score + 1;
                }
                if (position == 4)
                {
                    score = score;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        Spinner spinner6 = findViewById(R.id.spin6);
        spinner6.setAdapter(adapter);
        spinner6.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0)
                {
                    score = score;
                }
                if(position == 1)
                {
                    score = score + 1;
                }
                if (position == 2)
                {
                    score = score + 2;
                }
                if (position == 3)
                {
                    score = score + 3;
                }
                if (position == 4)
                {
                    score = score + 4;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        Spinner spinner7 = findViewById(R.id.spin7);
        spinner7.setAdapter(adapter);
        spinner7.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0)
                {
                    score = score + 4;
                }
                if (position == 1)
                {
                    score = score + 3;
                }
                if (position == 2)
                {
                    score = score + 2;
                }
                if (position == 3)
                {
                    score = score + 1;
                }
                if (position == 4)
                {
                    score = score;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        Spinner spinner8 = findViewById(R.id.spin8);
        spinner8.setAdapter(adapter);
        spinner8.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0)
                {
                    score = score + 4;
                }
                if (position == 1)
                {
                    score = score + 3;
                }
                if (position == 2)
                {
                    score = score + 2;
                }
                if (position == 3)
                {
                    score = score + 1;
                }
                if (position == 4)
                {
                    score = score;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        Spinner spinner9 = findViewById(R.id.spin9);
        spinner9.setAdapter(adapter);
        spinner9.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0)
                {
                    score = score;
                }
                if(position == 1)
                {
                    score = score + 1;
                }
                if (position == 2)
                {
                    score = score + 2;
                }
                if (position == 3)
                {
                    score = score + 3;
                }
                if (position == 4)
                {
                    score = score + 4;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        Spinner spinner10 = findViewById(R.id.spin10);
        spinner10.setAdapter(adapter);
        spinner10.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0)
                {
                    score = score;
                }
                if(position == 1)
                {
                    score = score + 1;
                }
                if (position == 2)
                {
                    score = score + 2;
                }
                if (position == 3)
                {
                    score = score + 3;
                }
                if (position == 4)
                {
                    score = score + 4;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        Btn = findViewById(R.id.total_score);
        Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

                View totalScoreWindow = layoutInflater.inflate(R.layout.totalscore_popup_window, null);



                PopupWindow popupWindow = new PopupWindow(totalScoreWindow, 1000, 900, true);

                if(score >= 0 && score <= 13)
                {
                    ((TextView)popupWindow.getContentView().findViewById(R.id.score_final))
                            .setText(getString(R.string.stress_score) + " " + score + " " + "You are at a Low Stress Level");
                }
                if (score >= 14 && score <= 26)
                {
                    ((TextView)popupWindow.getContentView().findViewById(R.id.score_final))
                            .setText(getString(R.string.stress_score) + " " + score + " " + "You are at a Moderate Stress Level");
                }
                if (score >= 27 && score <= 40)
                {

                    ((TextView)popupWindow.getContentView().findViewById(R.id.score_final))
                            .setText(getString(R.string.stress_score) + " " + score + " " + "You are at a High Stress Level");
                }


                ((Button)popupWindow.getContentView().findViewById(R.id.close)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });


                popupWindow.getContentView().findViewById(R.id.upload_results).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                       FirebaseUser user = mAuth.getCurrentUser();
                       firebaseDatabase = FirebaseDatabase.getInstance();
                       String id = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                       databaseReference = FirebaseDatabase.getInstance()
                                            .getReference().child("pss_scores").child(id).push();


                                    int finalScore = score;
                                    String message = "";

                             if(score >= 0 && score <= 13)
                                 {
                                     message = "You are at a Low Stress Level";
                                 }
                                 if (score >= 14 && score <= 26)
                                 {
                                     message = "You are at a Moderate Stress Level";
                                 }
                                 if (score >= 27 && score <= 40)
                                 {
                                     message = "You are at a High Stress Level";
                                 }


                                 StorePSS fScore = new StorePSS(finalScore, message);
                                  databaseReference.setValue(fScore);

                                    Toast.makeText(getApplicationContext(),
                                            "Your Results Have Been Uploaded",
                                            Toast.LENGTH_LONG).show();


                    }
                });


                popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);

                Toast.makeText(getApplicationContext(),
                        "Your Stress Score Is: " + score,
                        Toast.LENGTH_LONG).show();
            }
        });


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

                                Intent resultIntent = new Intent(PSSActivity.this, StoredResultsActivity.class);
                                startActivity(resultIntent);

                                Toast.makeText(getApplicationContext(),
                                        "Stored Results",
                                        Toast.LENGTH_SHORT).show();
                                break;

                            case R.id.action_home:
                                Toast.makeText(getApplicationContext(),
                                        "Home",
                                        Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(PSSActivity.this, HomeActivity.class);
                                startActivity(intent);
                                break;

                            case R.id.action_signout:
                                Toast.makeText(getApplicationContext(),
                                        "Logged Out",
                                        Toast.LENGTH_SHORT).show();

                                final FirebaseAuth firebaseAuth;
                                firebaseAuth = FirebaseAuth.getInstance();
                                firebaseAuth.signOut();

                                Intent intent2 = new Intent(PSSActivity.this, MainActivity.class);
                                intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent2);
                                finish();
                                break;
                        }
                        return true;
                    }
                });
    }

}
