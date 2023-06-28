package com.example.pacify;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private EditText emailTextView;
    private EditText passwordTextView;
    private Button Btn;

    private ProgressBar progressBar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();


        emailTextView = findViewById(R.id.email);
        passwordTextView = findViewById(R.id.passwd);
        Btn = findViewById(R.id.btnregister);
        progressBar= findViewById(R.id.progessbar);

        Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { registerNewUser(); }
        });
    }

    private void registerNewUser()
    {
        progressBar.setVisibility(View.VISIBLE);

        String email;
        String password;

        email = emailTextView.getText().toString().trim();
        password = passwordTextView.getText().toString();


        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(getApplicationContext(),
                    "Please Enter Email Address",
                    Toast.LENGTH_LONG).show();

            return;
        }
        if(TextUtils.isEmpty(password))
        {
            Toast.makeText(getApplicationContext(),
                    "Please Enter A Password",
                    Toast.LENGTH_LONG).show();

            return;
        }


        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            FirebaseUser user = mAuth.getCurrentUser();
                            firebaseDatabase = FirebaseDatabase.getInstance();
                            String id = mAuth.getCurrentUser().getUid();
                            databaseReference = FirebaseDatabase.getInstance()
                                    .getReference().child("user").child(id);

                            String Email;
                            String Password;

                            Email = emailTextView.getText().toString().trim();
                            Password = passwordTextView.getText().toString();

                            UserInfo register = new UserInfo(Email, Password);

                            databaseReference.setValue(register);

                            Toast.makeText(getApplicationContext(),
                                    "Registration Successful",
                                    Toast.LENGTH_LONG).show();


                            progressBar.setVisibility(View.GONE);

                            finish();

                            Intent intent = new Intent(RegistrationActivity.this,
                                    LoginActivity.class);

                            startActivity(intent);
                        }
                        else
                            {
                                FirebaseAuthException exception = (FirebaseAuthException)task.getException();

                                Toast.makeText(getApplicationContext(),
                                        "Registration Failed" + "" + exception.getMessage() + " " + ",Please Try Again",
                                        Toast.LENGTH_LONG).show();

                                Log.e("LoginActivity", "Failed Registration", exception);

                                progressBar.setVisibility(View.GONE);
                            }
                    }
                });
    }
}
