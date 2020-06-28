package com.example.bleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.Series;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ChartActivity extends AppCompatActivity {

    private TextView tvSpO2, tvHeartRate, tvUUID;
    private GraphView rawGraph, bpmGraph;
    private LineGraphSeries<DataPoint> redSeries, iredSeries, bpmSeries;

    private int flag = 0;
    private int timestamp = 0;
    private int red = 0;
    private int ired = 0;
    private int CPUTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        initWidgets();

        // Raw Graph
        redSeries = new LineGraphSeries<>();
        redSeries.setColor(Color.rgb(227, 53, 18));
        rawGraph.addSeries(redSeries);

        iredSeries = new LineGraphSeries<>();
        iredSeries.setColor(Color.rgb(29, 242, 10));
        rawGraph.addSeries(iredSeries);

        rawGraph.getViewport().setXAxisBoundsManual(true);
        rawGraph.getViewport().setMinX(0);
        rawGraph.getViewport().setMaxX(10000);
        rawGraph.setTitle("Raw Data");
        rawGraph.getGridLabelRenderer().setHorizontalAxisTitle("times");
        rawGraph.getGridLabelRenderer().setVerticalAxisTitle("values");

        // bmp graph
        bpmSeries = new LineGraphSeries<>();
        bpmGraph.addSeries(bpmSeries);
        bpmGraph.getViewport().setXAxisBoundsManual(true);
        bpmGraph.getViewport().setMinX(0);
        bpmGraph.getViewport().setMaxX(10000);
        bpmGraph.setTitle("bpm");
        bpmGraph.getGridLabelRenderer().setHorizontalAxisTitle("times");
        bpmGraph.getGridLabelRenderer().setVerticalAxisTitle("bpm");
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

    private void initWidgets() {
        tvSpO2 = findViewById(R.id.tv_spo2_text_view);
        tvHeartRate = findViewById(R.id.tv_bpm_text_view);
        tvUUID = findViewById(R.id.tv_device_uuid);

        rawGraph = findViewById(R.id.gv_raw_data_chart);
        bpmGraph = findViewById(R.id.gv_heart_rate_chart);

    }

    private final BroadcastReceiver receiveData = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {


                String data = intent.getStringExtra(BluetoothLeService.EXTRA_DATA);
                //data = data.replace("\r", "").replace("\n", "");
                //tvUUID.setText(data);

                if(data != null){
                    try {
                        JSONObject JSONData = new JSONObject(data);
                        Log.d("ChartActivity", "onReceive: " + JSONData.toString());
                        /*
                        JSONObject JSONPayLoad = JSONData.getJSONArray("payload").getJSONObject(0);
                        Log.d("AndroidLE", "JSON Array: " + JSONPayLoad.toString());
                        tvSpO2.setText(JSONPayLoad.getInt("spO2") + "%");
                        tvHeartRate.setText(JSONPayLoad.getInt("BPM") + "bpm");
                        red = JSONPayLoad.getInt("Red");
                        ired = JSONPayLoad.getInt("IR");
                        CPUTime = JSONPayLoad.getInt("cpuTimestamp");

                        if(flag == 0) {
                            String deviceUUID = JSONData.getString("device_uuid");
                            //deviceUUID = deviceUUID.substring(4,8);
                            tvUUID.setText(deviceUUID);
                            timestamp = CPUTime;
                            flag++;
                        }
                        int time = CPUTime - timestamp;
                        redSeries.appendData(new DataPoint(red,time), true, 250);
                        iredSeries.appendData(new DataPoint(ired, time), true, 250);
                        bpmSeries.appendData(new DataPoint(ired, time), true, 250);
                         */
                    } catch (JSONException e) {
                        Log.d("ChartActivity", "Could not parse malformed JSON:" + data);
                    }
                }

            }
        }
    };
}
