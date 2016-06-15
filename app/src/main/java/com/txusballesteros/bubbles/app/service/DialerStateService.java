package com.txusballesteros.bubbles.app.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by gopikrishna on 6/15/16.
 */
public class DialerStateService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();

        final String str = "";
        Timer timer  =  new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            int phonelaunched = 0,phoneclosed =0;
            int phonelaunches = 1;
            @Override
            public void run() {
                ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfo = am.getRunningAppProcesses();

                for ( ActivityManager.RunningAppProcessInfo appProcess: runningAppProcessInfo ) {
                    if (appProcess.processName.equals("com.android.dialer")) {
                        Log.d(appProcess.processName.toString(),"is running");
                        if ( appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND  /*isForeground(getApplicationContext(),runningAppProcessInfo.get(i).processName)*/){
                            if (phonelaunched == 0 ){
                                phonelaunched = 1;
                                Log.d(str,"dude phone has been launched");
                                System.out.println("dude phone has been launched");
                            }
                            else if (phoneclosed == 1){
                                phonelaunches++;
                                phoneclosed = 0;
                                Log.d(str+" " +String.valueOf(phonelaunches),"dude that was counter");
                                System.out.println(String.valueOf(phonelaunches) + "dude that was counter");
                            }
                        }
                        else {
                            phoneclosed = 1;
                            Log.d(str, "dude phone has been closed");
                            System.out.println("dude phone has been closed");

                        }
                    }
                }
            }
        },2000,3000);

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
