package com.example.pacify;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;

public class StorePSS {

    public HashMap<String, Object> dateCreated;
    public int pssScore;
    public String stress;


    public StorePSS(){}

    public StorePSS(int pssScore, String stress)
    {
        this.pssScore = pssScore;
        this.stress = stress;
    }


    public HashMap<String, Object> getDateCreated() {
        if(dateCreated != null){
        return dateCreated;
        }

        HashMap<String, Object> dateCreatedObj = new HashMap<String, Object>();
        dateCreatedObj.put("date", ServerValue.TIMESTAMP);
        return dateCreatedObj;
    }


    public int getPssScore() {
        return pssScore;
    }


    public String getStress() {
        return stress;
    }


    @Exclude
    public long getDateCreatedLong()
    {
        return (long)dateCreated.get("date");
    }

}
