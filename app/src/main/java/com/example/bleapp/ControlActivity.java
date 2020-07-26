package com.example.bleapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ControlActivity extends AppCompatActivity {

    private final static String TAG ="ControlActivity";

    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";
    public static final String COMMAND = "command";
    public static final String CONNECT_WIFI = "Connect to wifi";
    public static final String ENABLE_EMIT = "Enable emit";
    public static final String DISABLE_EMIT = "Disable emit";
    public static final String SCAN_WIFI = "Scan nearby wifi";
    public static final String WIFI_NAME = "wifi ssid";
    public static final String WIFI_PASS = "wifi pass";

    private TextView mConnectionState;
    private TextView mDeviceName;
    private TextView mDevice;
    private Button btnButton;
    private Button btnSendData;
    private String mDeviceAddress;
    private ExpandableListView mGattServicesList;
    public static BluetoothLeService mBluetoothLeService;
    private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics =
            new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
    private boolean mConnected = false;
    private BluetoothGattCharacteristic mNotifyCharacteristic;
    public static BluetoothGattCharacteristic mWriteCharacteristic;

    private final String LIST_NAME = "NAME";
    private final String LIST_UUID = "UUID";

    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBluetoothLeService = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);
        initWidgets();

        final Intent deviceIntent = getIntent();
        String device = deviceIntent.getStringExtra(EXTRAS_DEVICE_ADDRESS);
        mDeviceAddress = device.substring(device.length() - 17);
        String deviceName = device.substring(0, device.length() - 18);
        mDeviceName.setText(deviceName);
        mDevice.setText(mDeviceAddress);
        btnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject jsonDisableEmit = new JSONObject();
                try {
                    jsonDisableEmit.put(COMMAND, DISABLE_EMIT);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String disableEmitData = jsonDisableEmit.toString() + '\n';
                Log.d("Control Activity", "Command" + disableEmitData);
                byte[] noEmit = disableEmitData.getBytes();
                mWriteCharacteristic.setValue(noEmit);
                mBluetoothLeService.writeCharacteristic(mWriteCharacteristic);
                Intent intent = new Intent(ControlActivity.this, ChartActivity.class);
                startActivity(intent);
            }
        });
        btnSendData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String flag = "send_data";
                //byte[] flagByte = flag.getBytes();
                //mWriteCharacteristic.setValue(flagByte);
                //mBluetoothLeService.writeCharacteristic(mWriteCharacteristic);

                Intent intent = new Intent(ControlActivity.this, SendWifiActivity.class);
                startActivity(intent);
            }
        });

        mGattServicesList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                if (mGattCharacteristics != null) {
                    final BluetoothGattCharacteristic characteristic =
                            mGattCharacteristics.get(groupPosition).get(childPosition);
                    final int charaProp = characteristic.getProperties();
                    if ((charaProp | BluetoothGattCharacteristic.PROPERTY_READ) > 0) {
                        // If there is an active notification on a characteristic, clear
                        // it first so it doesn't update the data field on the user interface.
                        if (mNotifyCharacteristic != null) {
                            mBluetoothLeService.setCharacteristicNotification(
                                    mNotifyCharacteristic, false);
                            mNotifyCharacteristic = null;
                        }
                        mBluetoothLeService.readCharacteristic(characteristic);
                    }
                    if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
                        mNotifyCharacteristic = characteristic;
                        mBluetoothLeService.setCharacteristicNotification(
                                characteristic, true);
                    }

                    if (((characteristic.getProperties() & BluetoothGattCharacteristic.PROPERTY_WRITE) |
                            (charaProp & BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE)) > 0) {
                        // writing characteristic functions
                        mWriteCharacteristic = characteristic;
                    }

                    btnButton.setEnabled(true);
                    btnSendData.setEnabled(true);
                    //JSONObject jsonDisableEmit = new JSONObject();
                    //try {
                    //    jsonDisableEmit.put(ControlActivity.COMMAND, ControlActivity.DISABLE_EMIT);
                    //   Log.d("ControlActivity","data: " + jsonDisableEmit.toString());
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    //String disableEmitData = jsonDisableEmit.toString() + '\n';
//                    byte[] noEmit = disableEmitData.getBytes();
//                    mWriteCharacteristic.setValue(noEmit);
//                    mBluetoothLeService.writeCharacteristic(mWriteCharacteristic);
//                    for(int i = 0; i < 1; i++){
//                        String wifi = "hello world\n";
//                        byte[] wifiByte = wifi.getBytes();
//                        mWriteCharacteristic.setValue(wifiByte);
//                        mBluetoothLeService.writeCharacteristic(mWriteCharacteristic);
//
//                    }


