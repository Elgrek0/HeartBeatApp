package bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;

import upatras.heartbeatapp.MainActivity;

/**
 * Created by Paris on 20/4/2015.
 */
public class Detector {
    BluetoothAdapter ba= BluetoothAdapter.getDefaultAdapter();
    ArrayAdapter<String> BTArrayAdapter ;
    Activity a;
    public void  connect(Activity a){
        this.a=a;
        Toast.makeText(a,"detector created",Toast.LENGTH_SHORT).show();
        ba.startDiscovery();
        IntentFilter filter =new IntentFilter(BluetoothDevice.ACTION_FOUND);
        BTArrayAdapter=new ArrayAdapter<String>(a,android.R.layout.simple_list_item_1);
        //if(ba.isDiscovering())
           // ba.cancelDiscovery();
       // if(!ba.isDiscovering()){
            Log.d("myapp","started discovery");
            ba.startDiscovery();
        try {
            a.unregisterReceiver(bReceiver);
        }
        catch(Exception e){}
            a.registerReceiver(bReceiver,new IntentFilter(BluetoothDevice.ACTION_FOUND));
        //}

    }
    final BroadcastReceiver bReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
            Log.d("myapp","received smthing");
            if(BluetoothDevice.ACTION_FOUND.equals(action)){
                BluetoothDevice device=intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                BTArrayAdapter.add(device.getName()+"\n"+device.getAddress());
                if(a.getClass()==MainActivity.class)
                    ((MainActivity)a).showMyToast(device.getName());
                Log.d("myapp","device found " + device.getName());
                BTArrayAdapter.notifyDataSetChanged();
                new ConnectThread(device,ba,a).start();
            }
        }
    };

}
