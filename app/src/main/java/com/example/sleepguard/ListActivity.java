package com.example.sleepguard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private ImageButton toMain;
    private ImageButton toNewTask;
    private ImageButton toSettings;
    private TextView taskNum;
    private List<Task> taskList;

    private RecyclerView recyclerView;
    public static DatabaseHelper database;
    private Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        toMain = findViewById(R.id.toMainButton);
        toMain.setOnClickListener(listener);

        toNewTask = findViewById(R.id.toNewTaskButton);
        toNewTask.setOnClickListener(listener);

        toSettings = findViewById(R.id.toSettingsButton);
        toSettings.setOnClickListener(listener);

        taskNum = findViewById(R.id.taskNum);


        recyclerView = findViewById(R.id.recycler_list);
        taskList = new ArrayList<>();
        database = new DatabaseHelper(this);

        fetchAllTasks();

        recyclerView.setLayoutManager(new LinearLayoutManager(this)); // задание структуры вывода данных в recyclerView
        adapter = new Adapter(this, ListActivity.this, taskList); // инициализация адаптера и передача в рего данных из БД
        recyclerView.setAdapter(adapter); // передача в recyclerView адаптер

        String totalTime = adapter.getTotalHours() + ":" + adapter.getTotalMinutes(false);

        Intent intent = getIntent();
        String settings = "0:0.";

        if (intent.getStringExtra("settings") != null) {
            SharedPreferences sharedPref = ListActivity.this.getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(getString(R.string.settings), intent.getStringExtra("settings"));
            editor.apply();

            settings = intent.getStringExtra("settings");
        }
        else {
            SharedPreferences sharedPref = ListActivity.this.getPreferences(Context.MODE_PRIVATE);
            settings = sharedPref.getString(getString(R.string.settings), getResources().getString(R.string.default_settings));
        }

        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_time_keys), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.preference_time_keys), settings + totalTime);
        editor.apply();

        taskNum.setText("Всего задач: " + adapter.getItemCount() + "\nОбщее время выполнения:\n" + adapter.getTotalHours() + " ч " + adapter.getTotalMinutes(false) + " мин");
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

                if (view.getId() == R.id.toMainButton) {
                    Intent intentMain = new Intent(getApplicationContext(), MainActivity.class);
                    intentMain.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intentMain);
                }

                else if (view.getId() == R.id.toNewTaskButton) {
                    Intent intentNewTask = new Intent(getApplicationContext(), NewTaskActivity.class);
                    intentNewTask.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intentNewTask);
                }

                else if (view.getId() == R.id.toSettingsButton) {
                    Intent intentSettings = new Intent(getApplicationContext(), SettingsActivity.class);
                    intentSettings.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intentSettings);
                }
        }
    };

    public void fetchAllTasks(){

        Cursor cursor = database.readTasks();

        if (cursor.getCount() == 0) { // если данные отсутствую, то вывод на экран об этом тоста
//            Toast.makeText(this, "Задач нет", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()){
                taskList.add(new Task(cursor.getString(0), cursor.getString(1), cursor.getString(2) + " ч", cursor.getString(3) + " мин"));
            }
        }
    }

    public void recreate()
    {
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }
}