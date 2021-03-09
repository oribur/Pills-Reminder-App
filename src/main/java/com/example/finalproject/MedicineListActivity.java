package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MedicineListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    MedicineListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.medicine_list);

        recyclerView = this.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MedicineListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        Common.curActivity = this;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.addItem:
                // create an empty medicine and go to edit medicine page
                boolean[] emptyDays = {false, false, false, false, false, false, false};
                Intent intent = new Intent(this, MedicinePageActivity.class);
                Medicine emptyMedicine = new Medicine("", 0, 0, emptyDays, false, false, false, " ");
                intent.putExtra("selectedMedicine", emptyMedicine);
                startActivityForResult(intent, 2);
                break;
            case R.id.backItem:
                Intent intent2=new Intent(this,MainActivity.class);
                startActivity(intent2);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_back_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null) //changes where made to a medicine
        {
            Medicine receivedMedicine = (Medicine)data.getSerializableExtra("changedMedicine");
            String oldName = data.getStringExtra("oldName");
            // add/update the quantity in sp file
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Common.myApplication);
            sp.edit().remove(oldName).commit();
            sp.edit().putFloat(receivedMedicine.name, receivedMedicine.quantity).commit();
            if(resultCode == 1) // edited medicine
            {
                adapter.switchMedicines(receivedMedicine);
                adapter.writeToFile();
            }
            else{ // resultCode == 2, new medicine added
                adapter.addNewMedicine(receivedMedicine);
                Common.addMedicineToFile(receivedMedicine, true);
            }
        }
    }
}