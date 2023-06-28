package com.example.pacify;

import android.widget.Button;
import android.widget.EditText;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.google.firebase.auth.FirebaseAuth;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LoginActivityTest {

    LoginActivity loginActivity;

    @Mock
    FirebaseAuth mAuth;


    @Test
    public void testLogin() {

        EditText email = loginActivity.findViewById(R.id.email);
        EditText pass = loginActivity.findViewById(R.id.passwd);
        Button loginButton = loginActivity.findViewById(R.id.login);

        email.setText("User8@email.com");
        pass.setText("password");

        String user = email.getText().toString().trim();
        String password = pass.getText().toString();

        loginButton.performClick();



        //assertTrue("User Logged In", );
    }
}

