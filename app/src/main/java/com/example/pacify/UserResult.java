package com.example.pacify;

import android.content.Intent;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;

public class UserResult {

    public String result;
    public HashMap<String, Object> dateMeasured;

    public UserResult(){}

    public UserResult(String result)
    {
        this.result = result;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public HashMap<String, Object> getDateMeasured()
    {
        if (dateMeasured != null)
        {
            return dateMeasured;
        }

        HashMap<String, Object> dateMeasuredObj = new HashMap<String, Object>();
        dateMeasuredObj.put("date", ServerValue.TIMESTAMP);
        return dateMeasuredObj;
    }

    @Exclude
    public long getDateMeasuredLong()
    {
        return (long)dateMeasured.get("date");
    }
}
