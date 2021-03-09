package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;

public class Medicine implements Serializable {

    String name;
    float dose;
    float quantity;
    boolean[] days;
    boolean inMorning;
    boolean inNoon;
    boolean inEvening;
    String website;

    public Medicine(String name, float dose, float quantity, boolean[] days, boolean inMorning, boolean inNoon, boolean inEvening, String website)
    {
        this.name = name;
        this.dose = dose;
        this.quantity = quantity;
        this.days = days;
        this.inMorning = inMorning;
        this.inNoon = inNoon;
        this.inEvening = inEvening;
        this.website = website;
    }

    public Medicine(Medicine medicine)
    {
        this.name = medicine.name;
        this.dose = medicine.dose;
        this.quantity = medicine.quantity;
        this.days = medicine.days;
        this.inMorning = medicine.inMorning;
        this.inNoon = medicine.inNoon;
        this.inEvening = medicine.inEvening;
        this.website = medicine.website;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        Medicine toCompare = (Medicine)obj;
        for(int i = 0; i < Common.numOfDaysInWeek; i++)
        {
            if(days[i] != toCompare.days[i])
                return false;
        }
        return(name.equals(toCompare.name) && dose == toCompare.dose && quantity == toCompare.quantity && inMorning == toCompare.inMorning &&
                inNoon == toCompare.inNoon && inEvening == toCompare.inEvening && website.equals(toCompare.website));
    }

    @NonNull
    @Override
    public String toString() {
        String result = "";
        result += (name +";");
        result += (dose +";");
        result += (quantity +";");
        for(int i = 0; i < Common.numOfDaysInWeek; i++)
        {
            if(days[i])
                result += "1";
            else result += "0";
        }
        result += (";" + inMorning +";");
        result += (inNoon +";");
        result += (inEvening +";");
        result += (website + "\n");
        return result;
    }
}
