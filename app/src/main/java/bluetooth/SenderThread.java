package bluetooth;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import upatras.heartbeatapp.MainActivity;

public class SenderThread  extends Thread {

    private final BluetoothSocket mmSocket;
    private final OutputStream mmOutStream;
    final int MESSAGE_READ = 9999;
    public static MainActivity mainactivity;
    Handler mHandler;

    public SenderThread(BluetoothSocket socket, Handler mHandler) {
        mmSocket = socket;
        OutputStream tmpOut = null;
        this.mHandler = mHandler;
        // Get the input and output streams, using temp objects because
        // member streams are final
        try {
            tmpOut = socket.getOutputStream();

        } catch (IOException e) {
        }
        mmOutStream = tmpOut;
    }

    public void run() {
        byte[] buffer = new byte[1024];  // buffer store for the stream
        int bytes; // bytes returned from read()

        // Keep listening to the InputStream until an exception occurs
        while (true) {


                //bytes = mmOutStream.write(buffer);
                // Send the obtained bytes to the UI activity

               ;
            }

    }
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) {
        }
    }



}

