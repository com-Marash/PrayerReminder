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
public  class StorageManager {

    private final static String STORED_Alerts_TEXT = "storedAlertsText.txt";
    OutputStreamWriter out;
    FileOutputStream outputFile;
    FileInputStream inputFile;
    Context context;
    private ArrayList<Alert> alertsList;
    private BufferedReader inputReader;
    String inputString;
    private String ringtone;
    private final static String STORED_ringtone = "storedRingtoneText.txt";
    private final static String STORED_calcmethode = "storedCalculationMethode.txt";


    public StorageManager(Context context) {
        this.context = context;
        alertsList = new ArrayList<Alert>();
    }

    public boolean saveAlert(Alert alert) {

        try {
            outputFile = context.openFileOutput(STORED_Alerts_TEXT, Context.MODE_APPEND);
            out = new OutputStreamWriter(outputFile);
            out.write(alert.getPrayerName() + "," + alert.getBeforeAfter() + "," + alert.getTime() + "\n");
            alertsList.add(alert);
            out.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public ArrayList<Alert> loadAlert() {
        try {

            inputFile = context.openFileInput(STORED_Alerts_TEXT);
            inputReader = new BufferedReader(new InputStreamReader(inputFile));

            String alertInfo;
            String[] alertParts;

            while ((inputString = inputReader.readLine()) != null) {
                //stringBuffer.append(inputString);
                alertInfo = inputString;
                alertParts = alertInfo.split(",");
                Alert a = new Alert(alertParts[0], alertParts[1], alertParts[2]);
                alertsList.add(a);


                Log.d("maedehSara", inputString);

            }
            inputReader.close();
            return alertsList;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void editAlert(Alert alert) {

    }

    public void deleteAlert(int index) {
        this.loadAlert();
        alertsList.remove(index);

        // Because we dont know how to delete an specific line from file, we deleted that. And again write all alerts.
        context.deleteFile(STORED_Alerts_TEXT);
        try {
            outputFile = context.openFileOutput(STORED_Alerts_TEXT, Context.MODE_APPEND);
            out = new OutputStreamWriter(outputFile);
            for (Alert alert : alertsList) {
                out.write(alert.getPrayerName() + "," + alert.getBeforeAfter() + "," + alert.getTime() + "\n");
            }
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveAlarmRingtone(String ringtoneTitle, String URIString){
        try {

            outputFile = context.openFileOutput(STORED_ringtone, Context.MODE_PRIVATE);
            out = new OutputStreamWriter(outputFile);
            out.write(ringtoneTitle+ "\n"+URIString);
            out.close();
        }catch (IOException e){
            e.printStackTrace();

        }
    }

    public String[] loadAlarmRingtone() {
        try {
            String[] result = new String[2];

            inputFile = context.openFileInput(STORED_ringtone);
            inputReader = new BufferedReader(new InputStreamReader(inputFile));
            result[0] = inputReader.readLine();
            result[1] = inputReader.readLine();
            return result;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void saveCalculationMethode(String calcMthode){
        try {
            outputFile = context.openFileOutput(STORED_calcmethode, Context.MODE_PRIVATE);
            out = new OutputStreamWriter(outputFile);
            out.write(calcMthode);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String loadCalculationMethode() {
        String temp;
        try {
            inputFile = context.openFileInput(STORED_calcmethode);
            inputReader = new BufferedReader(new InputStreamReader(inputFile));
            temp = inputReader.readLine();
            inputReader.close();
            return temp;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}