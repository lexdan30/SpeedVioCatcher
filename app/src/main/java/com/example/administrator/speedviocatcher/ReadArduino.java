package com.example.administrator.speedviocatcher;

//package com.example.espirobluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class ReadArduino extends Activity implements OnItemClickListener{

    Button Visible;
    ArrayAdapter<String> listAdapter;
    ListView listView;
    BluetoothAdapter btAdapter;
    Set<BluetoothDevice> devicesArray;
    ArrayList<String> pairedDevices;
    ArrayList<BluetoothDevice> devices;
    public static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    protected static final int SUCCESS_CONNECT = 0;
    protected static final int MESSAGE_READ = 1;
    IntentFilter filter;
    BroadcastReceiver receiver;
    String tag = "debugging";
    Handler mHandler = new Handler(){
        public void handleMessage(Message msg) {
            Log.i(tag,"in handler");
            super.handleMessage(msg);
            switch(msg.what){
                case SUCCESS_CONNECT:
                    //DO SOMETHING
                    ConnectedThread connectedThread = new ConnectedThread((BluetoothSocket)msg.obj);
                    Toast.makeText(getApplicationContext(), "Conectar", Toast.LENGTH_SHORT).show();
                    String s = "conectado con Éxito";
                    connectedThread.write(s.getBytes());
                    Log.i(tag,"connected");
                    break;

                case MESSAGE_READ:
                    byte[] readBuf = (byte[])msg.obj;
                    String string = new String(readBuf);
                    Toast.makeText(getApplicationContext(), string, Toast.LENGTH_SHORT).show();
                    break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.read_arduino);
        init();
        if (btAdapter==null){
            Toast.makeText(getApplicationContext(), "No hay Bluetooth Detectado", Toast.LENGTH_SHORT).show();
            finish();
        }
        else {
            if(!btAdapter.isEnabled()) {
                turnOnBT();
            }

            getPairedDevices();
            startDiscovery();
        }


    }

    private void startDiscovery() {
        // TODO Auto-generated method stub
        btAdapter.cancelDiscovery();
        btAdapter.startDiscovery();
    }

    private void turnOnBT() {
        // TODO Auto-generated method stub
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(intent, 1);
    }

    private void getPairedDevices() {
        // TODO Auto-generated method stub
        devicesArray = btAdapter.getBondedDevices();
        if (devicesArray.size()>0){
            for (BluetoothDevice device:devicesArray){
                pairedDevices.add(device.getName());

            }
        }
    }

    private void init() {
        // TODO Auto-generated method stub
        Visible = (Button)findViewById(R.id.button2);
        listView= (ListView)findViewById(R.id.listView);
        listView.setOnItemClickListener(this);
        listAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,0);
        listView.setAdapter(listAdapter);
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        pairedDevices = new ArrayList<String>();
        filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        devices = new ArrayList<BluetoothDevice>();
        receiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                // TODO Auto-generated method stub

                String action = intent.getAction();

                if(BluetoothDevice.ACTION_FOUND.equals(action)){
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    devices.add(device);
                    String s = "";
                    for (int a = 0; a < pairedDevices.size(); a++){
                        if (device.getName().equals(pairedDevices.get(a))){
                            s = "(Emparejado)";
                            break;
                        }
                    }

                    listAdapter.add(device.getName()+" "+s+" "+"\n"+device.getAddress());
                }

                else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)){
                    //Ejecuta código

                }

                else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){


                }

                else if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)){
                    if (btAdapter.getState() == btAdapter.STATE_OFF){
                        turnOnBT();
                    }

                }
            }
        };

        registerReceiver(receiver, filter);
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        registerReceiver(receiver, filter);
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(receiver, filter);
        filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(receiver, filter);

    }

    public void visible(View view){
        Intent getVisible = new Intent(BluetoothAdapter.
                ACTION_REQUEST_DISCOVERABLE);
        startActivityForResult(getVisible, 0);

    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        unregisterReceiver(receiver);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED){
            Toast.makeText(getApplicationContext(), "Debe conectar Bluetooth para continuar", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main_, menu);
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        // TODO Auto-generated method stub

        if (btAdapter.isDiscovering()){
            btAdapter.cancelDiscovery();
        }
        if (listAdapter.getItem(arg2).contains("Emparejado")){
            BluetoothDevice selectDevice = devices.get(arg2);
            ConnectThread connect = new ConnectThread (selectDevice);
            connect.start();
            Log.i(tag,"in click listener");
        }
        else{
            Toast.makeText(getApplicationContext(), "El Dispositivo no está emparejado", Toast.LENGTH_SHORT).show();
        }
    }

    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            // Use a temporary object that is later assigned to mmSocket,
            // because mmSocket is final
            BluetoothSocket tmp = null;
            mmDevice = device;
            Log.i(tag,"construct");

            // Get a BluetoothSocket to connect with the given BluetoothDevice
            try {
                // MY_UUID is the app's UUID string, also used by the server code
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                Log.i(tag, "get socket failed");
            }
            mmSocket = tmp;
        }

        public void run() {
            // Cancel discovery because it will slow down the connection
            btAdapter.cancelDiscovery();
            Log.i(tag,"connect-run");
            try {
                // Connect the device through the socket. This will block
                // until it succeeds or throws an exception
                mmSocket.connect();
                Log.i(tag,"connect-succeded");
            } catch (IOException connectException) {
                Log.i(tag,"connect-failed");
                // Unable to connect; close the socket and get out
                try {
                    mmSocket.close();
                } catch (IOException closeException) { }
                return;
            }

            // Do work to manage the connection (in a separate thread)
            mHandler.obtainMessage(SUCCESS_CONNECT, mmSocket).sendToTarget();
        }


        /** Will cancel an in-progress connection, and close the socket */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }


    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer;  // buffer store for the stream
            int bytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {

                    buffer = new byte[1024];
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);
                    // Send the obtained bytes to the UI activity
                    mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
                            .sendToTarget();

                } catch (IOException e) {
                    break;
                }
            }
        }

        /* Call this from the main activity to send data to the remote device */
        public void write(byte[] bytes) {
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) { }
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }
}