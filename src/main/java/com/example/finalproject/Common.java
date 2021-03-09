package com.example.finalproject;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

// general class containing static elements to use from every class
public class Common {

    public static final String[] daysOfWeek = {"Sun","Mon","Tue","Wed","Thu","Fri","Sat"};
    public static final String[] daysOfWeek_Capital = {"SUNDAY","MONDAY","TUESDAY","WEDNESDAY","THURSDAY","FRIDAY","SATURDAY"};
    public static final int numOfDaysInWeek = 7;
    public static final int[] daysBtnsID = {R.id.sunBtn, R.id.monBtn, R.id.tueBtn, R.id.wedBtn, R.id.thuBtn, R.id.friBtn, R.id.satBtn};
    public static final String greenForBtn = "#9F2EFA00";
    public static final String redForBtn = "#9FFA0505";
    public static final String fileName = "AllMedicines.txt";
    public static final float medicineQuantityAlert = 5;
    public static Activity curActivity;
    public static Application myApplication;
    private static File directory;
    private static ArrayList<Medicine> MedArray;

    public static void setDirectory(File dir)
    {
        directory = dir;
    }

    public static ArrayList<Medicine> getMedArray()
    {
        return MedArray;
    }

    //returns only the medicines of a requested day
    public static ArrayList<Medicine> getMedicinesOfSpecificDay(String day)
    {
        ArrayList<Medicine> dailyMedicines = new ArrayList<Medicine>();
        int i;
        for(i = 0; i < numOfDaysInWeek; i++) //find the index of the day
        {
            if(day.equals(daysOfWeek_Capital[i]))
                break;
        }
        for(Medicine medicine : MedArray) // get medicines of the day
        {
            if(medicine.days[i])
            {
                dailyMedicines.add(medicine);
            }
        }
        return dailyMedicines;
    }

    //initialize arraylist that contains all the medicines
    public static void initMedArray()
    {
        MedArray = getMedicinesFromCSV();
    }

    //reads all medicines from CSV internal file
    public static ArrayList<Medicine> getMedicinesFromCSV()
    {
        ArrayList<Medicine> medicinesArray = new ArrayList<Medicine>();
        BufferedReader br = null;
        int i;
        try {
            String currentLine;
            File file = new File(directory, fileName);
            br = new BufferedReader(new FileReader(file));
            while ((currentLine = br.readLine()) != null) {
                String[] values = currentLine.split(";");
                //get days of medicine (for example 1000001 means sunday and saturday)
                boolean[] days = {false, false, false, false, false, false, false};
                for(i = 0; i < numOfDaysInWeek; i++)
                {
                    if(values[3].charAt(i) == '1')
                        days[i] = true;
                }
                medicinesArray.add(new Medicine(values[0], Float.parseFloat(values[1]), Float.parseFloat(values[2]), days,
                                                values[4].equals("true"), values[5].equals("true"), values[6].equals("true"), values[7]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return medicinesArray;
    }

    //add a medicine to the CSV file.
    //toAppend = false for initializing the file
    public static void addMedicineToFile(Medicine medicine, boolean toAppend)
    {
        try {
            String medicineStr;
            if(medicine == null) //empty the file completely
            {
                medicineStr = "";
            }
            else
            {
                medicineStr = medicine.toString();
            }
            File file = new File(directory, fileName);
            // if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile(), toAppend);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(medicineStr);
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //initializing the CSV file with medicines from arraylist (when a medicine is edited)
    public static void initMedicineFile(ArrayList<Medicine> medicineArray)
    {
        if(medicineArray.size() == 0)
        {
            addMedicineToFile(null, false); // empty the file
            return;
        }
        addMedicineToFile(medicineArray.get(0), false); //first medicine starts a fresh file
        for(int i = 1; i < medicineArray.size(); i++) // appending the rest to the file
        {
            addMedicineToFile(medicineArray.get(i), true);
        }
    }

    //check if medicine name exist in the medicine list
    public static boolean isMedicineAlreadyExist(String name)
    {
        for(Medicine med : MedArray)
        {
            if(med.name.equals(name))
            {
                return true;
            }
        }
        return false;
    }

    // Initialize the medicine files (CSV and SP) with 6 medicines. **FOR TEST**
    public static void initializeFile()
    {
        boolean day1[];
        day1= new boolean[]{true, false, true, false, true, false, false};
        Medicine med1=new Medicine("Augmentin",20,30,day1,true,false,false,"https://www.drugs.com/augmentin.html");
        Common.addMedicineToFile(med1, false);
        //MedArray.add(med1);
        boolean day2[];
        day2= new boolean[]{true, true, true, true, true, true, true};
        Medicine med2=new Medicine("Amoxil",10,30,day2,true,true,false,"https://www.drugs.com/amoxil.html");
        //MedArray.add(med2);
        Common.addMedicineToFile(med2, true);
        boolean day3[];
        day3= new boolean[]{true, true, true, true, true, true, true};
        Medicine med3=new Medicine("Cipro",10,30,day3,false,false,true,"https://www.drugs.com/cipro.html");
        //MedArray.add(med3);
        Common.addMedicineToFile(med3, true);

        boolean day4[];
        day4= new boolean[]{true, false, false, true, false, false, false};
        Medicine med4=new Medicine("Keflex",10,30,day4,false,true,false,"https://www.drugs.com/keflex.html");
        //MedArray.add(med4);
        Common.addMedicineToFile(med4, true);

        boolean day5[];
        day5= new boolean[]{true, false, true, false, true, false, true};
        Medicine med5=new Medicine("Levaquin",10, (float) 2.5,day5,true,false,true,"https://www.drugs.com/levaquin.html");
        //MedArray.add(med5);
        Common.addMedicineToFile(med5, true);

        boolean day6[];
        day6= new boolean[]{true, true, true, true, true, true, true};
        Medicine med6=new Medicine("Zithromax",10,4,day6,true,false,false,"https://www.drugs.com/zithromax.html");
        //MedArray.add(med6);
        Common.addMedicineToFile(med6, true);

        // init sp file with quantity of the 6 medicines
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(myApplication);
        sp.edit().clear().commit();
        sp.edit().putFloat("Augmentin", 30).commit();
        sp.edit().putFloat("Amoxil", 30).commit();
        sp.edit().putFloat("Cipro", 30).commit();
        sp.edit().putFloat("Keflex", 30).commit();
        sp.edit().putFloat("Levaquin", (float) 2.5).commit();
        sp.edit().putFloat("Zithromax", 4).commit();
    }
}
