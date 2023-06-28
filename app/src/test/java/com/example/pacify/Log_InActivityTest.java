package com.example.pacify;

import android.content.res.Resources;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.collection.SimpleArrayMap;
import androidx.collection.SparseArrayCompat;
import androidx.core.app.ComponentActivity;
import androidx.fragment.app.FragmentController;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.ViewModelStore;

import com.google.firebase.auth.FirebaseAuth;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;


public class Log_InActivityTest {
    @Mock
    FirebaseAuth mAuth;
    @Mock
    FirebaseAuth.AuthStateListener authStateListener;
    @Mock
    EditText emailTextView;
    @Mock
    EditText passwordTextView;
    @Mock
    Button Btn;
    @Mock
    ProgressBar progressBar;
    @Mock
    AppCompatDelegate mDelegate;
    @Mock
    Resources mResources;
    @Mock
    FragmentController mFragments;
    @Mock
    LifecycleRegistry mFragmentLifecycleRegistry;
    @Mock
    SparseArrayCompat<String> mPendingFragmentActivityResults;
    @Mock
    LifecycleRegistry mLifecycleRegistry;
    //Field mSavedStateRegistryController of type SavedStateRegistryController - was not mocked since Mockito doesn't mock a Final class when 'mock-maker-inline' option is not set
    @Mock
    ViewModelStore mViewModelStore;
    //Field mOnBackPressedDispatcher of type OnBackPressedDispatcher - was not mocked since Mockito doesn't mock a Final class when 'mock-maker-inline' option is not set
    @Mock
    SimpleArrayMap<Class<? extends ComponentActivity.ExtraData>, ComponentActivity.ExtraData> mExtraDataMap;

    @InjectMocks
    LoginActivity loginActivity;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testOnStart() throws Exception {
        loginActivity.onStart();
    }

    @Test
    public void testOnCreate() throws Exception {
        loginActivity.onCreate(null);
    }

    @Test
    public void testLogin()
    {
        EditText email = loginActivity.findViewById(R.id.email);
        EditText pass = loginActivity.findViewById(R.id.passwd);
        Button loginButton = loginActivity.findViewById(R.id.login);

        email.setText("User10@email.com");
        pass.setText("password");

        loginButton.performClick();
        //assertTrue(loginActivity.);

    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme