package com.marash.prayerreminder;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.marash.prayerTimes.dto.Coordination;
import com.marash.prayerTimes.dto.prayerTimesData;
import com.marash.prayerTimes.main.PrayerTimes;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class showPrayerTimes extends AppCompatActivity {

    private TextView showDateText;
    DatePickerDialog.OnDateSetListener myDatePickerListener;
    final Calendar calendar = new GregorianCalendar();

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_prayer_times);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        defaultFunction();


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void defaultFunction() {
        PrayerTimes myPrayerTimes = new PrayerTimes();
        myPrayerTimes.setMethod(PrayerTimes.methods.ISNA);

        Date date = new Date();
        calendar.setTime(date);

        String weekDay;
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US);
        weekDay = dayFormat.format(calendar.getTime());

        showDate((int) calendar.get(Calendar.YEAR), (int) calendar.get(Calendar.MONTH) + 1, (int) calendar.get(Calendar.DAY_OF_MONTH), weekDay);


//        showDateText.setText(new StringBuilder().append(weekDay + "  ").append((int) calendar.get(Calendar.DAY_OF_MONTH)).append("/")
//                .append((int) calendar.get(Calendar.MONTH) + 1).append("/").append((int) calendar.get(Calendar.YEAR)));






//        prayerTimesData calculatedTimes = null;
//        try {
//            calculatedTimes = myPrayerTimes.getTimes(new int[]{calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH) +1,calendar.get(Calendar.DAY_OF_MONTH)}, new Coordination(40, -80), (double) -5, null);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

//        System.out.println(calculatedTimes.getImsak().getFormatedTime());
//        System.out.println(calculatedTimes.getFajr().getFormatedTime());
//        System.out.println(calculatedTimes.getSunrise().getFormatedTime());
//        System.out.println(calculatedTimes.getDhuhr().getFormatedTime());
//        System.out.println(calculatedTimes.getAsr().getFormatedTime());
//        System.out.println(calculatedTimes.getSunset().getFormatedTime());
//        System.out.println(calculatedTimes.getMaghrib().getFormatedTime());
//        System.out.println(calculatedTimes.getIsha().getFormatedTime());
//        System.out.println(calculatedTimes.getMidnight().getFormatedTime());
    }

    private void showDate(int year, int month, final int day, String weekDay) {
        showDateText = (TextView) findViewById(R.id.editTextShowDate);

        showDateText.setText(new StringBuilder().append(weekDay + "  ").append(day).append("/")
                .append(month).append("/").append(year));

        myDatePickerListener= new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {

                calendar.set(Calendar.YEAR,selectedYear);
                calendar.set(Calendar.MONTH,selectedMonth);
                calendar.set(Calendar.DAY_OF_MONTH,selectedDay);
                showDateText.setText(new StringBuilder().append(selectedDay).append("/")
                        .append(selectedMonth + 1).append("/").append(selectedYear));
            }
        };

        final DatePickerDialog datePickerDialog = new DatePickerDialog(showPrayerTimes.this,myDatePickerListener,Calendar.YEAR,Calendar.MONTH,Calendar.DAY_OF_MONTH);
        datePickerDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                DatePicker datePicker = datePickerDialog
                        .getDatePicker();
                myDatePickerListener.onDateSet(datePicker, datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
            }
        });


        showDateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(showPrayerTimes.this,myDatePickerListener, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }




}
