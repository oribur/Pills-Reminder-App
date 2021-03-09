package com.example.finalproject;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.ArrayList;

//responsible for the medicines data in the recyclerview that contains all of the medicines
public class MedicineListAdapter extends RecyclerView.Adapter<MedicineListAdapter.MedicineDetailsViewHolder>{

    private ArrayList<Medicine> medicines;
    private Activity myActivity;
    public Medicine selectedMedicine;
    private int editPosition;

    //constructor: get relevant data
    public MedicineListAdapter(Activity myActivity)
    {
        this.myActivity = myActivity;
        medicines = Common.getMedArray();
    }

    //creates each item of the recyclerview
    @NonNull
    @Override
    public MedicineDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.medicine_item, null);
        MedicineDetailsViewHolder viewHolder = new MedicineDetailsViewHolder(itemLayoutView);
        return viewHolder;
    }

    //binding the data to the view on each item
    @Override
    public void onBindViewHolder(@NonNull MedicineDetailsViewHolder holder, final int position) {
        //set text data
        holder.medicineName.setText(medicines.get(position).name);
        holder.dose.setText(String.valueOf(medicines.get(position).dose));
        holder.quantity.setText(String.valueOf(medicines.get(position).quantity));
        holder.days.setText(getDaysFromArr(medicines.get(position).days));
        String timeOnDay = "";
        if(medicines.get(position).inMorning) timeOnDay += "Morning, ";
        if(medicines.get(position).inNoon) timeOnDay += "Noon, ";
        if(medicines.get(position).inEvening) timeOnDay += "Evening ";
        holder.timeOnDay.setText(timeOnDay);

        //anonymous class for dealing with click on a button
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //save the position for easier edit and go to editMedicine page
                editPosition = position;
                selectedMedicine = medicines.get(position);
                Intent intent = new Intent(myActivity, MedicinePageActivity.class);
                intent.putExtra("selectedMedicine", selectedMedicine);
                myActivity.startActivityForResult(intent, 1);
            }
        });
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pops dialog to make sure that delete was intended
                AlertDialog.Builder builder = new AlertDialog.Builder(myActivity);
                builder.setMessage("Delete " + medicines.get(position).name + "?").setTitle("Delete Medicine")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //remove from sp file
                                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Common.myApplication);
                                sp.edit().remove(medicines.get(position).name).commit();

                                //remove from medicine list and internal file
                                Medicine pressedMedicine = medicines.get(position);
                                medicines.remove(pressedMedicine);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, getItemCount());

                                Common.initMedicineFile(medicines); // make changes in file
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //do nothing
                            }
                        });
                builder.show();
            }
        });
        holder.websiteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check connection
                if(!NetworkConnectionReceiver.isConnected)
                {
                    Toast.makeText(myActivity,"No Internet Connection!", Toast.LENGTH_LONG).show();
                    return;
                }
                //check valid address
                if(medicines.get(position).website.length() < 8)
                {
                    Toast.makeText(myActivity,"No Valid Website", Toast.LENGTH_SHORT).show();
                }
                else{ //go to website
                    if (!medicines.get(position).website.startsWith("http://") && !medicines.get(position).website.startsWith("https://"))
                        medicines.get(position).website = "http://" + medicines.get(position).website;
                    Uri webpage = Uri.parse(medicines.get(position).website);
                    Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                    if (intent.resolveActivity(myActivity.getPackageManager()) != null) {
                        myActivity.startActivity(intent);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return medicines.size();
    }

    //returns string of the relevant days from a boolean array
    private String getDaysFromArr(boolean[] days)
    {
        String daysStr = " ";
        boolean isEveryDay = true;
        for(int i = 0; i< Common.numOfDaysInWeek; i++)
        {
            if(days[i])
            {
                daysStr += Common.daysOfWeek[i] + ", ";
            }
            else{
                isEveryDay = false;
            }
        }
        if(isEveryDay)
        {
            daysStr = "Every Day";
        }
        else if(!(daysStr.equals(" ")))
        {
            daysStr = daysStr.substring(0, daysStr.length()-2); //cut the last ', '
        }
        return daysStr;
    }

    //add new medicine to the arraylist and notify recyclerview
    public void addNewMedicine(Medicine newMedicine)
    {
        medicines.add(newMedicine);
        notifyItemInserted(getItemCount());
    }

    //switching the received medicine with the medicine positioned in editPosition.
    //used after editing a medicine
    public void switchMedicines(Medicine newMedicine)
    {
        medicines.remove(editPosition);
        medicines.add(editPosition, newMedicine);
        notifyItemChanged(editPosition);
    }

    // updating th CSV file
    public void writeToFile()
    {
        Common.initMedicineFile(medicines);
    }

    //internal class containing the view elements of an item on the recyclerview
    public static class MedicineDetailsViewHolder extends RecyclerView.ViewHolder
    {
        private TextView medicineName;
        private TextView dose;
        private TextView quantity;
        private TextView timeOnDay;
        private TextView days;
        private Button editButton;
        private Button deleteButton;
        private Button websiteButton;

        public MedicineDetailsViewHolder(View v) {
            super(v);
            medicineName = v.findViewById(R.id.medicineNameTxt);
            dose = v.findViewById(R.id.doseNum);
            quantity = v.findViewById(R.id.quantityNum);
            timeOnDay = v.findViewById(R.id.timeOnDayTxt);
            days = v.findViewById(R.id.daysVal);
            editButton = v.findViewById(R.id.editBtn);
            deleteButton = v.findViewById(R.id.deleteBtn);
            websiteButton = v.findViewById(R.id.websiteBtn);
        }

        public TextView getMedicineName() {
            return medicineName;
        }

        public TextView getDose() {
            return dose;
        }

        public TextView getQuantity() {
            return quantity;
        }

        public TextView getTimeOnDay() {
            return timeOnDay;
        }

        public TextView getDays() {
            return days;
        }

        public Button getEditButton() {
            return editButton;
        }

        public Button getDeleteButton() {
            return deleteButton;
        }

        public Button getWebsiteButton() {
            return websiteButton;
        }
    }
}
