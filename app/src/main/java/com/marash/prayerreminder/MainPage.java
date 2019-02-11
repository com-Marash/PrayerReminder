package com.marash.prayerreminder;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import com.marash.prayerTimes.dto.Coordination;
import com.marash.prayerTimes.dto.prayerTimesData;
import com.marash.prayerTimes.main.PrayerTimes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class MainPage extends AppCompatActivity {

    public static boolean refreshPrayerTimes = false;
    public static boolean refreshLocation = false;
    private boolean[] prayersToShow;
    private final CharSequence[] prayersNameOrders = {"Imsaak", "Fajr", "Sunrise", "Dhuhr", "Asr", "Sunset", "Maghrib", "Isha", "Midnight"};

    private TextView showDateText;
    public TextView locationText;
    private ImageView goToTodayText;
    private ImageView nextDayButton, previousDayButton;
    private Calendar calendar = new GregorianCalendar();
    private Double[] todayPrayerTimes = new Double[9];
    private String[] location;

    @Override
    protected void onResume() {
        super.onResume();

        if(refreshLocation){
            location = StorageManager.loadLocation(this);
            if(location != null && !location[2].equals("unknown") && !location[3].equals("unknown")){
                locationText.setText(location[3] + ", " + location[2]);
                refreshLocation = false;
            }
        }

        if (refreshPrayerTimes || prayersToShow == null) {
            refreshPrayerTimes = false;
            prayersToShow = StorageManager.loadSavedPrayersToShow(this);
        }
        for (int k = 0; k < prayersToShow.length; k++) {
            int tableRowId = getResources().getIdentifier("tableRow_" + prayersNameOrders[k], "id", this.getPackageName());
            TableRow tableRow = findViewById(tableRowId);
            if (tableRow != null) {
                if (prayersToShow[k]) {
                    tableRow.setVisibility(View.VISIBLE);
                } else {
                    tableRow.setVisibility(View.GONE);
                }
            }
        }
        nextPrayerFunction();
    }


    @Override
    protected void onStart() {
        super.onStart();
        //FirstUse Handler
        if (isLocationAvailable(MainPage.this) && isMethodAvailable(MainPage.this)) {
            setContentView(R.layout.activity_main_page);
            showDateText = (TextView) findViewById(R.id.editTextShowDate);
            goToTodayText = (ImageView) findViewById(R.id.returnToday);
            locationText = findViewById(R.id.locationText);
            nextDayButton = (ImageView) findViewById(R.id.buttonNextDay);
            previousDayButton = (ImageView) findViewById(R.id.buttonPreviousDay);

            calendar.setTime(new Date());
            setListeners();

            showDateInformation();

            location = StorageManager.loadLocation(this);
            if(location != null && !location[2].equals("unknown") && !location[3].equals("unknown")){
                locationText.setText(location[3] + ", " + location[2]);
            }
        } else {
            Intent firstUsageIntent = new Intent("com.marash.prayerreminder.FirstUsage");
            startActivity(firstUsageIntent);
        }
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
        return StorageManager.loadLocation(context) != null;
    }

    private boolean isMethodAvailable(Context context) {
        return StorageManager.loadCalculationMethode(context) != null;
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

        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE dd MMM yyyy");
        String dateText = dayFormat.format(calendar.getTime());

        dateText = dateText + "\n" + DateHijri.writeIslamicDate(MainPage.this, calendar);

        //for making hijridate smaller font
        int begin = dateText.indexOf('\n') + 1;
        int end = dateText.length();
        SpannableString dateSpan = new SpannableString(dateText);
        dateSpan.setSpan(new RelativeSizeSpan(0.8f), begin, end, 0);
        ///////////////////////////////////

        showDateText.setText(dateSpan);

        Calendar currentCalender = Calendar.getInstance();
        if (calendar.get(Calendar.YEAR) == currentCalender.get(Calendar.YEAR)
                && calendar.get(Calendar.MONTH) == currentCalender.get(Calendar.MONTH)
                && calendar.get(Calendar.DAY_OF_YEAR) == currentCalender.get(Calendar.DAY_OF_YEAR)) {
            goToTodayText.setVisibility(View.GONE);
        } else {
            goToTodayText.setVisibility(View.VISIBLE);
        }

        PrayerTimes myPrayerTimes = new PrayerTimes(PrayerTimes.methods.valueOf(prayerTimesCalculator.getMethod(this)));
        double[] location = prayerTimesCalculator.getLocation(this);
        prayerTimesData calculatedTimes = myPrayerTimes.getTimes(new int[]{calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH)}, new Coordination(location[0], location[1]));

        if (calculatedTimes != null) {

            TextView fajr = findViewById(R.id.textView_fajr);
            fajr.setText(minuteFormatting(calculatedTimes.getFajr().getFormatedTime()));
            todayPrayerTimes[0] = calculatedTimes.getFajr().getTime();

            TextView sunrise = findViewById(R.id.textView_sunrise);
            sunrise.setText(minuteFormatting(calculatedTimes.getSunrise().getFormatedTime()));
            todayPrayerTimes[1] = calculatedTimes.getSunrise().getTime();


            TextView Dhuhr = findViewById(R.id.textView_Dhuhr);
            Dhuhr.setText(minuteFormatting(calculatedTimes.getDhuhr().getFormatedTime()));
            todayPrayerTimes[2] = calculatedTimes.getDhuhr().getTime();


            TextView asr = findViewById(R.id.textView_asr);
            asr.setText(minuteFormatting(calculatedTimes.getAsr().getFormatedTime()));
            todayPrayerTimes[3] = calculatedTimes.getAsr().getTime();


            TextView sunset = findViewById(R.id.textView_sunset);
            sunset.setText(minuteFormatting(calculatedTimes.getSunset().getFormatedTime()));
            todayPrayerTimes[4] = calculatedTimes.getSunset().getTime();


            TextView maghrib = findViewById(R.id.textView_maghrib);
            maghrib.setText(minuteFormatting(calculatedTimes.getMaghrib().getFormatedTime()));
            todayPrayerTimes[5] = calculatedTimes.getMaghrib().getTime();


            TextView isha = findViewById(R.id.textView_isha);
            isha.setText(minuteFormatting(calculatedTimes.getIsha().getFormatedTime()));
            todayPrayerTimes[6] = calculatedTimes.getIsha().getTime();


            TextView midnight = findViewById(R.id.textView_midnight);
            midnight.setText(minuteFormatting(calculatedTimes.getMidnight().getFormatedTime()));
            todayPrayerTimes[7] = calculatedTimes.getMidnight().getTime();

            TextView imsak = findViewById(R.id.textView_imsak);
            imsak.setText(minuteFormatting(calculatedTimes.getImsak().getFormatedTime()));
            todayPrayerTimes[8] = calculatedTimes.getImsak().getTime();

        }
    }

    private String minuteFormatting(String s) {
        String result;
        String[] t = s.split(":");
        String s1 = t[0];
        String s2 = t[1];
        if (s2.length() == 1) {
            s2 = "0" + s2;
        }
        result = s1 + ":" + s2;
        return result;
    }

    private void nextPrayerFunction() {
        // make sure prayerTimes are calculated
        if (todayPrayerTimes[0] != null) {
            TextView nextPrayer = findViewById(R.id.textView_nextPrayer);

            long currentTime = System.currentTimeMillis();
            int nextPrayerIndex = 0;
            Double smallest = 10.0;
            for (int i = 1; i < todayPrayerTimes.length; i++) {
                if (todayPrayerTimes[i] - currentTime < smallest) {
                    smallest = todayPrayerTimes[i] - currentTime;
                    nextPrayerIndex = i;
                }
            }
            if (smallest == 10) {
                nextPrayerIndex = 8;
            }


            // only show visible next prayer times
            do{
                if(prayersToShow[nextPrayerIndex]){
                    break;
                }else{
                    nextPrayerIndex++;
                    if(nextPrayerIndex == 9){
                        nextPrayerIndex = 0;
                    }
                }
            }while(true);

            nextPrayer.setText(prayersNameOrders[nextPrayerIndex]);
        }
    }
}

