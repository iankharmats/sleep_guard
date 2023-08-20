package com.example.sleepguard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class NewTaskActivity extends AppCompatActivity {

    private ImageButton createNewTask;
    private ImageButton cancelNewTask;

    private EditText titleIn, hoursIn, minutesIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        createNewTask = findViewById(R.id.createNewTaskButton);
        createNewTask.setOnClickListener(listener);

        cancelNewTask = findViewById(R.id.cancelNewTaskButton);
        cancelNewTask.setOnClickListener(listener);

        titleIn = findViewById(R.id.titleIn);
        hoursIn = findViewById(R.id.hoursIn);
        minutesIn = findViewById(R.id.minutesIn);
    }
    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.createNewTaskButton) {

                if (!TextUtils.isEmpty(titleIn.getText().toString()) && (!TextUtils.isEmpty(hoursIn.getText().toString()) || !TextUtils.isEmpty(minutesIn.getText().toString()))){

                    boolean isOnlyDigits = true;
                    if (!TextUtils.isEmpty(hoursIn.getText().toString())) {
                        for (int i = 0; i < hoursIn.getText().toString().length() && isOnlyDigits; i++) {
                            if (!Character.isDigit(hoursIn.getText().toString().charAt(i))) {
                                isOnlyDigits = false;
                            }
                        }
                    }
                    if (!TextUtils.isEmpty(minutesIn.getText().toString())) {
                        for (int i = 0; i < minutesIn.getText().toString().length() && isOnlyDigits; i++) {
                            if (!Character.isDigit(minutesIn.getText().toString().charAt(i))) {
                                isOnlyDigits = false;
                            }
                        }
                    }


                    if (isOnlyDigits) {

                        String hours = "0";
                        String minutes = "0";
                        boolean isReduced = true;

                        if (!TextUtils.isEmpty(hoursIn.getText().toString())) {
                            int hoursInt = Integer.parseInt(hoursIn.getText().toString());
                            if (hoursInt > 0) {
                                hours = Integer.toString(hoursInt);
                            }
                            if (hoursInt >= 24) {
                                isReduced = false;
                            }
                        }

                        if (!TextUtils.isEmpty(minutesIn.getText().toString())) {
                            int minutesInt = Integer.parseInt(minutesIn.getText().toString());
                            if (minutesInt > 0) {
                                minutes = Integer.toString(minutesInt);
                            }
                            if (minutesInt >= 60) {
                                isReduced = false;
                            }
                        }

                        if (isReduced)
                        {
                            DatabaseHelper database = new DatabaseHelper(NewTaskActivity.this); // создание объекта БД в текущей активности
                            database.addTask(titleIn.getText().toString(), hours, minutes); // создание записи в БД


                            Intent intent = new Intent(NewTaskActivity.this, ListActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

                            finish();
                        }
                        else {
                            Toast.makeText(NewTaskActivity.this, "Время введено некоректно!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(NewTaskActivity.this, "Время введено некоректно!", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(NewTaskActivity.this, "Необходимо заполнить название и хотя бы одно поле времени!", Toast.LENGTH_SHORT).show();
                }
            }

            else if (view.getId() == R.id.cancelNewTaskButton) {
                Intent intentCancel = new Intent(getApplicationContext(), ListActivity.class);
                intentCancel.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intentCancel);
            }
        }
    };
}