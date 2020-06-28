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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

public  class Sp02Activity extends AppCompatActivity implements MyInterface{

    private TextView tvUUID;

    final SpO2Fragment spO2Fragment = new SpO2Fragment();
    final RawDataFragment rawDataFragment = new RawDataFragment();
    final HeartRateFragment heartRateFragment = new HeartRateFragment();
    final FragmentManager fragmentManager = getSupportFragmentManager();

    private Fragment activeFragment = heartRateFragment;

    private int flag = 0;
    private int timestamp = 0;
    private int time = 0;
    private String MyUUID = "";

    private int red = 0;
    private int ired = 0;
    private int CPUTime = 0;
    private int spo2 = 0;
    private int bpm = 0;

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

                String data = intent.getStringExtra(BluetoothLeService.EXTRA_DATA);
                try {
                    Sp02Activity.this.setResult(data);
                } catch (JSONException e) {
                    //e.printStackTrace();
                    Log.d("SpO2Activity", "Could not parse malformed JSON: " + data);
                }
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
    public void setResult(String message) throws JSONException {
        final SpO2Fragment SpO2Fragment = (SpO2Fragment) getSupportFragmentManager().findFragmentByTag("SpO2 Fragment");
        final HeartRateFragment bpmFragment = (HeartRateFragment) getSupportFragmentManager().findFragmentByTag("Heart Rate Fragment");
        final RawDataFragment dataFragment = (RawDataFragment) getSupportFragmentManager().findFragmentByTag("Raw Fragment");

        JSONObject JSONData = new JSONObject(message);
        JSONObject JSONPayLoad = JSONData.getJSONArray("payload").getJSONObject(0);
        //Log.d("AndroidLE", "JSON Array: " + JSONPayLoad.toString());
        red = JSONPayLoad.getInt("Red");
        ired = JSONPayLoad.getInt("IR");
        CPUTime = JSONPayLoad.getInt("cpuTimestamp");
        bpm = JSONPayLoad.getInt("BPM");
        spo2 = JSONPayLoad.getInt("spO2");
        if(flag == 0) {
            tvUUID.setText(JSONData.getString("device_uuid"));
            timestamp = CPUTime;
            flag++;
        }
        time = CPUTime - timestamp;
        if(SpO2Fragment != null) {
            spO2Fragment.setFragment(spo2, time);
        }
        if(bpmFragment != null) {
            bpmFragment.setFragment(bpm, time);
        }
        if(dataFragment != null) {
            dataFragment.setFragment(red, ired, time);
        }
    }

    @Override
    public void setFragment(int valueX, int valueY, int time) {}

    @Override
    public void setFragment(int value, int time) {}

}

interface MyInterface{
    public void setResult(String message) throws JSONException;
    public void setFragment(int valueX, int valueY, int time);
    public void setFragment(int value, int time);
}
