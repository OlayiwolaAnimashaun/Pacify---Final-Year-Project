package com.example.pacify;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class StoredResultsActivity extends AppCompatActivity{

    public FirebaseAuth mAuth;
    public FirebaseAuth.AuthStateListener stateListener;

    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableTitleList;
    HashMap<String, List<String>> expandableDetailList;


    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(stateListener);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        mAuth = FirebaseAuth.getInstance();

        stateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if(firebaseAuth.getCurrentUser() == null)
                {
                    Intent intent = new Intent(StoredResultsActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        };


        expandableListView = (ExpandableListView) findViewById(R.id.expandedListView);
        expandableDetailList = ExpandableListDataItems.getData();
        expandableTitleList = new ArrayList<String>(expandableDetailList.keySet());
        expandableListAdapter = new CustomizedExpandableListAdapter(this, expandableTitleList, expandableDetailList);
        expandableListView.setAdapter(expandableListAdapter);

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        expandableTitleList.get(groupPosition) + "List Expanded.",
                        Toast.LENGTH_SHORT).show();
            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        expandableTitleList.get(groupPosition) + "List Collapsed",
                        Toast.LENGTH_SHORT).show();
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                Toast.makeText(getApplicationContext(),
                        expandableTitleList.get(groupPosition)
                + "->"
                + Objects.requireNonNull(expandableDetailList.get(
                                expandableTitleList.get(groupPosition))).get(
                                childPosition),
                        Toast.LENGTH_SHORT
                ).show();
                return false;
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

                                Toast.makeText(getApplicationContext(),
                                        "Stored Results",
                                        Toast.LENGTH_SHORT).show();
                                break;

                            case R.id.action_home:
                                Toast.makeText(getApplicationContext(),
                                        "Home",
                                        Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(StoredResultsActivity.this, HomeActivity.class);
                                startActivity(intent);
                                break;

                            case R.id.action_signout:
                                Toast.makeText(getApplicationContext(),
                                        "Logged Out",
                                        Toast.LENGTH_SHORT).show();

                                final FirebaseAuth firebaseAuth;
                                firebaseAuth = FirebaseAuth.getInstance();
                                firebaseAuth.signOut();

                                Intent intent2 = new Intent(StoredResultsActivity.this, MainActivity.class);
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
