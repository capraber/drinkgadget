package com.globant.iot.drinkgadget;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.punchthrough.bean.sdk.*;
import java.util.*;
import com.punchthrough.bean.sdk.message.*;
import android.view.*;
import android.view.View.*;

import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import android.app.NotificationManager;
import android.support.v4.app.NotificationCompat;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import at.grabner.circleprogress.AnimationState;
import at.grabner.circleprogress.AnimationStateChangedListener;
import at.grabner.circleprogress.CircleProgressView;
import at.grabner.circleprogress.TextMode;
import at.grabner.circleprogress.UnitPosition;

import android.preference.*;
import android.content.SharedPreferences;

public class Main extends AppCompatActivity {

    final List<Bean> beans = new ArrayList<>();
    byte[] rx_data = new byte[1024];
    int rx_index = 0;
    boolean notification_sent;
    byte temperatureSelected = 4;
    CircleProgressView mCircleViewTemperature;
    CircleProgressView mCircleViewBatteryLevel;
    boolean convertToFahrenheit = !true;


    boolean connected = false;

    void initView()
    {
        initSession();

    }
    void initSession()
    {

        beans.clear();

        Button button = (Button) findViewById(R.id.buttonSearch);
        button.setVisibility(View.VISIBLE);
        ProgressBar spinner = (ProgressBar) findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);

        notification_sent = false;

        //circles
        mCircleViewTemperature = (CircleProgressView) findViewById(R.id.circleViewTemperature);
        mCircleViewBatteryLevel = (CircleProgressView) findViewById(R.id.circleViewBatteryLevel);
        mCircleViewTemperature.setMaxValue(100);
        //mCircleViewTemperature.setMinValue(-10);
        mCircleViewTemperature.setValue(0);
        mCircleViewTemperature.setUnit("°C");
        mCircleViewTemperature.setUnitVisible(false);
        mCircleViewTemperature.setVisibility(View.GONE);

        mCircleViewBatteryLevel.setMaxValue(100);
        mCircleViewBatteryLevel.setValue(0);
        mCircleViewBatteryLevel.setUnit("%");
        mCircleViewBatteryLevel.setVisibility(View.GONE);

