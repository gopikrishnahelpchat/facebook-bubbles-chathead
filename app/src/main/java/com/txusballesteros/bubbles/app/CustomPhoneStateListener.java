package com.txusballesteros.bubbles.app;

import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * Created by gopikrishna on 6/13/16.
 */
public class CustomPhoneStateListener extends PhoneStateListener {

    private static final String TAG = "CustomPhoneStateListene";

    @Override
    public void onCallStateChanged(int state, String incomingNumber){

//        if(incomingNumber!=null&&incomingNumber.length()>0) incoming_nr=incomingNumber;

        switch(state){
            case TelephonyManager.CALL_STATE_RINGING:
                Log.d(TAG, "CALL_STATE_RINGING");
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                Log.d(TAG, "CALL_STATE_OFFHOOK");
                break;
            case TelephonyManager.CALL_STATE_IDLE:
                Log.d(TAG, "CALL_STATE_IDLE==>");
                break;

        }
    }
}
