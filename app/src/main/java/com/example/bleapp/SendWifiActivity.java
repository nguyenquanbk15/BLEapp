package com.example.bleapp;

import androidx.annotation.LongDef;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothGattCharacteristic;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SendWifiActivity extends AppCompatActivity {

    private Button btnReload;
    private ListView lvWifi;
    private CustomWifiAdapter WifiAdapter;
    private ArrayList<Wifi> WifiList = new ArrayList<>();
    public static final String WIFI_NAME = "WIFI NAME";

    private final String NONE = "none";

    //private int flag = 0;

    private BluetoothGattCharacteristic mWriteCharacteristic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_wifi);

        mWriteCharacteristic = ControlActivity.mWriteCharacteristic;

        btnReload = findViewById(R.id.btn_reload);
        lvWifi = findViewById(R.id.lv_list_wifi);

        WifiAdapter = new CustomWifiAdapter(this, R.layout.item_wifi, WifiList);
        lvWifi.setAdapter(WifiAdapter);

        lvWifi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SendWifiActivity.this, LoginWifiActivity.class);
                Wifi wifiObj = (Wifi) parent.getItemAtPosition(position);
                intent.putExtra(WIFI_NAME, wifiObj.getWifiName());
                startActivity(intent);
            }
        });
        btnReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SendWifiActivity.this, "Request reload wifi send to remote device", Toast.LENGTH_SHORT).show();
                JSONObject reload = new JSONObject();
                try {
                    reload.put(ControlActivity.COMMAND, ControlActivity.SCAN_WIFI);
                    //Log.d("SenWifiActivity", "JSON: " + reload.toString());
                } catch (JSONException e) {
                    //Log.d("SenWifiActivity", "Could not parse malformed JSON:");
                }
                String disableEmitData = reload.toString() + '\n';
                byte[] reloadByte = disableEmitData.getBytes();
                mWriteCharacteristic.setValue(reloadByte);
                ControlActivity.mBluetoothLeService.writeCharacteristic(mWriteCharacteristic);
//                String wifi = "nguyenquannguyennam151515215P\n";
//                byte[] wifiByte = wifi.getBytes();
//                mWriteCharacteristic.setValue(wifiByte);
//                ControlActivity.mBluetoothLeService.writeCharacteristic(mWriteCharacteristic);

//                final Handler loopHandler = new Handler();
//                    loopHandler.post(new Runnable () {
//                        @Override
//                        public void run() {
//                            String wifi = "nguyenquannguyennam151515215P\n";
//                            byte[] wifiByte = wifi.getBytes();
//                            mWriteCharacteristic.setValue(wifiByte);
//                            ControlActivity.mBluetoothLeService.writeCharacteristic(mWriteCharacteristic);
//                            loopHandler.postDelayed(this, 1000);
//                        }
//                    });

            }
        });
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
                    JSONObject JSONData = new JSONObject(data);
                    JSONArray jsonListWifi = JSONData.getJSONArray("wifi list");
                    Log.d( "SenWifiActivity", "" + jsonListWifi.toString());
                    ArrayList<Wifi> listWifi = new ArrayList<>();
                    int len = jsonListWifi.length();
                    String channel = "";
                    for (int i = 0; i < len; i++){
                        JSONObject JSONItem = jsonListWifi.getJSONObject(i);
                        if(JSONItem.has("channel")) {
                            channel = JSONItem.getString("channel");
                        }
                        else{
                            channel = NONE;
                        }
                        Wifi dataItem = new Wifi(JSONItem.getString("ssid"),
                                                JSONItem.getString("rssi"),
                                                channel,
                                                JSONItem.getString("enc"));
                        listWifi.add(dataItem);
                    }
//                    if (flag == 0) {
//                        WifiAdapter = new CustomWifiAdapter(SendWifiActivity.this, android.R.layout.item_wifi,listWifi);
//                        lvWifi.setAdapter(WifiAdapter);
//                    }
//                    else{
                        WifiAdapter.UpdateWifiAdapter(listWifi);
//                    }
//                    flag++;
                } catch (JSONException e) {
                    Log.d("SenWifiActivity", "Could not parse malformed JSON:" + data);
                }
            }
        }
    };
}
