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
import android.widget.Toast;

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
    private LineGraphSeries<DataPoint> bpmSeries, spo2Series;

    private int flag = 0;
    private int value0 = 0;
    private int spo2 = 0;
    private int bpm = 0;
    private int time = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        initWidgets();

        // Raw Graph
//        redSeries = new LineGraphSeries<>();
//        redSeries.setColor(Color.rgb(227, 53, 18));
//        rawGraph.addSeries(redSeries);
//
//        iredSeries = new LineGraphSeries<>();
//        iredSeries.setColor(Color.rgb(29, 242, 10));
//        rawGraph.addSeries(iredSeries);

        spo2Series = new LineGraphSeries<>();
        spo2Series.setColor(Color.rgb(227, 53, 18));
        rawGraph.addSeries(spo2Series);

        rawGraph.getViewport().setXAxisBoundsManual(true);
        rawGraph.getViewport().setMinX(0);
        rawGraph.getViewport().setMaxX(8000);
        rawGraph.getViewport().setYAxisBoundsManual(true);
        rawGraph.getViewport().setMinY(0);
        rawGraph.getViewport().setMaxY(200);

        rawGraph.getViewport().setScalable(true);
        rawGraph.setTitle("SpO2 Graph");
        rawGraph.getGridLabelRenderer().setHorizontalAxisTitle("times");
        rawGraph.getGridLabelRenderer().setVerticalAxisTitle("values");

        // bmp graph
        bpmSeries = new LineGraphSeries<>();
        bpmSeries.setColor(Color.rgb(29, 242, 10));
        bpmGraph.addSeries(bpmSeries);

        bpmGraph.getViewport().setXAxisBoundsManual(true);
        bpmGraph.getViewport().setMinX(0);
        bpmGraph.getViewport().setMaxX(8000);
        bpmGraph.getViewport().setYAxisBoundsManual(true);
        bpmGraph.getViewport().setMinY(0);
        bpmGraph.getViewport().setMaxY(200);

        bpmGraph.getViewport().setScalable(true);
        bpmGraph.setTitle("Heart Rate Graph");
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
                //Log.d("dataReceive", data);
                if(!data.contains("command")) {
                    data = data.substring(0,data.length() - 1);
                    String[] dataBuffer = data.split(";");
                    tvSpO2.setText(dataBuffer[1] + " %");
                    tvHeartRate.setText(dataBuffer[0] + " bpm");
                    try {
                        spo2 = Integer.parseInt(dataBuffer[1]);
                        bpm = Integer.parseInt(dataBuffer[0]);
                        time = Integer.parseInt(dataBuffer[2]);
                        if (flag == 0) {
                            value0 = time;
                            flag++;
                        }
                        int valueX = time - value0;
                        spo2Series.appendData(new DataPoint(valueX, spo2), true, 10000);
                        bpmSeries.appendData(new DataPoint(valueX, bpm), true, 10000);
                    } catch (final NumberFormatException e) {
                        Toast.makeText(context, "Can't String to int", Toast.LENGTH_SHORT).show();
                        Log.e("ChartActivity", "exception", e);
                    }
                }


//                if(data != null){
//                    try {
//                        JSONObject JSONData = new JSONObject(data);
//
//                        JSONObject JSONPayLoad = JSONData.getJSONArray("payload").getJSONObject(0);
//                        tvSpO2.setText(JSONPayLoad.getInt("spO2") + " %");
//                        tvHeartRate.setText(JSONPayLoad.getInt("BPM") + " bpm");
//
//                        red = JSONPayLoad.getInt("Red");
//                        ired = JSONPayLoad.getInt("IR");
//                        CPUTime = JSONPayLoad.getInt("cpuTimestamp");
//                        bpm = JSONPayLoad.getInt("BPM");
//                        //Log.d("ChartActivity", "onReceive: " + red + " " + ired + " " + CPUTime);
//
//                        if(flag == 0) {
//                            String deviceUUID = JSONData.getString("device_uuid");
//                            String deviceUUIDs = deviceUUID.substring(4,8);
//                            tvUUID.setText(deviceUUIDs);
//                            timestamp = CPUTime;
//                            flag++;
//                        }
//
//                        int time = CPUTime - timestamp;
//
//                        redSeries.appendData(new DataPoint(time,red), true, 100);
//                        iredSeries.appendData(new DataPoint(time, ired), true, 100);
//                        bpmSeries.appendData(new DataPoint(time,bpm), true, 100);
//
//                    } catch (JSONException e) {
//                        Log.d("ChartActivity", "Could not parse malformed JSON: " + data);
//                    }
//                }

            }
        }
    };
}
