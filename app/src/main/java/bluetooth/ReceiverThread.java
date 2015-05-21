package bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import database.DatabaseAccess;
import upatras.heartbeatapp.HeartBeatSpectrum;
import upatras.heartbeatapp.HeartBeatSpectrumActivity;
import upatras.heartbeatapp.MainActivity;

public class ReceiverThread  extends Thread {

    private final BluetoothSocket mmSocket;
    private final InputStream mmInStream;
    final int MESSAGE_READ = 9999;
    public static MainActivity mainactivity;
    Handler mHandler;
    Activity a;
    public ReceiverThread(BluetoothSocket socket,Activity a) {
        mmSocket = socket;
        this.a=a;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;
        this.mHandler = mHandler;
        // Get the input and output streams, using temp objects because
        // member streams are final
        try {
            tmpIn = socket.getInputStream();

        } catch (IOException e) {
        }
        mmInStream = tmpIn;
    }

    public void run() {
        byte[] buffer = new byte[1024];  // buffer store for the stream
        int bytes; // bytes returned from read()
        String s="";

        // Keep listening to the InputStream until an exception occurs
        Looper.prepare();

        while (true) {
            try {
                s = "";
                while(mmInStream.available()>0) {

                    bytes = mmInStream.read(buffer);
                    for (int i = 0; i < bytes; i++) {
                        s = s + (char) buffer[i];
                    }
                }
                final String sf=s;
                if(!s.equals("")){
                    HeartBeatSpectrum.db.commitNewHBSample(1);

                        (a).runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(a, sf, Toast.LENGTH_SHORT).show();
                        }
                    });
                Log.d(this.getClass().getSimpleName(), "received "+s);}
            } catch (IOException e) {
                break;
            }
        }
    }
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) {
        }
    }



}

