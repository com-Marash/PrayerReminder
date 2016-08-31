package com.marash.prayerreminder;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.Arrays;

/**
 * Created by Maedeh on 8/31/2016.
 */

//this class checks for the first time usage. Right after installation. and tries
    //to set some custom settings by asking the user to select them.

public class FirstUsage extends Activity{
    private String selection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_use_page);

        calculationMethodFunction().show();

        //when every thing set do this. and return to main page.
        //this.finish();
    }

    private AlertDialog calculationMethodFunction() {
        final CharSequence[] methodesItems = {"ISNA", "MWL", "Makkah", "Karachi", "Jafari", "Tehran", "Egypt"};
        String savedCalcMethode;

        savedCalcMethode = StorageManager.loadCalculationMethode(FirstUsage.this.getApplicationContext());

        if (savedCalcMethode == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(FirstUsage.this);
            builder.setTitle("Choose Calculation Methode").setSingleChoiceItems(methodesItems, 0, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int methodeNumber) {
                    selection = (String) methodesItems[methodeNumber];
                }
            }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(FirstUsage.this, "You have selected " + selection + " as your calculation methode.", Toast.LENGTH_LONG).show();
                    StorageManager.saveCalculationMethode(selection, FirstUsage.this.getApplicationContext());
                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            return builder.create();
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(FirstUsage.this);
            builder.setTitle("Choose Calculation Methode").setSingleChoiceItems(methodesItems, Arrays.asList(methodesItems).indexOf(savedCalcMethode), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int methodeNumber) {
                    selection = (String) methodesItems[methodeNumber];
                }
            }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(FirstUsage.this, "You have selected " + selection + " as your calculation methode.", Toast.LENGTH_LONG).show();
                    StorageManager.saveCalculationMethode(selection, FirstUsage.this.getApplicationContext());
                    //// TODO: 8/4/2016 for arash code!
                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            return builder.create();
        }
    }


}
