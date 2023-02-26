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
    private static OutputStreamWriter out;
    private static FileOutputStream outputFile;
    private static FileInputStream inputFile;
    private static BufferedReader inputReader;
    private final static String STORED_ringtone = "storedRingtoneText.txt";
    private final static String STORED_calcmethode = "storedCalculationMethode.txt";
    private final static String STORED_location = "storedLocation.txt";
    private final static String STORED_savedPrayersToShow = "storedPrayersToShow.txt";


    public static void saveAlert(Alert alert, Context context) {

        try {
            outputFile = context.openFileOutput(STORED_Alerts_TEXT, Context.MODE_APPEND);
            out = new OutputStreamWriter(outputFile);
            out.write(alert.getPrayerName() + "," + alert.getTime() + "," + alert.getAlertNumber() + "\n");
            out.close();
        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        }
    }

    public static ArrayList<Alert> loadAlert(Context context) {
        try {
            ArrayList<Alert> alertsList = new ArrayList<Alert>();

            inputFile = context.openFileInput(STORED_Alerts_TEXT);
            inputReader = new BufferedReader(new InputStreamReader(inputFile));

            String alertInfo;
            String[] alertParts;

            String inputString;
            while ((inputString = inputReader.readLine()) != null) {
                alertInfo = inputString;
                alertParts = alertInfo.split(",");
                Alert a = new Alert(alertParts[0], Integer.parseInt(alertParts[1]), Integer.parseInt(alertParts[2]));
                alertsList.add(a);
            }
            inputReader.close();
            return alertsList;
        } catch (IOException e) {

        }
        return null;
    }


    public static void deleteAlert(int index, Context context) {
        ArrayList<Alert> alertsList = loadAlert(context);
        AlarmSetter.deleteAlarm(alertsList.get(index).getAlertNumber(), context);
        alertsList.remove(index);

        // Because we dont know how to delete an specific line from file, we deleted that. And again write all alerts.
        context.deleteFile(STORED_Alerts_TEXT);
        try {
            outputFile = context.openFileOutput(STORED_Alerts_TEXT, Context.MODE_APPEND);
            out = new OutputStreamWriter(outputFile);
            for (Alert alert : alertsList) {
                out.write(alert.getPrayerName() + "," + alert.getTime() + "," + alert.getAlertNumber() + "\n");
            }
            out.close();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public static void saveAlarmRingtone(String ringtoneTitle, String URIString, Context context) {
        try {

            outputFile = context.openFileOutput(STORED_ringtone, Context.MODE_PRIVATE);
            out = new OutputStreamWriter(outputFile);
            out.write(ringtoneTitle + "\n" + URIString);
            out.close();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public static void saveAlarmRingtone(String azanTitle, Context context) {
        try {

            outputFile = context.openFileOutput(STORED_ringtone, Context.MODE_PRIVATE);
            out = new OutputStreamWriter(outputFile);
            out.write(azanTitle);
            out.close();
        } catch (IOException e) {
            throw new IllegalStateException(e);
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
           throw new IllegalStateException(e);
        }
    }

    public static void saveCalculationMethode(String calcMethod, Context context) {
        try {
            outputFile = context.openFileOutput(STORED_calcmethode, Context.MODE_PRIVATE);
            out = new OutputStreamWriter(outputFile);
            out.write(calcMethod);
            out.close();
        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        }
    }

    public static String loadCalculationMethod(Context context) {
        String temp;
        try {
            inputFile = context.openFileInput(STORED_calcmethode);
            inputReader = new BufferedReader(new InputStreamReader(inputFile));
            temp = inputReader.readLine();
            inputReader.close();
            return temp;

        } catch (IOException e) {

        }
        return null;
    }

    public static void saveLocation(Double latitude, Double longitude, String country, String city, Context context) {
        try {
            outputFile = context.openFileOutput(STORED_location, Context.MODE_PRIVATE);
            out = new OutputStreamWriter(outputFile);
            out.write(latitude + "\n" + longitude + "\n" + country + "\n" + city);
            out.close();
        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        }
    }

    public static String[] loadLocation(Context context) {
        String[] temp = new String[4];
        try {
            inputFile = context.openFileInput(STORED_location);
            inputReader = new BufferedReader(new InputStreamReader(inputFile));
            for (int i = 0; i < 4; i++) {
                temp[i] = inputReader.readLine();
            }
            inputReader.close();
            return temp;
        } catch (IOException e) {

        }
        return null;
    }

    public static boolean[] loadSavedPrayersToShow(Context context) {
        String temp;
        try {
            inputFile = context.openFileInput(STORED_savedPrayersToShow);
            inputReader = new BufferedReader(new InputStreamReader(inputFile));
            temp = inputReader.readLine();
            inputReader.close();

            String[] savedPrayersToShowArray = temp.split(",");
            final boolean[] savedPrayersToShowBoolean = new boolean[savedPrayersToShowArray.length];
            for (int i = 0; i < savedPrayersToShowArray.length; i++) {
                savedPrayersToShowBoolean[i] = savedPrayersToShowArray[i].equals("true");
            }
            return savedPrayersToShowBoolean;

        } catch (IOException e) {
            String firstPrayers = "true,true,true,true,true,true,true,true,true";
            StorageManager.saveSavedPrayersToShow(firstPrayers, context);
            return new boolean[]{true, true, true, true, true, true, true, true, true};
        }
    }

    public static void saveSavedPrayersToShow(String savedPrayers, Context context) {

        try {
            outputFile = context.openFileOutput(STORED_savedPrayersToShow, Context.MODE_PRIVATE);
            out = new OutputStreamWriter(outputFile);
            out.write(savedPrayers);
            out.close();
        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        }
    }

}

