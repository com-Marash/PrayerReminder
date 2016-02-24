package com.marash.prayerreminder;

import android.content.Context;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Created by Maedeh on 2/22/2016.
 */
public class StorageManager {

    private final static String STORED_Alerts_TEXT = "storedAlertsText.txt";
    OutputStreamWriter out;
    Context context;
    FileOutputStream fos;

    public boolean saveAlert(Alert alert){


        try {
            fos = context.openFileOutput(STORED_Alerts_TEXT, Context.MODE_PRIVATE);
            out = new OutputStreamWriter(fos);
            out.append(alert.getPrayerName() + "," + alert.getBeforeAfter() + "," + alert.getTime() + "\n");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
}
