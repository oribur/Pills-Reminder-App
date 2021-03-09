package com.example.finalproject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

public class QuantityCheckHandler extends Handler {

    private MsgReceiver msgReceiver;
    public QuantityCheckHandler(MsgReceiver msgReceiver)
    {
        this.msgReceiver = msgReceiver;
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        super.handleMessage(msg);
        Bundle reply = msg.getData();
        msgReceiver.onMessageReceived(reply.getCharSequence("lowQuantityMedicines").toString());
    }

    public interface MsgReceiver{
        void onMessageReceived(String message);
    }
}
