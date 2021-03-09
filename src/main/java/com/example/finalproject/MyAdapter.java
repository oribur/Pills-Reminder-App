package com.example.finalproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Locale;

/*
Adapter for managing recycler view of a specific day
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    public ArrayList<Medicine> dailyMedArray;
    Context myContext;
    String chosenDay;

    public MyAdapter(Context context, String chosenDay)
    {
        this.chosenDay = chosenDay;
        dailyMedArray = Common.getMedicinesOfSpecificDay(chosenDay);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.med_item, null);
        MyViewHolder viewHolder = new MyViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        /*checking the day of the week and in which day the user need to take his pill*/
        for (int i = 0; i < Common.numOfDaysInWeek; i++) { //check if current medicine is relevant to the
            if (dailyMedArray.get(position).days[i] == true && chosenDay.equals(Common.daysOfWeek_Capital[i]))
            {
                holder.med_name.setText(dailyMedArray.get(position).name);
                holder.dose.setText("Dose: " + (String.valueOf(dailyMedArray.get(position).dose)));
                holder.quantity.setText("Quantity: " + (String.valueOf(dailyMedArray.get(position).quantity)));
                String timeOnDay = "";
                if(dailyMedArray.get(position).inMorning) timeOnDay += "Morning, ";
                if(dailyMedArray.get(position).inNoon) timeOnDay += "Noon, ";
                if(dailyMedArray.get(position).inEvening) timeOnDay += "Evening ";
                holder.timeOnDay.setText(timeOnDay);

            }
        }
    }


    @Override
    public int getItemCount()
    {
            return dailyMedArray.size();
    }

    public ArrayList<Medicine> getDailyMedArray() {
        return dailyMedArray;
    }

    public void setDailyMedArray(ArrayList<Medicine> dailyMedArray)
    {
        this.dailyMedArray = dailyMedArray;
        notifyDataSetChanged();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private View view;
        private TextView dose;
        private TextView med_name;
        private TextView quantity;
        private TextView timeOnDay;

        public MyViewHolder(View v) {
            super(v);
            view = v;
            med_name = v.findViewById(R.id.med_name);
            dose = v.findViewById(R.id.dose);
            quantity = v.findViewById(R.id.quantity);
            timeOnDay=v.findViewById(R.id.morning_time);

        }
    }
}