        TextView text = (TextView) findViewById(R.id.textViewTemperature);
        text.setVisibility(View.GONE);
        text = (TextView) findViewById(R.id.textViewBatteryLevel);
        text.setVisibility(View.GONE);


    }

    protected void showNotification(String title, String message)
    {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_fingerprint_black_24dp)
                        .setContentTitle(title)
                        .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                        .setContentText(message);

        Intent notificationIntent = new Intent(this, Main.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }

    protected void showPopup(String message)
    {
        Context context = getApplicationContext();
        Toast.makeText(context, message,
                Toast.LENGTH_LONG).show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initView();
        Button button = (Button) findViewById(R.id.buttonSearch);
        button.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // do the "on click" action here
                initSearch();

            }
        });






    }

    public void buttonSearch(View v)
    {
        initSearch();

    }

    protected void initSearch()
    {
        //disable search button
        Button button = (Button) findViewById(R.id.buttonSearch);
        button.setVisibility(View.GONE);
        //show loading
        ProgressBar spinner = (ProgressBar)findViewById(R.id.progressBar);
        spinner.setVisibility(View.VISIBLE);


        BeanDiscoveryListener listener = new BeanDiscoveryListener() {
            @Override
            public void onBeanDiscovered(Bean bean, int rssi) {

                //System.out.println("Encontrado uno!!!");

                //add bean if not yet included
                boolean new_bean = true;
                for(int c = 0; c < beans.size(); c++)
                {
                    if(beans.get(c).getDevice().getAddress().equals(bean.getDevice().getAddress()))
                    {
                        new_bean = false;
                        break;
                    }
                }

                if(new_bean)
                {
                    beans.add(bean);
                    System.out.println("Encontrado uno!!!");
                }

            }

            @Override
            public void onDiscoveryComplete() {
                // This is called when the scan times out, defined by the .setScanTimeout(int seconds) method

                for (Bean bean : beans) {
                    System.out.println(bean.getDevice().getName());   // "Bean"              (example)
                    System.out.println(bean.getDevice().getAddress());    // "B4:99:4C:1E:BC:75" (example)
                }

                //connect to first one detected
                if(beans.size() > 0) {
                    connect();
                }
                else
                {
                    //reset
                    initSession();
                    showPopup("No widgets detected. Check that Bluetooth is enabled.");
                }
            }
        };



        BeanManager.getInstance().setScanTimeout(5);  // Timeout in seconds, optional, default is 30 seconds
        BeanManager.getInstance().startDiscovery(listener);
    }

    protected void updateViewInfo(byte temperature, byte battery_level)
    {
        /*
        //update temperature info
        TextView text = (TextView) findViewById(R.id.textViewTemperature);
        text.setText(Byte.toString(temperature) + "°C");
        //update battery level info
        text = (TextView) findViewById(R.id.textViewBatteryLevel);
        text.setText(Byte.toString(battery_level) + "%");
*/
        mCircleViewTemperature = (CircleProgressView) findViewById(R.id.circleViewTemperature);
        mCircleViewBatteryLevel = (CircleProgressView) findViewById(R.id.circleViewBatteryLevel);

        if(convertToFahrenheit)
        {
            mCircleViewTemperature.setUnit("°F");

            temperature = (byte)(1.8 * (float)temperature + 32);
        }
        else
        {
            mCircleViewTemperature.setUnit("°C");
        }
        mCircleViewTemperature.setUnitVisible(true);
        mCircleViewTemperature.setValue(temperature);

        mCircleViewBatteryLevel.setValue(battery_level);
    }

    protected void connect()
    {
        // Assume we have a reference to the 'beans' ArrayList from above.
        final Bean bean = beans.get(0);//[0];

        //clear rx data
        rx_index = 0;

        BeanListener beanListener = new BeanListener() {

            @Override
            public void onConnected() {
                System.out.println("onConnected");
                //disable spinner
                ProgressBar spinner = (ProgressBar)findViewById(R.id.progressBar);
                spinner.setVisibility(View.GONE);

                //enable info container (TBD)
                /*
                TextView text = (TextView) findViewById(R.id.textViewTemperatureLabel);
                text.setVisibility(View.VISIBLE);
                text = (TextView) findViewById(R.id.textViewTemperature);
                text.setVisibility(View.VISIBLE);
                text = (TextView) findViewById(R.id.textViewBatteryLevelLabel);
                text.setVisibility(View.VISIBLE);
                text = (TextView) findViewById(R.id.textViewBatteryLevel);
                text.setVisibility(View.VISIBLE);
*/

                mCircleViewTemperature.setVisibility(View.VISIBLE);
                mCircleViewBatteryLevel.setVisibility(View.VISIBLE);
                TextView text = (TextView) findViewById(R.id.textViewTemperature);
                text.setVisibility(View.VISIBLE);
                text = (TextView) findViewById(R.id.textViewBatteryLevel);
                text.setVisibility(View.VISIBLE);


                bean.readDeviceInfo(new Callback<DeviceInfo>() {
                    @Override
                    public void onResult(DeviceInfo deviceInfo) {
                        System.out.println(deviceInfo.hardwareVersion());
                        System.out.println(deviceInfo.firmwareVersion());
                        System.out.println(deviceInfo.softwareVersion());
                    }
                });

                connected = true;
            }

            @Override
            public void onConnectionFailed() {
                System.out.println("onConnectionFailed");
            }

            @Override
            public void onDisconnected() {
                connected = false;

                System.out.println("onDisconnected");
                initSession();
                showPopup("Widget disconnected");


            }

            @Override
            public void onSerialMessageReceived(byte[] data)
            {
                System.out.println("onSerialMessageReceived");
                //System.out.println("Longitud del mensaje " + data.length);


                for(int c=0;c< data.length;c++)
                {
                    rx_data[rx_index++] = data[c];
                    if(2 == rx_index)
                    {
                        //get temperature
                        System.out.println("Temperature: " + Byte.toString(rx_data[0]));
                        //get battery level %
                        System.out.println("Battery: " + Byte.toString(rx_data[1]));
                        rx_index = 0;
                        byte temperature = rx_data[0];
                        byte battery_level = rx_data[1];
                        updateViewInfo(temperature, battery_level);

                        if(!notification_sent)
                        {
                            if(temperature <= temperatureSelected)
                            {
                                showNotification("Drink Widget", "Your drink is ready! Enjoy it!");
                                notification_sent = true;
                            }
                        }
                        break;
                    }
                }


            }

            @Override
            public void onScratchValueChanged(ScratchBank bank, byte[] value)
            {
                System.out.println("onScratchValueChanged");
            }

            @Override
            public void onError(BeanError error)
            {
                System.out.println("onError");
            }

            @Override
            public void onReadRemoteRssi(int i) {
                System.out.println("onReadRemoteRssi");
            }
            // In practice you must implement the other Listener methods
            //...
        };

        // Assuming you are in an Activity, use 'this' for the context
        bean.connect(this, beanListener);
    }





}
