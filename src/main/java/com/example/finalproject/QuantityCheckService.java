package com.example.finalproject;

import android.app.IntentService;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.preference.PreferenceManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import java.util.Map;

public class QuantityCheckService extends IntentService {

    public QuantityCheckService() {
        super("service");
    }

    public QuantityCheckService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Bundle bundle = intent.getExtras();
        String lowQuantityMedicines = "";
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Common.myApplication);
        Map<String, Float> allMedicineQuantity = (Map<String, Float>) sp.getAll();

        for(String medName : allMedicineQuantity.keySet())
        {
            if(allMedicineQuantity.get(medName) < Common.medicineQuantityAlert)
            {
                lowQuantityMedicines += (medName + ": " + allMedicineQuantity.get(medName) + " pills left\n");
            }
        }
        if (bundle != null) {
            Messenger messenger = (Messenger) bundle.get("messenger");
            Message msg = Message.obtain();
            bundle.putCharSequence("lowQuantityMedicines",lowQuantityMedicines);
            msg.setData(bundle); //put the data here
            try {
                messenger.send(msg);
            } catch (RemoteException e) {
            }
        }
    }
}
