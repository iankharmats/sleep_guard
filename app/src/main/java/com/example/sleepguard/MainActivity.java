package com.example.sleepguard;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class MainActivity extends AppCompatActivity {

    private ImageButton toList;
    private ImageButton update;
    private TextView output;
    private TextView report;


    @SuppressLint({"SetTextI18n", "ResourceAsColor", "MissingInflatedId", "ResourceType"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toList = findViewById(R.id.toListButton);
        toList.setOnClickListener(listener);

        update = findViewById(R.id.updateButton);
        update.setOnClickListener(listener);

        output = findViewById(R.id.output);
        report = findViewById(R.id.report);



        int hour = 0;
        int minute = 0;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            hour = Integer.parseInt(DateTimeFormatter.ofPattern("HH").format(LocalDateTime.now()));
            minute = Integer.parseInt(DateTimeFormatter.ofPattern("mm").format(LocalDateTime.now()));
        }
        if (hour >= 24) {
            hour -= 24;
        }

        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_time_keys), Context.MODE_PRIVATE);
        String prefs = sharedPref.getString(getString(R.string.preference_time_keys), getResources().getString(R.string.default_preference_time_keys));

        String[] list1 = prefs.split("\\.");

        String fallAsleepTime = list1[0];
        String totalTime = list1[1];


        String[] list2 = fallAsleepTime.split(":");

        int AsleepHours = Integer.parseInt(list2[0]);
        int AsleepMinutes = Integer.parseInt(list2[1]);


        String[] list3 = totalTime.split(":");

        int totalHours = Integer.parseInt(list3[0]);
        int totalMinutes = Integer.parseInt(list3[1]);


        int deltaHour = AsleepHours - hour;
        int deltaMinute = AsleepMinutes - minute;

        if (deltaMinute < 0){
            deltaMinute = 60 + deltaMinute;
            deltaHour -= 1;
        }

        if (deltaHour < 0){
            deltaHour = 24 + deltaHour;
        }

        if (totalHours != 0 || totalMinutes != 0)
        {
            int resultHours = deltaHour - totalHours;
            int resultMinutes = deltaMinute - totalMinutes;

            if (resultMinutes < 0) {
                resultMinutes = 60 + resultMinutes;
                resultHours -= 1;
            }

            String result = resultHours + " ч " + resultMinutes + " мин";

            if (resultHours < 0) {
                result = "Нет :(";
                output.setText("Пора поторопиться!");
                output.getBackground().setTint(getResources().getColor(R.color.red));
            } else if (resultHours >= 0 && resultHours <= 1) {
                output.setText("Идeте точно по плану!");
                output.getBackground().setTint(getResources().getColor(R.color.yellow));
            } else if (resultHours >= 2) {
                output.setText("У вас еще полно времени!");
                output.getBackground().setTint(getResources().getColor(R.color.green));
            }

            report.setText("До хорошего сна осталось:\n" + deltaHour + " ч " + deltaMinute + " мин" +"\nСвободное время:\n" + result);
        }
        else
        {
            output.setText("Нет задач на сегодня");
            output.getBackground().setTint(getResources().getColor(R.color.light_grey));
            report.setText("До хорошего сна осталось:\n" + deltaHour + " ч " + deltaMinute + " мин");
        }

//        report.setText("Текущее время: " + hour + ":" + minute + "\n" + "Время отбоя: " + fallAsleepTime + "\nДо хорошего сна осталось: " + deltaHour + ":" + deltaMinute
//        + "\nОбщее время выполнения всех задач: " + totalTime + "\nСвободное время: " + result);

    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.toListButton)
            {
                Intent intentList = new Intent(getApplicationContext(), ListActivity.class);
                intentList.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intentList);
            }
            else if (view.getId() == R.id.updateButton)
            {
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
                Toast.makeText(MainActivity.this, "Обновлено!", Toast.LENGTH_SHORT).show();
            }
        }
    };
}