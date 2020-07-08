package com.example.bleapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginWifiActivity extends AppCompatActivity {

    private TextView tvWifiName;
    private EditText edtPassWord;
    private Button btnConnectWifi;
    private Button btnDisplayData;
    private CheckBox cbShowPassWord;

    private String wifiName;
    private String wifiPassword;

    private BluetoothGattCharacteristic mWriteCharacteristic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_wifi);
        initWidgets();
        mWriteCharacteristic = ControlActivity.mWriteCharacteristic;

        final Intent wifiIntent = getIntent();
        wifiName = wifiIntent.getStringExtra(SendWifiActivity.WIFI_NAME);
        tvWifiName.setText(wifiName);

        cbShowPassWord.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    edtPassWord.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else{
                    edtPassWord.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        btnConnectWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wifiPassword = edtPassWord.getText().toString();
                JSONObject jsonWifi = new JSONObject();
                try {
                    jsonWifi.put(ControlActivity.COMMAND, ControlActivity.CONNECT_WIFI);
                    jsonWifi.put(ControlActivity.WIFI_NAME, wifiName);
                    jsonWifi.put(ControlActivity.WIFI_PASS, wifiPassword);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String wifiData = jsonWifi.toString() + '\n';
                byte[] flagByte = wifiData.getBytes();
                mWriteCharacteristic.setValue(flagByte);
                ControlActivity.mBluetoothLeService.writeCharacteristic(mWriteCharacteristic);
            }
        });

        btnDisplayData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginWifiActivity.this, ChartActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initWidgets() {
        tvWifiName = findViewById(R.id.tv_wifi_name);
        edtPassWord = findViewById(R.id.edt_password);
        btnConnectWifi = findViewById(R.id.btn_connect_wifi);
        btnDisplayData = findViewById(R.id.btn_display_data_wifi);
        cbShowPassWord = findViewById(R.id.cb_show_password);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.emit_wifi_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_emit_server:
                item.setChecked(true);
                //Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show();
                JSONObject jsonEmit = new JSONObject();
                try {
                    jsonEmit.put(ControlActivity.COMMAND, ControlActivity.ENABLE_EMIT);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String emitData = jsonEmit.toString() + '\n';
                byte[] flagByte = emitData.getBytes();
                mWriteCharacteristic.setValue(flagByte);
                ControlActivity.mBluetoothLeService.writeCharacteristic(mWriteCharacteristic);
                break;
            case R.id._menu_disable_emit:
                item.setChecked(true);
                JSONObject jsonDisableEmit = new JSONObject();
                try {
                    jsonDisableEmit.put(ControlActivity.COMMAND, ControlActivity.DISABLE_EMIT);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String disableEmitData = jsonDisableEmit.toString() + '\n';
                byte[] noEmit = disableEmitData.getBytes();
                mWriteCharacteristic.setValue(noEmit);
                ControlActivity.mBluetoothLeService.writeCharacteristic(mWriteCharacteristic);
                break;
        }
        return true;
    }
}
