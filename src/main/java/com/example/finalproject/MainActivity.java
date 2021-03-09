package com.example.finalproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;

//responsible for the app's main page - the medicines of the current day
public class MainActivity extends AppCompatActivity implements View.OnClickListener, QuantityCheckHandler.MsgReceiver {
    private Button WeeklyPills,Took_the_pills;
    private Button AllPills;
    private TextView day_of_the_week;
    private CheckBox morning_check,noon_check,eve_check;
    RecyclerView recyclerView;
    Handler handler;
    private static boolean isQuantityChecked;
    private String currDay;
    MyAdapter adapter;
    private NetworkConnectionReceiver networkConnectionReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //register the broadcast receiver to keep track of the connection of the device.
        IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        networkConnectionReceiver = new NetworkConnectionReceiver();
        registerReceiver(networkConnectionReceiver, intentFilter);


        Common.myApplication = getApplication();
        Common.setDirectory(getFilesDir());
        //Common.initializeFile(); // FOR TESTING. uncomment to initialize files
        Common.initMedArray();
        Common.curActivity = this;

        // start the intent service
        if(!isQuantityChecked) { //runs the service only once
            Intent intent = new Intent(this, QuantityCheckService.class);
            handler = new QuantityCheckHandler(this);
            intent.putExtra("messenger", new Messenger(handler));
            startService(intent);
            isQuantityChecked = true;
        }
        setUpUI();
        currDay = LocalDate.now(ZoneId.of("Israel")).getDayOfWeek().name();
        /************************************************************************************/
        /*recycle view for getting the pills for the current day */
        recyclerView = this.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyAdapter( this, currDay);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
         /***********************************************************************************/
        day_of_the_week.setText(currDay);

        /************************************************************************************/
        /*anonymous class listener for moving to activity that contain the weekly pills*/
        WeeklyPills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(v.getId()==R.id.button3)
                {
                    MoveToWeeklyPills();
                }
            }
        });
        /************************************************************************************/

        /************************************************************************************/
        /* Moving to the screen of all pills --> inner class listener */
        AllPills.setOnClickListener(this);
        /************************************************************************************/


    }
    /*set up the ui on the screen with the correct id */
    private void setUpUI()
    {
        WeeklyPills = (Button)findViewById(R.id.button3);
        AllPills=(Button)findViewById(R.id.button4);
        day_of_the_week=(TextView)findViewById(R.id.textView3);
        morning_check=(CheckBox)findViewById((R.id.checkBoxmorning));
        noon_check=(CheckBox)findViewById((R.id.checkBoxnoon));
        eve_check=(CheckBox)findViewById((R.id.checkBoxevening));
        Took_the_pills=(Button)findViewById((R.id.buttonTookMyPills));
    }
    /*moving on to see the weekly pills */
    public void MoveToWeeklyPills()
    {
        Intent intent=new Intent(MainActivity.this,weeklyActivity.class);
        startActivity(intent);
    }
    /* moving to see all pills */
    @Override
    public void onClick(View v)
    {
        Intent intent=new Intent(MainActivity.this,MedicineListActivity.class);
        startActivity(intent);
    }

    /* Check which check box pressed by the user after he pressed on the button took_my_pills*/
    public void ClickTookMyPills(View v)
    {
        if(morning_check.isChecked())
        {
            ReduceQuantity(0);
            morning_check.setChecked(false);
        }
        if(noon_check.isChecked())
        {
            ReduceQuantity(1);
            noon_check.setChecked(false);
        }
        if(eve_check.isChecked())
        {
            ReduceQuantity(2);
            eve_check.setChecked(false);
        }
    }
    /* time in day: morning =0
                    noon=1
                    evening=2
      reduce the quantity of the pills in the adapter and files after checking the check box
    */
    public void ReduceQuantity(int time_in_day)
    {
        ArrayList<Medicine> medArr = adapter.getDailyMedArray();
        ArrayList<Medicine> allMeds= Common.getMedArray();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Common.myApplication);
        for(Medicine medicine : medArr) //reduce the quantity of the daily relevant medicines + update sp file
        {
            if((time_in_day == 0 && medicine.inMorning))
            {
                medicine.quantity -= medicine.dose;
                if(medicine.quantity < 0)
                    medicine.quantity=0;
                sp.edit().putFloat(medicine.name, medicine.quantity).commit();
            }

            if(time_in_day == 1 && medicine.inNoon)
            {
                medicine.quantity-=medicine.dose;
                if(medicine.quantity < 0)
                    medicine.quantity=0;
                sp.edit().putFloat(medicine.name, medicine.quantity).commit();
            }
            if(time_in_day == 2 && medicine.inEvening)
            {
                medicine.quantity-=medicine.dose;
                if(medicine.quantity < 0)
                    medicine.quantity=0;
                sp.edit().putFloat(medicine.name, medicine.quantity).commit();
            }
        }
        adapter.setDailyMedArray(medArr);

        //update the CSV file with the new data
        for(Medicine medicine : allMeds)
        {
            for (Medicine medicine1 : medArr)
            {
                if (medicine.name.equals(medicine1.name))
                {
                    medicine.quantity = medicine1.quantity;
                }
            }
        }
        Common.initMedicineFile(allMeds);
    }

    //get message from the service with the low quantity medicines and pops a dialog
    @Override
    public void onMessageReceived(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Common.curActivity);
        builder.setTitle("Low Quantity Medicines:").setMessage(message) //medicines and quantity
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        builder.show();
    }

    //when application is destroyed, unregister the broadcastReceiver
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(networkConnectionReceiver);
    }
}