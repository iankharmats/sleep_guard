package com.example.sleepguard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>{

    private Context context;
    private Activity activity;
    private List<Task> taskList;
    private List<Task> newList;





    public Adapter(Context context, Activity activity, List<Task> taskList) {
        this.context = context;
        this.activity = activity;
        this.taskList = taskList;
        newList = new ArrayList<>(taskList);
    }


    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // трансформация layout-файла во View-элемент
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.title.setText(taskList.get(position).getTitle());
        holder.hours.setText(taskList.get(position).getHours());
        holder.minutes.setText(taskList.get(position).getMinutes());

//        Random random = new Random();
//        int color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
//
//        holder.title.setTint();

        holder.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.title)
                {
                    ListActivity.database.deleteSingleItem(taskList.get(position).getId());
                    Intent intent = new Intent(context, ListActivity.class);
                    activity.startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public int getTotalHours() {
        int hours = 0;

        for (Task task : taskList)
        {
            hours += Integer.parseInt(task.getHours().split(" ")[0]);
        }

        hours += getTotalMinutes(true);

        return hours;
    }

    public int getTotalMinutes(boolean returnRemain) {
        int minutes = 0;

        for (Task task : taskList)
        {
            minutes += Integer.parseInt(task.getMinutes().split(" ")[0]);
        }

        int hours = minutes / 60;
        minutes = minutes % 60;

        if (returnRemain) {
            return hours;
        }
        else {return minutes;}
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        // поля представления
        TextView title, hours, minutes;
        ConstraintLayout mLayout;
        RadioGroup radioGroup;

        // конструктор класса ViewHolder с помощью которого мы связываем поля и представление
        ViewHolder(@NonNull View view) {
            super(view);
            title = view.findViewById(R.id.title);
            hours = view.findViewById(R.id.hours);
            minutes = view.findViewById(R.id.minutes);
            mLayout = view.findViewById(R.id.mLayout);
            radioGroup = view.findViewById(R.id.radioGroup);
        }
    }

}
