package com.marash.prayerreminder;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

/**
 * Created by Maedeh on 3/3/2016.
 */
public class DeleteAlert extends AppCompatActivity {

    //private ArrayList<Alert> savedAlerts;

    private Context context;

    public DeleteAlert(Context context){
        this.context = context;
    }

    public void deleteFunction(int i){
        StorageManager myManager = new StorageManager(this.context);
        //savedAlerts = myManager.loadAlert();
        myManager.deleteAlert(i);
    }
}
