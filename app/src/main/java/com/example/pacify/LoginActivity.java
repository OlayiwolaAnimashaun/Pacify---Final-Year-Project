package com.example.pacify;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    public FirebaseAuth.AuthStateListener authStateListener;

    private EditText emailTextView;
    private EditText passwordTextView;

    private Button Btn;
    private ProgressBar progressBar;


    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();


        emailTextView = findViewById(R.id.email);
        passwordTextView = findViewById(R.id.password);
        Btn = findViewById(R.id.login);

        progressBar = findViewById(R.id.progessbar);


        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if(firebaseAuth.getCurrentUser() == null)
                {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };


        Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { loginUserAccount(); }
        });

    }


    private void loginUserAccount()
    {
        progressBar.setVisibility(View.VISIBLE);

        String email, password;
        email = emailTextView.getText().toString().trim();
        password = passwordTextView.getText().toString();


        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(getApplicationContext(),
                    "Enter Your Email Address Please",
                    Toast.LENGTH_LONG).show();

            return;
        }
        if (TextUtils.isEmpty(password))
        {
            Toast.makeText(getApplicationContext(),
                    "Enter Your Password Please",
                    Toast.LENGTH_LONG).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(getApplicationContext(),
                                    "User Login Successful",
                                    Toast.LENGTH_LONG).show();

                            //Hide Progress Bar
                            progressBar.setVisibility(View.GONE);

                            //When sign in is successful, intent to home activity
                            Intent intent = new Intent(LoginActivity.this,
                                    HomeActivity.class);
                            startActivity(intent);
                        }else
                            {
                                //Failed Login
                                Toast.makeText(getApplicationContext(),
                                        "Login Failed",
                                        Toast.LENGTH_LONG).show();

                                //Hide Progress Bar
                                progressBar.setVisibility(View.GONE);
                            }
                    }
                });

    }
}
