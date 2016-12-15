package com.marash.prayerreminder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * Created by Maedeh on 9/1/2016.
 */
public class FirstUsageSecondPage extends Activity {
    private RadioButton selectedMethode;
    private String selectedMethodeText;
    private Button okButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_use_secondpage);

        okButton = (Button)findViewById(R.id.secondPageOKButt);
        methodSelection();
    }

    private void methodSelection() {

        RadioGroup rg = (RadioGroup)findViewById(R.id.radioGroup);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId != -1){
                    selectedMethode = (RadioButton) findViewById(checkedId);
                    selectedMethodeText = selectedMethode.getText().toString();

                    StorageManager.saveCalculationMethode(selectedMethodeText, FirstUsageSecondPage.this.getApplicationContext());
                    prayerTimesCalculator.setMethod(selectedMethodeText);
                    okButton.setEnabled(true);
                }
            }
        });
    }

    public void OKButtFunction(View view) {
        if(okButton.isEnabled()){

            Intent mainPageIntent = new Intent("com.marash.prayerreminder.MainPage");
            startActivity(mainPageIntent);
        }
    }
}
