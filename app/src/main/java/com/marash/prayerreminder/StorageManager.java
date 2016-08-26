package com.marash.prayerreminder;

import android.content.Context;
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
    static OutputStreamWriter out;
    static FileOutputStream outputFile;
    static FileInputStream inputFile;
    private static BufferedReader inputReader;
    static String inputString;
    private final static String STORED_ringtone = "storedRingtoneText.txt";
    private final static String STORED_calcmethode = "storedCalculationMethode.txt";


    public static void saveAlert(Alert alert, Context context) {

        try {
            outputFile = context.openFileOutput(STORED_Alerts_TEXT, Context.MODE_APPEND);
            out = new OutputStreamWriter(outputFile);
            out.write(alert.getPrayerName() + "," + alert.getBeforeAfter() + "," + alert.getTime() + "," + alert.getAlertNumber() + "\n");
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Alert> loadAlert(Context context) {
        try {
            ArrayList<Alert> alertsList = new ArrayList<Alert>();

            inputFile = context.openFileInput(STORED_Alerts_TEXT);
            inputReader = new BufferedReader(new InputStreamReader(inputFile));

            String alertInfo;
            String[] alertParts;

            while ((inputString = inputReader.readLine()) != null) {
                alertInfo = inputString;
                alertParts = alertInfo.split(",");
                Alert a = new Alert(alertParts[0], alertParts[1], alertParts[2], Integer.parseInt(alertParts[3]));
                alertsList.add(a);
            }
            inputReader.close();
            return alertsList;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void deleteAlert(int index, Context context) {
        ArrayList<Alert> alertsList = loadAlert(context);
        alertsList.remove(index);

        // Because we dont know how to delete an specific line from file, we deleted that. And again write all alerts.
        context.deleteFile(STORED_Alerts_TEXT);
        try {
            outputFile = context.openFileOutput(STORED_Alerts_TEXT, Context.MODE_APPEND);
            out = new OutputStreamWriter(outputFile);
            for (Alert alert : alertsList) {
                out.write(alert.getPrayerName() + "," + alert.getBeforeAfter() + "," + alert.getTime() + "," + alert.getAlertNumber() + "\n");
            }
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveAlarmRingtone(String ringtoneTitle, String URIString, Context context){
        try {

            outputFile = context.openFileOutput(STORED_ringtone, Context.MODE_PRIVATE);
            out = new OutputStreamWriter(outputFile);
            out.write(ringtoneTitle+ "\n"+URIString);
            out.close();
        }catch (IOException e){
            e.printStackTrace();

        }
    }

    public static String[] loadAlarmRingtone(Context context) {
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

    public static void saveCalculationMethode(String calcMthode, Context context){
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

    public static String loadCalculationMethode(Context context) {
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