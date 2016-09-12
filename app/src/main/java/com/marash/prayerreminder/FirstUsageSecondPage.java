package com.marash.prayerreminder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

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

    }

    public void methodSelection(View view) {

        int checked_ID = ((RadioGroup) view).getCheckedRadioButtonId();

        if (checked_ID != -1){
            selectedMethode = (RadioButton) findViewById(checked_ID);
            selectedMethodeText = selectedMethode.getText().toString();

            StorageManager.saveCalculationMethode(selectedMethodeText, FirstUsageSecondPage.this.getApplicationContext());

            okButton.setEnabled(true);
        }
    }

    public void OKButtFunction(View view) {
        if(okButton.isEnabled()){
            //TODO...
            Intent mainPageIntent = new Intent("com.marash.prayerreminder.MainPage");
            startActivity(mainPageIntent);
        }
    }
}
