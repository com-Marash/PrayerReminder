package com.marash.prayerreminder;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import java.util.Locale;

public class showPrayerTimes extends AppCompatActivity {

    private TextView showDateText, goToTodayText;
    private Button nextDayButton, previousDayButton;
    private Calendar calendar = new GregorianCalendar();
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

        showDateText = (TextView) findViewById(R.id.editTextShowDate);
        goToTodayText = (TextView) findViewById(R.id.textView_goToToday);
        nextDayButton = (Button) findViewById(R.id.buttonNextDay);
        previousDayButton = (Button) findViewById(R.id.buttonPreviousDay);

        calendar.setTime(new Date());
        setListeners();
        showDateInformation();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
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
                final DatePickerDialog datePickerDialog = new DatePickerDialog(showPrayerTimes.this, myDatePickerListener, calendar
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

    private void showDateInformation() {

        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE dd/MM/yyyy", Locale.US);
        String dateText = dayFormat.format(calendar.getTime());
        showDateText.setText(dateText);

        Date todayDate = new Date();
        SimpleDateFormat checkFormat = new SimpleDateFormat("dd/MM/yyyy");
        String todayFormat = checkFormat.format(todayDate);
        String selectedDayFormat = checkFormat.format(calendar.getTime());

        if (todayFormat.equals(selectedDayFormat)) {
            goToTodayText.setVisibility(View.GONE);
        } else {
            goToTodayText.setVisibility(View.VISIBLE);
        }

        PrayerTimes myPrayerTimes = new PrayerTimes();

        String savedCalcMethode = StorageManager.loadCalculationMethode(showPrayerTimes.this.getApplicationContext());



        myPrayerTimes.setMethod(PrayerTimes.methods.valueOf(savedCalcMethode));
        prayerTimesData calculatedTimes = null;
        try {

            //TODO coordination has to be gotton from related class data
            calculatedTimes = myPrayerTimes.getTimes(new int[]{calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH) +1,calendar.get(Calendar.DAY_OF_MONTH)}, new Coordination(+42.9870, - 81.2432), (double) -5, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        TextView fajr = (TextView)findViewById(R.id.textView_fajr);
        fajr.setText(calculatedTimes.getFajr().getFormatedTime());

        TextView sunrise = (TextView)findViewById(R.id.textView_sunrise);
        sunrise.setText(calculatedTimes.getSunrise().getFormatedTime());

        TextView Dhuhr = (TextView)findViewById(R.id.textView_Dhuhr);
        Dhuhr.setText(calculatedTimes.getDhuhr().getFormatedTime());

        TextView asr = (TextView)findViewById(R.id.textView_asr);
        asr.setText(calculatedTimes.getAsr().getFormatedTime());

        TextView sunset = (TextView)findViewById(R.id.textView_sunset);
        sunset.setText(calculatedTimes.getSunset().getFormatedTime());

        TextView maghrib = (TextView)findViewById(R.id.textView_maghrib);
        maghrib.setText(calculatedTimes.getMaghrib().getFormatedTime());

        TextView isha = (TextView)findViewById(R.id.textView_isha);
        isha.setText(calculatedTimes.getIsha().getFormatedTime());

        TextView midnight = (TextView)findViewById(R.id.textView_midnight);
        midnight.setText(calculatedTimes.getMidnight().getFormatedTime());

        TextView imsak = (TextView)findViewById(R.id.textView_imsak);
        imsak.setText(calculatedTimes.getImsak().getFormatedTime());

    }
}