//                    final Handler loopHandler = new Handler();
//                    loopHandler.post(new Runnable () {
//                        @Override
//                        public void run() {
//                            String wifi = "hello world\n";
//                            byte[] wifiByte = wifi.getBytes();
//                            mWriteCharacteristic.setValue(wifiByte);
//                            mBluetoothLeService.writeCharacteristic(mWriteCharacteristic);
//                            //loopHandler.postDelayed(this, 1000);
//                        }
//                    });

                    return true;
                }

                return false;
            }
        });

        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mGattUpdateReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
        mBluetoothLeService = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_control_menu, menu);
        if (mConnected) {
            menu.findItem(R.id.menu_connect).setVisible(false);
            menu.findItem(R.id.menu_disconnect).setVisible(true);
        } else {
            menu.findItem(R.id.menu_connect).setVisible(true);
            menu.findItem(R.id.menu_disconnect).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_connect:
                mBluetoothLeService.connect(mDeviceAddress);
                break;
            case R.id.menu_disconnect:
                mBluetoothLeService.disconnect();
                btnButton.setEnabled(false);
                break;
            case android.R.id.home:
            case R.id.back:
                onBackPressed();
                break;
        }
        return true;
    }

    private void initWidgets() {
        mDevice = findViewById(R.id.tv_device_address);
        mConnectionState = findViewById(R.id.tv_connection_state);
        mDeviceName = findViewById(R.id.tv_data_value);
        btnButton = findViewById(R.id.btn_button);
        btnSendData = findViewById(R.id.btn_send_data);

        mGattServicesList = this.findViewById(R.id.elv_gatt_services_list);
    }

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                mConnected = true;
                updateConnectionState(R.string.connected);
                invalidateOptionsMenu();
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                mConnected = false;
                updateConnectionState(R.string.disconnected);
                invalidateOptionsMenu();
                clearUI();
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                // Show all the supported services and characteristics on the user interface.
                displayGattServices(mBluetoothLeService.getSupportedGattServices());
            }
            /*else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                displayData(intent.getStringExtra(BluetoothLeService.EXTRA_DATA));
            }
            */
        }
    };

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        //intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

    private void updateConnectionState(final int resourceId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mConnectionState.setText(resourceId);
            }
        });
    }

    private void clearUI() {
        mGattServicesList.setAdapter((SimpleExpandableListAdapter) null);
        //mDataField.setText(R.string.no_data);
    }

    private void displayData(String data) {
        if (data != null) {
            //mDataField.setText(data);
        }
    }

    private void displayGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null) return;
        String uuid;
        String unknownServiceString = getResources().getString(R.string.unknown_service);
        String unknownCharaString = getResources().getString(R.string.unknown_characteristic);
        ArrayList<HashMap<String, String>> gattServiceData = new ArrayList<HashMap<String, String>>();
        ArrayList<ArrayList<HashMap<String, String>>> gattCharacteristicData
                = new ArrayList<ArrayList<HashMap<String, String>>>();
        mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();

        // Loops through available GATT Services.
        for (BluetoothGattService gattService : gattServices) {
            HashMap<String, String> currentServiceData = new HashMap<String, String>();
            uuid = gattService.getUuid().toString();
            currentServiceData.put(
                    LIST_NAME, SampleGattAttributes.lookup(uuid, unknownServiceString));
            currentServiceData.put(LIST_UUID, uuid);
            gattServiceData.add(currentServiceData);

            ArrayList<HashMap<String, String>> gattCharacteristicGroupData =
                    new ArrayList<HashMap<String, String>>();
            List<BluetoothGattCharacteristic> gattCharacteristics =
                    gattService.getCharacteristics();
            ArrayList<BluetoothGattCharacteristic> charas =
                    new ArrayList<BluetoothGattCharacteristic>();

            // Loops through available Characteristics.
            for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                charas.add(gattCharacteristic);
                HashMap<String, String> currentCharaData = new HashMap<String, String>();
                uuid = gattCharacteristic.getUuid().toString();
                currentCharaData.put(
                        LIST_NAME, SampleGattAttributes.lookup(uuid, unknownCharaString));
                currentCharaData.put(LIST_UUID, uuid);
                gattCharacteristicGroupData.add(currentCharaData);
            }
            mGattCharacteristics.add(charas);
            gattCharacteristicData.add(gattCharacteristicGroupData);
        }

        SimpleExpandableListAdapter gattServiceAdapter = new SimpleExpandableListAdapter(
                this,
                gattServiceData,
                android.R.layout.simple_expandable_list_item_2,
                new String[] {LIST_NAME, LIST_UUID},
                new int[] { android.R.id.text1, android.R.id.text2 },
                gattCharacteristicData,
                android.R.layout.simple_expandable_list_item_2,
                new String[] {LIST_NAME, LIST_UUID},
                new int[] { android.R.id.text1, android.R.id.text2 }
        );
        mGattServicesList.setAdapter(gattServiceAdapter);
    }
}
