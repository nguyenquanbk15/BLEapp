package com.example.bleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.UUID;

public class Sp02Activity extends AppCompatActivity {

    private BluetoothGattService mBGService;
    private TextView tvData;
    private GraphView gvLineChart;
    private LineGraphSeries<DataPoint> mSeries;
    private static final UUID SERVICE_UUID = UUID.fromString("0000ffe0-0000-1000-8000-00805f9b34fb");
    private static final UUID CHAR_UUID = UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sp02);
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
    /*
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
                tvData.setText(intent.getStringExtra(BluetoothLeService.EXTRA_DATA));

                String data = intent.getStringExtra(BluetoothLeService.EXTRA_DATA);
                if(data != null) {
                    if (data.contains(",")) {
                        int[] dataInt = generateData(data);
                        mSeries.appendData(new DataPoint(dataInt[0], dataInt[1]), true, 40);
                    }
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
    */
}
