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
    private RadioButton selectedMethod;
    private String selectedMethodText;
    private Button okButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_first_use_second_page);

        okButton = (Button)findViewById(R.id.secondPageOKButt);
        methodSelection();
    }

    private void methodSelection() {

        RadioGroup rg = (RadioGroup)findViewById(R.id.radioGroup);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId != -1){
                    selectedMethod = (RadioButton) findViewById(checkedId);
                    selectedMethodText = selectedMethod.getText().toString();

                    StorageManager.saveCalculationMethode(selectedMethodText, FirstUsageSecondPage.this.getApplicationContext());
                    prayerTimesCalculator.setMethod(selectedMethodText);
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
