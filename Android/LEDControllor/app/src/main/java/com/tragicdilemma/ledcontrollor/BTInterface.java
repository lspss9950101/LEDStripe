package com.tragicdilemma.ledcontrollor;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class BTInterface {

    public static final int REQUEST_ENABLE_BT = 1;
    public static final UUID uuid = UUID.fromString("210ea8a1-e32b-414f-a4c0-fa557f77c4b7");

    private static BTInterface instance;
    private Context context;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket bluetoothSocket;
    private OutputStream outputStream;

    private BTInterface(Context context){
        this.context = context;
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter == null)return;
    }

    public static BTInterface getInstance(Context context){
        if(instance == null)instance = new BTInterface(context);
        return instance;
    }

    public void enableBT(){
        if(!instance.bluetoothAdapter.isEnabled()){
            Intent enable_BT_It = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            ((Activity)instance.context).startActivityForResult(enable_BT_It, REQUEST_ENABLE_BT);
        }
    }

    public boolean isEnable(){
        return instance.bluetoothAdapter.isEnabled();
    }

    public Set<BluetoothDevice> getPairedDevice(){
        return bluetoothAdapter.getBondedDevices();
    }

    public void connect(BluetoothDevice bluetoothDevice){
        BluetoothSocket tmp = null;
        try{
            tmp = bluetoothDevice.createRfcommSocketToServiceRecord(uuid);
        } catch (IOException e) {
            e.printStackTrace();
        }
        bluetoothSocket = tmp;

        Thread connectThread = new Thread(new Runnable() {
            @Override
            public void run() {
                bluetoothAdapter.cancelDiscovery();
                try{
                    bluetoothSocket.connect();
                } catch (IOException e) {
                    e.printStackTrace();
                    closeSocket();
                }
            }
        });

        if(bluetoothSocket == null)return;
        connectThread.start();
    }

    public boolean isConnected(){
        if(bluetoothSocket != null && bluetoothSocket.isConnected())return true;
        return false;
    }

    public void write(byte bytes[]){
        OutputStream tmp = null;
        try {
            tmp = bluetoothSocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        outputStream = tmp;

        try{
            outputStream.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean closeSocket(){
        try {
            bluetoothSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        bluetoothSocket = null;
        return true;
    }

}
