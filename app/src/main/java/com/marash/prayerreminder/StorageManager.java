package com.marash.prayerreminder;

import android.content.Context;
import android.util.Log;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * Created by Maedeh on 2/22/2016.
 */
public class StorageManager {

    private final static String STORED_Alerts_TEXT = "storedAlertsText.txt";
    OutputStreamWriter out;
    FileOutputStream outputFile;
    FileInputStream inputFile;
    Context context;

    public StorageManager(Context context){
        this.context = context;
    }

    public boolean saveAlert(Alert alert){

        try {
            outputFile = context.openFileOutput(STORED_Alerts_TEXT, Context.MODE_APPEND);
            out = new OutputStreamWriter(outputFile);
            out.write(alert.getPrayerName() + "," + alert.getBeforeAfter() + "," + alert.getTime() + "\n");
            out.close();
            return true;
         }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public ArrayList<Alert> loadAlert(){
        try {

            inputFile = context.openFileInput(STORED_Alerts_TEXT);
            BufferedReader inputReader = new BufferedReader(new InputStreamReader(inputFile));
            String inputString;
            //StringBuffer stringBuffer = new StringBuffer();
            String alertInfo;
            String[] alertParts;
            ArrayList<Alert> alertsList = new ArrayList<Alert>();
            while ((inputString = inputReader.readLine()) != null) {
                //stringBuffer.append(inputString);
                alertInfo = inputString;
                alertParts = alertInfo.split(",");
                Alert a = new Alert(alertParts[0],alertParts[1],alertParts[2]);
                alertsList.add(a);


                Log.d("maedehSara", inputString);

            }
            //inputReader.close();
            //String result = stringBuffer.toString();
            return alertsList;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
