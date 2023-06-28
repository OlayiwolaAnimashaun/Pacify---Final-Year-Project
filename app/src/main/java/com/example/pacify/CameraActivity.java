package com.example.pacify;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.TextureView;
import android.view.Surface;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Objects;

public class CameraActivity extends Activity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private static final int CREATE_REQUEST_CODE = 0;
    private Analyzer analyzer;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    public String string;

    private final int REQUEST_CODE_CAMERA = 0;

    public static final int MESSAGE_UPDATE_REALTIME = 1;
    public static final int MESSAGE_UPDATE_FINAL = 2;
    public static final int MESSAGE_CAMERA_NOT_AVAILABLE = 3;

    private static final int MENU_INDEX_NEW_MEASUREMENT = 0;
    private static final int MENU_INDEX_EXPORT_RESULT = 1;

    public enum VIEW_STATE {
        MEASUREMENT,
        SHOW_RESULTS
    }

    private boolean justShared = false;

    @SuppressLint("HandlerLeak")
    private final Handler mainHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            if (msg.what ==  MESSAGE_UPDATE_REALTIME) {
                ((TextView) findViewById(R.id.textView)).setText(msg.obj.toString());
            }

            if (msg.what == MESSAGE_UPDATE_FINAL) {
                ((TextView) findViewById(R.id.outputView)).setText(msg.obj.toString());

                String str = ((TextView) findViewById(R.id.outputView)).getText().toString();

                try
                {
                    int num = Integer.parseInt(str.replaceAll("[\\D]", ""));

                    if(num > 600 && num < 1000)
                    {
                        ((TextView) findViewById(R.id.outputView2))
                                .setText("You are within the normal range for your resting heart rate, test again later and compare results");
                    }
                    if(num < 600)
                    {
                        ((TextView) findViewById(R.id.outputView2))
                                .setText("This is below the normal range for your resting heart rate, this can be an indicator of stress on the body");
                    }
                    if (num > 1000)
                    {
                        ((TextView) findViewById(R.id.outputView2))
                                .setText("This is above the average range for your resting heart rate, this can be an indicator of stress on the body");
                    }

                }
                catch (NumberFormatException e) {
                    e.printStackTrace();
                }


                Menu appMenu = ((Toolbar) findViewById(R.id.toolbar)).getMenu();

                setViewState(VIEW_STATE.SHOW_RESULTS);
            }

            if (msg.what == MESSAGE_CAMERA_NOT_AVAILABLE) {
                Log.println(Log.WARN, "camera", msg.obj.toString());

                ((TextView) findViewById(R.id.textView)).setText(
                        R.string.camera_not_found
                );
                analyzer.stop();
            }
        }
    };

    private final CameraService cameraService = new CameraService(this, mainHandler);

    @Override
    protected void onResume() {
        super.onResume();

        analyzer = new Analyzer(this, findViewById(R.id.graphTextureView), mainHandler);

        TextureView cameraTextureView = findViewById(R.id.textureView2);
        SurfaceTexture previewSurfaceTexture = cameraTextureView.getSurfaceTexture();

        if ((previewSurfaceTexture != null) && !justShared) {
            Surface previewSurface = new Surface(previewSurfaceTexture);

            // show warning when there is no flash
            if (!this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
                Snackbar.make(
                        findViewById(R.id.constraintLayout),
                        getString(R.string.noFlashWarning),
                        Snackbar.LENGTH_LONG
                ).show();
            }

            ((Toolbar) findViewById(R.id.toolbar)).getMenu().getItem(MENU_INDEX_NEW_MEASUREMENT).setVisible(false);

            cameraService.start(previewSurface);
            analyzer.measurePulse(cameraTextureView, cameraService);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraService.stop();
        if (analyzer != null) analyzer.stop();
        analyzer = new Analyzer(this, findViewById(R.id.graphTextureView), mainHandler);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if(firebaseAuth.getCurrentUser() == null)
                {
                    Intent intent = new Intent(CameraActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        };


        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},
                REQUEST_CODE_CAMERA);

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
                                Toast.makeText(getApplicationContext(),
                                        "Stored Results",
                                        Toast.LENGTH_SHORT).show();

                                Intent resultIntent = new Intent(CameraActivity.this, StoredResultsActivity.class);
                                startActivity(resultIntent);
                                break;

                            case R.id.action_home:
                                Toast.makeText(getApplicationContext(),
                                        "Home",
                                        Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(CameraActivity.this, HomeActivity.class);
                                startActivity(intent);
                                break;

                            case R.id.action_signout:
                                Toast.makeText(getApplicationContext(),
                                        "Logged Out",
                                        Toast.LENGTH_SHORT).show();

                                final FirebaseAuth firebaseAuth;
                                firebaseAuth = FirebaseAuth.getInstance();
                                firebaseAuth.signOut();

                                Intent intent2 = new Intent(CameraActivity.this, MainActivity.class);
                                intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent2);
                                finish();
                                break;
                        }
                        return true;
                    }
                }
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_CAMERA) {
            if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Snackbar.make(
                        findViewById(R.id.constraintLayout),
                        getString(R.string.cameraPermissionRequired),
                        Snackbar.LENGTH_LONG
                ).show();
            }
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Log.i("MENU", "menu is being prepared");

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return super.onPrepareOptionsMenu(menu);
    }

    public void setViewState(VIEW_STATE state) {
        Menu appMenu = ((Toolbar) findViewById(R.id.toolbar)).getMenu();
        switch (state) {
            case MEASUREMENT:
                appMenu.getItem(MENU_INDEX_NEW_MEASUREMENT).setVisible(false);
                appMenu.getItem(MENU_INDEX_EXPORT_RESULT).setVisible(false);
               // appMenu.getItem(MENU_INDEX_EXPORT_DETAILS).setVisible(false);
                findViewById(R.id.floatingActionButton).setVisibility(View.INVISIBLE);
                break;
            case SHOW_RESULTS:
                findViewById(R.id.floatingActionButton).setVisibility(View.VISIBLE);
                appMenu.getItem(MENU_INDEX_EXPORT_RESULT).setVisible(true);
                //appMenu.getItem(MENU_INDEX_EXPORT_DETAILS).setVisible(true);
                appMenu.getItem(MENU_INDEX_NEW_MEASUREMENT).setVisible(true);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CREATE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                 string = data.getStringExtra("RESULT_STRING");

            }
        }
    }



    public void onClickNewMeasurement(MenuItem item) {
        onClickNewMeasurement();
    }

    public void onClickNewMeasurement(View view) {
        saveMeasurement();
    }

    public void saveMeasurement()
    {
        FirebaseUser user = mAuth.getCurrentUser();
        firebaseDatabase =  FirebaseDatabase.getInstance();
        String id = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        databaseReference = FirebaseDatabase.getInstance()
                .getReference().child("ppg_scores").child(id).push();


       String result = (String) ((TextView)findViewById(R.id.textView)).getText();

       UserResult userResult = new UserResult(result);

        databaseReference.setValue(userResult);

        Toast.makeText(getApplicationContext(),
                "Your Results Have Been Uploaded" + result,
                Toast.LENGTH_LONG).show();

        onClickNewMeasurement();
    }

    public void onClickNewMeasurement() {
        analyzer = new Analyzer(this, findViewById(R.id.graphTextureView), mainHandler);

        // clear prior results
        char[] empty = new char[0];
        ((TextView) findViewById(R.id.textView)).setText(empty, 0, 0);
        ((TextView) findViewById(R.id.outputView)).setText(empty, 0, 0);
        ((TextView) findViewById(R.id.outputView2)).setText(empty, 0, 0);

        setViewState(VIEW_STATE.MEASUREMENT);

        TextureView cameraTextureView = findViewById(R.id.textureView2);
        SurfaceTexture previewSurfaceTexture = cameraTextureView.getSurfaceTexture();

        if (previewSurfaceTexture != null) {
            Surface previewSurface = new Surface(previewSurfaceTexture);
            cameraService.start(previewSurface);
            analyzer.measurePulse(cameraTextureView, cameraService);
        }
    }

    public void onClickExportResult(MenuItem item) {
        final Intent intent = getTextIntent((String) ((TextView) findViewById(R.id.textView)).getText());
        justShared = true;
        startActivity(Intent.createChooser(intent, getString(R.string.send_output_to)));
    }


    private Intent getTextIntent(String intentText) {
        final Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(
                Intent.EXTRA_SUBJECT,
                String.format(
                        getString(R.string.output_header_template),
                        new SimpleDateFormat(
                                getString(R.string.dateFormat),
                                Locale.getDefault()
                        ).format(new Date())
                ));
        intent.putExtra(Intent.EXTRA_TEXT, intentText);
        return intent;
    }

}
