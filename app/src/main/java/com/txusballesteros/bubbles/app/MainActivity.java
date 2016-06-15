/*
 * Copyright Txus Ballesteros 2015 (@txusballesteros)
 *
 * This file is part of some open source application.
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 * Contact: Txus Ballesteros <txus.ballesteros@gmail.com>
 */
package com.txusballesteros.bubbles.app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.txusballesteros.bubbles.BubbleLayout;
import com.txusballesteros.bubbles.BubblesManager;
import com.txusballesteros.bubbles.OnInitializedCallback;
import com.txusballesteros.bubbles.app.service.DialerStateService;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class MainActivity extends ActionBarActivity {

    private BubblesManager bubblesManager;
    private Intent dialerIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeBubblesManager();

        findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewBubble();
            }
        });
        dialerIntent = new Intent(this, DialerStateService.class);
        startService(dialerIntent);
    }

    private void addNewBubble() {
        final BubbleLayout bubbleView = (BubbleLayout)LayoutInflater.from(MainActivity.this).inflate(R.layout.bubble_layout, null);
        bubbleView.setOnBubbleRemoveListener(new BubbleLayout.OnBubbleRemoveListener() {
            @Override
            public void onBubbleRemoved(BubbleLayout bubble) { }
        });
        bubbleView.setOnBubbleClickListener(new BubbleLayout.OnBubbleClickListener() {

            @Override  // Show Dialog on Click of bubble.
            public void onBubbleClick(BubbleLayout bubble) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Low Balance Alert");
                builder.setMessage("Your Balance Seems to be low , click OK to recharge with Helpchat App");
                builder.setCancelable(true);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        /*
                        // To deeplink it to respective rechargepage of the app.
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        Uri uri = Uri.parse("helpchat://helpchat/recharge");
                        intent.setData(uri);*/

                        // To Directly Launch Helpchat First Page of App
                        Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage("com.akosha.directtalk");
                        startActivity(LaunchIntent);

                        //remove bubble
                        bubblesManager.removeBubble(bubbleView);
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();

                wmlp.gravity = Gravity.TOP | Gravity.LEFT;
                wmlp.x = bubbleView.getViewParams().x ; //x position
                wmlp.y = ( (bubbleView.getViewParams().y + bubbleView.getMeasuredHeight()));   //y position set below bubble
                dialog.getWindow().setAttributes(wmlp);
                dialog.show();
            }
        });
        bubbleView.setShouldStickToWall(true);
        bubblesManager.addBubble(bubbleView, 80, 100);
    }

    private void initializeBubblesManager() {
        bubblesManager = new BubblesManager.Builder(this)
                                    .setTrashLayout(R.layout.bubble_trash_layout)
                                    .setInitializationCallback(new OnInitializedCallback() {
                                        @Override
                                        public void onInitialized() {
                                            //addNewBubble();
                                        }
                                    })
                                    .build();
        bubblesManager.initialize();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bubblesManager.recycle();
        stopService(dialerIntent);
    }
}
