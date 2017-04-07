package com.marash.prayerreminder;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.marash.prayerTimes.dto.Coordination;
import com.marash.prayerTimes.dto.prayerTimesData;
import com.marash.prayerTimes.main.PrayerTimes;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class MainPage extends AppCompatActivity {

    private TextView showDateText, goToTodayText;
    private Button nextDayButton, previousDayButton;
    private Calendar calendar = new GregorianCalendar();

    protected GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //FirstUse Handler
        if (isLocationAvailable(MainPage.this) && isMethodAvailable(MainPage.this)) {
            setContentView(R.layout.activity_main_page);
            showDateText = (TextView) findViewById(R.id.editTextShowDate);
            goToTodayText = (TextView) findViewById(R.id.textView_goToToday);
            nextDayButton = (Button) findViewById(R.id.buttonNextDay);
            previousDayButton = (Button) findViewById(R.id.buttonPreviousDay);

            calendar.setTime(new Date());
            setListeners();

            showDateInformation();
        } else {
            Intent firstUsageIntent = new Intent("com.marash.prayerreminder.FirstUsage");
            startActivity(firstUsageIntent);
        }
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.settingsItem:
                Intent settingIntent = new Intent("com.marash.prayerreminder.Setting");
                startActivity(settingIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isLocationAvailable(Context context) {
        return StorageManager.loadLocation(context) == null ? false : true;
    }

    private boolean isMethodAvailable(Context context) {
        return StorageManager.loadCalculationMethode(context) == null ? false : true;
    }

    private void setListeners() {
        final DatePickerDialog.OnDateSetListener myDatePickerListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                calendar.set(Calendar.YEAR, selectedYear);
                calendar.set(Calendar.MONTH, selectedMonth);
                calendar.set(Calendar.DAY_OF_MONTH, selectedDay);
                showDateInformation();
            }
        };

        showDateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatePickerDialog datePickerDialog = new DatePickerDialog(MainPage.this, myDatePickerListener, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        goToTodayText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.setTime(new Date());
                showDateInformation();
            }
        });

        nextDayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.DATE, 1);
                showDateInformation();
            }
        });

        previousDayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.DATE, -1);
                showDateInformation();
            }
        });
    }

    /**
     * show the current date on top of main page and also show/hide gotoToday text
     * Also, calculate the new prayer times and update all prayer times text
     */
    private void showDateInformation() {

        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE dd/MM/yyyy");
        String dateText = dayFormat.format(calendar.getTime());

        showDateText.setText(dateText);

        Calendar currentCalender = Calendar.getInstance();
        if (calendar.get(Calendar.YEAR) == currentCalender.get(Calendar.YEAR)
                && calendar.get(Calendar.MONTH) == currentCalender.get(Calendar.MONTH)
                && calendar.get(Calendar.DAY_OF_YEAR) == currentCalender.get(Calendar.DAY_OF_YEAR)) {
            goToTodayText.setVisibility(View.GONE);
        } else {
            goToTodayText.setVisibility(View.VISIBLE);
        }

        PrayerTimes myPrayerTimes = new PrayerTimes(PrayerTimes.methods.valueOf(prayerTimesCalculator.getMethod(this)));
        double[] location = prayerTimesCalculator.getLccation(this);
        prayerTimesData calculatedTimes = myPrayerTimes.getTimes(new int[]{calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH)}, new Coordination(location[0], location[1]));

        if (calculatedTimes != null) {
            TextView fajr = (TextView) findViewById(R.id.textView_fajr);
            fajr.setText(minuteFormatting(calculatedTimes.getFajr().getFormatedTime()));

            TextView sunrise = (TextView) findViewById(R.id.textView_sunrise);
            sunrise.setText(minuteFormatting(calculatedTimes.getSunrise().getFormatedTime()));

            TextView Dhuhr = (TextView) findViewById(R.id.textView_Dhuhr);
            Dhuhr.setText(minuteFormatting(calculatedTimes.getDhuhr().getFormatedTime()));

            TextView asr = (TextView) findViewById(R.id.textView_asr);
            asr.setText(minuteFormatting(calculatedTimes.getAsr().getFormatedTime()));

            TextView sunset = (TextView) findViewById(R.id.textView_sunset);
            sunset.setText(minuteFormatting(calculatedTimes.getSunset().getFormatedTime()));

            TextView maghrib = (TextView) findViewById(R.id.textView_maghrib);
            maghrib.setText(minuteFormatting(calculatedTimes.getMaghrib().getFormatedTime()));

            TextView isha = (TextView) findViewById(R.id.textView_isha);
            isha.setText(minuteFormatting(calculatedTimes.getIsha().getFormatedTime()));

            TextView midnight = (TextView) findViewById(R.id.textView_midnight);
            midnight.setText(minuteFormatting(calculatedTimes.getMidnight().getFormatedTime()));

            TextView imsak = (TextView) findViewById(R.id.textView_imsak);
            imsak.setText(minuteFormatting(calculatedTimes.getImsak().getFormatedTime()));
        }
    }

    private String minuteFormatting(String s){
        String result;
        String[] t = s.split(":");
        String s1 = t[0];
        String s2 = t[1];
        if(s2.length() == 1){
            s2 = "0" + s2;
        }
        result = s1 + ":" + s2;
        return result;
    }
}

