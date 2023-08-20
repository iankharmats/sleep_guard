package com.example.sleepguard;

import static android.app.PendingIntent.getActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;


public class SettingsActivity extends AppCompatActivity {

    private ImageButton saveChanges;
    private ImageButton cancelChanges;

    private EditText wakeMinuteIn, wakeHourIn, sleepMinuteIn, sleepHourIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        saveChanges = findViewById(R.id.saveChangesButton);
        saveChanges.setOnClickListener(listener);

        cancelChanges = findViewById(R.id.cancelChangesButton);
        cancelChanges.setOnClickListener(listener);

        wakeMinuteIn = findViewById(R.id.wakeMinuteIn);
        wakeHourIn = findViewById(R.id.wakeHourIn);
        sleepMinuteIn = findViewById(R.id.sleepMinuteIn);
        sleepHourIn = findViewById(R.id.sleepHourIn);

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        String prefs = sharedPref.getString(getString(R.string.show_edit), getResources().getString(R.string.default_show_edit));

        String[] list = prefs.split("\\.");

        wakeHourIn.setHint(list[0].toString());
        wakeMinuteIn.setHint(list[1].toString());

        sleepHourIn.setHint(list[2].toString());
        sleepMinuteIn.setHint(list[3].toString());

    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.saveChangesButton) {
                if (!TextUtils.isEmpty(wakeMinuteIn.getText().toString()) && !TextUtils.isEmpty(wakeHourIn.getText().toString())
                        && !TextUtils.isEmpty(sleepMinuteIn.getText().toString()) && !TextUtils.isEmpty(sleepHourIn.getText().toString()))
                    {
                        boolean isOnlyDigits = true;
                        EditText[] lst = new EditText[] {wakeMinuteIn, wakeHourIn, sleepMinuteIn, sleepHourIn};

                        for(EditText editText : lst) {
                            for (int i = 0; i < editText.getText().toString().length() && isOnlyDigits; i++) {
                                if (!Character.isDigit(editText.getText().toString().charAt(i))) {
                                    isOnlyDigits = false;
                                    break;
                                }
                            }
                        }

                        if (isOnlyDigits) {
                            boolean isReduced = true;

                            EditText[] hoursIn = new EditText[] {wakeHourIn, sleepHourIn};
                            for(EditText editText : hoursIn) {
                                if (!TextUtils.isEmpty(editText.getText().toString())) {
                                    int hoursInt = Integer.parseInt(editText.getText().toString());
                                    if (hoursInt >= 24) {
                                        isReduced = false;
                                    }
                                }
                            }


                            EditText[] minutesIn = new EditText[] {wakeMinuteIn, sleepMinuteIn};
                            for(EditText editText : minutesIn) {
                                if (!TextUtils.isEmpty(editText.getText().toString())) {
                                    int minutesInt = Integer.parseInt(editText.getText().toString());
                                    if (minutesInt >= 60) {
                                        isReduced = false;
                                    }
                                }
                            }

                            if (isReduced)
                            {

                                int fallAsleepHour = Integer.parseInt(wakeHourIn.getText().toString()) - Integer.parseInt(sleepHourIn.getText().toString());

                                int fallAsleepMinute = Integer.parseInt(wakeMinuteIn.getText().toString()) - Integer.parseInt(sleepMinuteIn.getText().toString());

                                if (fallAsleepMinute < 0){
                                    fallAsleepMinute = 60 + fallAsleepMinute;
                                    fallAsleepHour -= 1;
                                }

                                if (fallAsleepHour < 0){
                                    fallAsleepHour = 24 + fallAsleepHour;
                                }


                                SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putString(getString(R.string.show_edit), wakeHourIn.getText().toString() + "." + wakeMinuteIn.getText().toString()
                                        + "." + sleepHourIn.getText().toString() + "." + sleepMinuteIn.getText().toString());
                                editor.apply();

                                Intent intentSave = new Intent(getApplicationContext(), ListActivity.class);
                                intentSave.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

                                intentSave.putExtra("settings", fallAsleepHour+ ":" + fallAsleepMinute + ".");

                                startActivity(intentSave);

                                Toast.makeText(SettingsActivity.this, "Настройки сохранены!", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(SettingsActivity.this, "Время введено некоректно!", Toast.LENGTH_SHORT).show();
                            }

                        }
                        else{
                            Toast.makeText(SettingsActivity.this, "Настройки введены некоректно!", Toast.LENGTH_SHORT).show();
                        }
                    }
                else {
                    Toast.makeText(SettingsActivity.this, "Необходимо заполнить все поля!", Toast.LENGTH_SHORT).show();
                }
            }

            else if (view.getId() == R.id.cancelChangesButton) {
                Intent intentCancel = new Intent(getApplicationContext(), ListActivity.class);
                intentCancel.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intentCancel);
            }
        }
    };
}