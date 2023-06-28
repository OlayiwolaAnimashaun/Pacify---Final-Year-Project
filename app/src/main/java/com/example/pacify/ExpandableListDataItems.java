package com.example.pacify;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ExpandableListDataItems {


    public  static HashMap<String, List<String>> getData()
    {
        Locale locale = Locale.getDefault();
        HashMap<String, List<String>> expandableDetailList = new HashMap<String, List<String>>();
        List<String> pssScoresList = new ArrayList<String>();
        List<String> ppgScoresList = new ArrayList<String>();
        SimpleDateFormat IS0_8601 = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:sss'Z'", locale);
        String s = IS0_8601.format(new Date());

        final  FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String id = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        DatabaseReference refPSS = database.getReference().child("pss_scores").child(id);
        DatabaseReference refPPG = database.getReference().child("ppg_scores").child(id);

        refPSS.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    StorePSS storePSS = snapshot.getValue(StorePSS.class);
                    assert storePSS != null;

                    String time = Objects.requireNonNull(snapshot.child("dateCreated")
                            .child("date").getValue()).toString();
                    long t = Long.parseLong(time);

                    Date newDate = new Date(t);

                    String pssDate = newDate + "";
                    String pssScore = storePSS.pssScore + " ";
                    String pssStress = storePSS.stress;

                    String pssFinal = pssDate + " " + "PSS Score: " + pssScore + " " + pssStress;
                    pssScoresList.add(pssFinal);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                pssScoresList.remove(snapshot.getValue(String.class));
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        refPPG.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                UserResult res = snapshot.getValue(UserResult.class);
                assert res != null;

                String time = Objects.requireNonNull(snapshot.child("dateMeasured")
                        .child("date").getValue()).toString();
                long t = Long.parseLong(time);

                Date newDate = new Date(t);

                String ppgDate = newDate + " ";
                String ppgMeasure = res.result;

                String ppgFinal = ppgDate + " " + ppgMeasure;
                ppgScoresList.add(ppgFinal);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                ppgScoresList.remove(snapshot.getValue(String.class));
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        expandableDetailList.put("PSS Scores", pssScoresList);
        expandableDetailList.put("PPG Scores", ppgScoresList);

        return expandableDetailList;
    }
}
