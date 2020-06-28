package com.example.bleapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.UUID;

public class Sp02Activity extends AppCompatActivity implements MyInterface{

    private TextView tvUUID;

    final SpO2Fragment spO2Fragment = new SpO2Fragment();
    final RawDataFragment rawDataFragment = new RawDataFragment();
    final HeartRateFragment heartRateFragment = new HeartRateFragment();
    final FragmentManager fragmentManager = getSupportFragmentManager();

    private Fragment activeFragment = heartRateFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sp02);
        tvUUID = findViewById(R.id.tv_uuid);

        fragmentManager.beginTransaction().add(R.id.fragment_display, spO2Fragment, "SpO2 Fragment").hide(spO2Fragment).commit();
        fragmentManager.beginTransaction().add(R.id.fragment_display, rawDataFragment, "Raw Fragment").hide(rawDataFragment).commit();
        fragmentManager.beginTransaction().add(R.id.fragment_display, heartRateFragment, "Heart Rate Fragment").commit();

        final BottomNavigationView bnvControl = findViewById(R.id.bnv_control);
        bnvControl.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_sp02:
                        fragmentManager.beginTransaction().hide(activeFragment).show(spO2Fragment).commit();
                        activeFragment = spO2Fragment;
                        return true;
                    case R.id.menu_heart_rate:
                        fragmentManager.beginTransaction().hide(activeFragment).show(heartRateFragment).commit();
                        activeFragment = heartRateFragment;
                        return true;
                    case R.id.menu_raw_data:
                        fragmentManager.beginTransaction().hide(activeFragment).show(rawDataFragment).commit();
                        activeFragment = rawDataFragment;
                        return true;
                }

                return false;
            }
        });

        /*
        tvData = findViewById(R.id.tv_data_AT09);
        gvLineChart = findViewById(R.id.gv_line_chart);
        mSeries =new LineGraphSeries<>();
        gvLineChart.addSeries(mSeries);
        gvLineChart.getViewport().setXAxisBoundsManual(true);
        gvLineChart.getViewport().setMinX(0);
        gvLineChart.getViewport().setMaxX(2000);
        gvLineChart.getViewport().setMinY(100000);
        gvLineChart.getViewport().setMaxY(999999);


        mBGService = ControlActivity.mBluetoothLeService.getGattService(SERVICE_UUID);
        if (mBGService == null) {
            Log.d("Read Data Activity", "Service is not found");
        }
        final BluetoothGattCharacteristic characteristic = mBGService.getCharacteristic(CHAR_UUID);

        ControlActivity.mBluetoothLeService.readCharacteristic(characteristic);

         */
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiveData, new IntentFilter(BluetoothLeService.ACTION_DATA_AVAILABLE));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiveData);
    }

    private final BroadcastReceiver receiveData = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                //tvData.setText(intent.getStringExtra(BluetoothLeService.EXTRA_DATA));

                String data = intent.getStringExtra(BluetoothLeService.EXTRA_DATA);
                //if(data != null) {
                    //if (data.contains(",")) {
                        //int[] dataInt = generateData(data);
                        //mSeries.appendData(new DataPoint(dataInt[0], dataInt[1]), true, 40);
                    //}
                //}

            }
        }
    };

    private int[] generateData(String data) {
        int[] result = new int[2];
        String replace = data.replace("\r", "").replace("\n", "");

        String[] temp = replace.split(",");
        result[0] = Integer.parseInt(temp[0]);
        result[1] = Integer.parseInt(temp[1]);

        return result;
    };

    @Override
    public void setResult(String message) {
        final SpO2Fragment SpO2Fragment = (SpO2Fragment) getSupportFragmentManager().findFragmentByTag("SpO2 Fragment");
        final HeartRateFragment bpmFragment = (HeartRateFragment) getSupportFragmentManager().findFragmentByTag("Heart Rate Fragment");
        final RawDataFragment dataFragment = (RawDataFragment) getSupportFragmentManager().findFragmentByTag("Raw Fragment");
    }

}

interface MyInterface{
    public void setResult(String message);
}
