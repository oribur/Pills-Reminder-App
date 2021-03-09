package com.example.finalproject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

// Class that deals with the add/edit medicine details
public class MedicinePageActivity extends AppCompatActivity {

    private Medicine selectedMedicine;
    TextView doseTxt;
    TextView nameTxt;
    TextView quantityTxt;
    TextView websiteTxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_medicine);

        Common.curActivity = this;

        selectedMedicine = new Medicine((Medicine)getIntent().getSerializableExtra("selectedMedicine"));

        /* Initialize the selected medicine values */
        nameTxt = (TextView)findViewById(R.id.editMedicineName);
        nameTxt.setText(selectedMedicine.name);

        doseTxt = (TextView)findViewById(R.id.editDoseVal);
        doseTxt.setText(String.valueOf(selectedMedicine.dose));

        quantityTxt = (TextView)findViewById(R.id.editQuantityTxt);
        quantityTxt.setText(String.valueOf(selectedMedicine.quantity));

        websiteTxt = (TextView)findViewById(R.id.editWebsiteTxt);
        websiteTxt.setText(selectedMedicine.website);

        /* Initialize days buttons */
        for(int i = 0; i < Common.numOfDaysInWeek; i++)
        {
            if(selectedMedicine.days[i])
            {
                ((Button)findViewById(Common.daysBtnsID[i])).setBackgroundColor(Color.parseColor(Common.greenForBtn));
            }
            else{
                ((Button)findViewById(Common.daysBtnsID[i])).setBackgroundColor(Color.parseColor(Common.redForBtn));
            }
        }
        View.OnClickListener dayBtnListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int viewId = v.getId();
                for(int i = 0; i < Common.numOfDaysInWeek; i++)
                {
                    if(Common.daysBtnsID[i] == viewId) {
                        if(selectedMedicine.days[i])
                        {
                            v.setBackgroundColor(Color.parseColor(Common.redForBtn));
                        }
                        else{
                            v.setBackgroundColor(Color.parseColor(Common.greenForBtn));
                        }
                        selectedMedicine.days[i] = !selectedMedicine.days[i];
                        return;
                    }
                }
            }
        };
        for(int i = 0; i < Common.numOfDaysInWeek; i++)
        {
            ((Button)findViewById(Common.daysBtnsID[i])).setOnClickListener(dayBtnListener);
        }

        /* Initialize time in day buttons */
        if(selectedMedicine.inMorning)
        {
            ((Button)findViewById(R.id.morningBtn)).setBackgroundColor(Color.parseColor(Common.greenForBtn));
        }
        else{
            ((Button)findViewById(R.id.morningBtn)).setBackgroundColor(Color.parseColor(Common.redForBtn));
        }
        if(selectedMedicine.inNoon)
        {
            ((Button)findViewById(R.id.noonBtn)).setBackgroundColor(Color.parseColor(Common.greenForBtn));
        }
        else{
            ((Button)findViewById(R.id.noonBtn)).setBackgroundColor(Color.parseColor(Common.redForBtn));
        }
        if(selectedMedicine.inEvening)
        {
            ((Button)findViewById(R.id.eveningBtn)).setBackgroundColor(Color.parseColor(Common.greenForBtn));
        }
        else{
            ((Button)findViewById(R.id.eveningBtn)).setBackgroundColor(Color.parseColor(Common.redForBtn));
        }
        View.OnClickListener timeBtnListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isSelected = true;

                switch(v.getId())
                {
                    case R.id.morningBtn:
                        if(selectedMedicine.inMorning)
                            isSelected = false;
                        selectedMedicine.inMorning = !selectedMedicine.inMorning;
                        break;
                    case R.id.noonBtn:
                        if(selectedMedicine.inNoon)
                            isSelected = false;
                        selectedMedicine.inNoon = !selectedMedicine.inNoon;
                        break;
                    case R.id.eveningBtn:
                        if(selectedMedicine.inEvening)
                            isSelected = false;
                        selectedMedicine.inEvening = !selectedMedicine.inEvening;
                        break;
                }
                if(isSelected)
                {
                    v.setBackgroundColor(Color.parseColor(Common.greenForBtn));
                }
                else{
                    v.setBackgroundColor(Color.parseColor(Common.redForBtn));
                }
            }
        };
        ((Button)findViewById(R.id.morningBtn)).setOnClickListener(timeBtnListener);
        ((Button)findViewById(R.id.noonBtn)).setOnClickListener(timeBtnListener);
        ((Button)findViewById(R.id.eveningBtn)).setOnClickListener(timeBtnListener);

        View.OnClickListener doseBtnListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float change = Float.parseFloat(((Button)v).getText().toString());
                selectedMedicine.dose += change;
                selectedMedicine.dose = Float.max(selectedMedicine.dose, 0); //dose can't be negative
                doseTxt.setText(String.valueOf(selectedMedicine.dose));
            }
        };
        ((Button)findViewById(R.id.addDoseBtn)).setOnClickListener(doseBtnListener);
        ((Button)findViewById(R.id.subDoseBtn)).setOnClickListener(doseBtnListener);

        final Context context = this;
        ((Button)findViewById(R.id.saveBtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nameTxt.getText().length() == 0)
                {
                    Toast.makeText(context, "Must enter name!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!selectedMedicine.name.equals(nameTxt.getText().toString()))
                {
                    if(Common.isMedicineAlreadyExist(nameTxt.getText().toString())) //check if medicine name already exist
                    {
                        Toast.makeText(context, "Medicine Already Exist!", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Save Changes?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                int resultCode;
                                Intent intent = new Intent(context, MedicineListActivity.class);
                                if(selectedMedicine.name.equals("")) //new medicine to add
                                    resultCode = 2;
                                else{
                                    resultCode = 1;
                                }
                                intent.putExtra("oldName", selectedMedicine.name); //save the old name for faster edit in recyclerview
                                selectedMedicine.name = nameTxt.getText().toString();
                                selectedMedicine.dose = Float.parseFloat(doseTxt.getText().toString());
                                selectedMedicine.quantity = Float.parseFloat(quantityTxt.getText().toString());
                                selectedMedicine.website = websiteTxt.getText().toString() + " ";
                                intent.putExtra("changedMedicine", selectedMedicine);
                                setResult(resultCode, intent);
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //do nothing
                            }
                        });
                builder.show();
            }
        });

        ((Button)findViewById(R.id.cancelBtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //just go back to medicine list
                setResult(1);
                finish();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("savedMed", selectedMedicine);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Medicine savedMed = (Medicine)savedInstanceState.getSerializable("savedMed");
        doseTxt.setText(String.valueOf(savedMed.dose));
        /* Initialize days buttons */
        for(int i = 0; i < Common.numOfDaysInWeek; i++)
        {
            if(savedMed.days[i])
            {
                ((Button)findViewById(Common.daysBtnsID[i])).setBackgroundColor(Color.parseColor(Common.greenForBtn));
            }
            else{
                ((Button)findViewById(Common.daysBtnsID[i])).setBackgroundColor(Color.parseColor(Common.redForBtn));
            }
        }
        if(savedMed.inMorning)
        {
            ((Button)findViewById(R.id.morningBtn)).setBackgroundColor(Color.parseColor(Common.greenForBtn));
        }
        else{
            ((Button)findViewById(R.id.morningBtn)).setBackgroundColor(Color.parseColor(Common.redForBtn));
        }
        if(savedMed.inNoon)
        {
            ((Button)findViewById(R.id.noonBtn)).setBackgroundColor(Color.parseColor(Common.greenForBtn));
        }
        else{
            ((Button)findViewById(R.id.noonBtn)).setBackgroundColor(Color.parseColor(Common.redForBtn));
        }
        if(savedMed.inEvening)
        {
            ((Button)findViewById(R.id.eveningBtn)).setBackgroundColor(Color.parseColor(Common.greenForBtn));
        }
        else{
            ((Button)findViewById(R.id.eveningBtn)).setBackgroundColor(Color.parseColor(Common.redForBtn));
        }
    }
}
