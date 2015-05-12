package bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;


class ConnectThread extends Thread {
    private final BluetoothSocket mmSocket;
    private final BluetoothDevice mmDevice;
    private BluetoothAdapter mBluetoothAdapter;
    public HelloWorldThread rbt ;
    public ReceiverThread rct ;
    Activity a;
    public ConnectThread(BluetoothDevice device, BluetoothAdapter ba,Activity a) {
        // Use a temporary object that is later assigned to mmSocket,
        // because mmSocket is final
        BluetoothSocket tmp = null;
        this.a=a;
        mmDevice = device;
        mBluetoothAdapter = ba;

        // Get a BluetoothSocket to connect with the given BluetoothDevice
        try {
            // MY_UUID is the app's UUID string, also used by the server code

            tmp = device.createRfcommSocketToServiceRecord(new UUID(0x27012f0c68af4fbfl,0x8dbe6bbaf7aa432al));
        } catch (IOException e) { }
        mmSocket = tmp;
    }

    public void run() {
        // Cancel discovery because it will slow down the connection
        //mBluetoothAdapter.cancelDiscovery();

        try {
            // Connect the device through the socket. This will block
            // until it succeeds or throws an exception
            mmSocket.connect();
            manageConnectedSocket(mmSocket);
        } catch (IOException connectException) {
            // Unable to connect; close the socket and get out
            try {
                mmSocket.close();
            } catch (IOException closeException) { }
            return;
        }
        Log.d(this.getClass().getSimpleName(),"socket connected");
        // Do work to manage the connection (in a separate thread)

    }


    void manageConnectedSocket(BluetoothSocket mmSocket){
        rbt=new HelloWorldThread(mmSocket);
        rbt.start();
        rct=new ReceiverThread(mmSocket,a);
        rct.start();
    }

    /** Will cancel an in-progress connection, and close the socket */
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) { }
    }
}