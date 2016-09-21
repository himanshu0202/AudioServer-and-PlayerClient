package com.layout.playerclient;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import course.examples.Services.KeyCommon.KeyGenerator;

public class MainActivity extends Activity {

    private KeyGenerator mKeyGeneratorService;
    protected static final String TAG = MainActivity.class.getName();
    private Button mPlayClip;
    private Button mPauseClip;
    private Button mStopClip;
    private Button mResumeClip;
    private Button mRecordActivity;
    private boolean mIsBound = false;
    private GoogleApiClient client;
    private EditText mClipNumber;
    private String[] records;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPlayClip = (Button) findViewById(R.id.button);
        mPauseClip = (Button) findViewById(R.id.button2);
        mResumeClip = (Button) findViewById(R.id.button3);
        mStopClip = (Button) findViewById(R.id.button6);
        mClipNumber = (EditText) findViewById(R.id.editText);
        mRecordActivity = (Button) findViewById(R.id.button5);

        mPlayClip.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {

                            // Call KeyGenerator and get a new ID
                            if (mIsBound) {
                                String clipName = mClipNumber.getText().toString();
                                Log.i(TAG, clipName);
                                mKeyGeneratorService.onStart(clipName);
                            } else {
                                Log.i(TAG, "Service was not bound!");
                            }

                        } catch (RemoteException e) {

                            Log.e(TAG, e.toString());

                        }
                    }
                }
        );

        mPauseClip.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {

                            // Call KeyGenerator and get a new ID
                            if (mIsBound)
                                mKeyGeneratorService.onPause();
                            else {
                                Log.i(TAG, "Service was not bound!");
                            }

                        } catch (RemoteException e) {

                            Log.e(TAG, e.toString());

                        }
                    }
                }
        );

        mResumeClip.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {

                            // Call KeyGenerator and get a new ID
                            if (mIsBound)
                                mKeyGeneratorService.onResume();
                            else {
                                Log.i(TAG, "Service was not bound!");
                            }

                        } catch (RemoteException e) {

                            Log.e(TAG, e.toString());

                        }
                    }
                }
        );

        mStopClip.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {

                            // Call KeyGenerator and get a new ID
                            if (mIsBound)
                                mKeyGeneratorService.onStop();
                            else {
                                Log.i(TAG, "Service was not bound!");
                            }

                        } catch (RemoteException e) {

                            Log.e(TAG, e.toString());

                        }
                    }
                }
        );


        mRecordActivity.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {

                try {

                    // Call KeyGenerator and get a new ID
                    records = mKeyGeneratorService.getRecords();

                    Intent i = new Intent(getApplicationContext(), recordActivity.class);

                    i.putExtra("records", records);
                    startActivity(i);

                } catch (RemoteException e) {

                    Log.e(TAG, e.toString());

                }
            }
        });

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

    }

    // Bind to KeyGenerator Service
    @Override
    protected void onResume() {
        super.onResume();

        if (!mIsBound) {

            boolean b = false;
            Intent i = new Intent(KeyGenerator.class.getName());

            // Must make intent explicit or lower target API level to 19.
            ResolveInfo info = getPackageManager().resolveService(i, Context.BIND_AUTO_CREATE);
            i.setComponent(new ComponentName(info.serviceInfo.packageName, info.serviceInfo.name));

            b = bindService(i, this.mConnection, Context.BIND_AUTO_CREATE);
            if (b) {
                Log.i(TAG, "bindService() succeeded!");
            } else {
                Log.i(TAG, "bindService() failed!");
            }

        }
    }

    private final ServiceConnection mConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName className, IBinder iservice) {

            mKeyGeneratorService = (KeyGenerator.Stub.asInterface(iservice));

            mIsBound = true;

        }

        public void onServiceDisconnected(ComponentName className) {

            mKeyGeneratorService = null;

            mIsBound = false;

        }
    };


    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://playerclient.layout.com/main"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.layout.playerclient/http/playerclient.layout.com/main")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://playerclient.layout.com/main"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.layout.playerclient/http/playerclient.layout.com/main")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mClipNumber.setText("");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mIsBound = false;
        unbindService(this.mConnection);
    }
}

