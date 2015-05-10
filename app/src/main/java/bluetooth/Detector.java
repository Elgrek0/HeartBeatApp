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

/**
 * Created by Paris on 20/4/2015.
 */
public class Detector {
    BluetoothAdapter ba= BluetoothAdapter.getDefaultAdapter();
    ArrayAdapter<String> BTArrayAdapter ;
    public void  connect(Activity a){
        Toast.makeText(a,"detector created",Toast.LENGTH_SHORT).show();
        ba.startDiscovery();
        IntentFilter filter =new IntentFilter(BluetoothDevice.ACTION_FOUND);
        BTArrayAdapter=new ArrayAdapter<String>(a,android.R.layout.simple_list_item_1);
        //if(ba.isDiscovering())
           // ba.cancelDiscovery();
       // if(!ba.isDiscovering()){
            Log.d("myapp","started discovery");
            ba.startDiscovery();
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
                Log.d("myapp","device found " + device.getName());
                BTArrayAdapter.notifyDataSetChanged();
                new ConnectThread(device,ba).start();
            }
        }
    };

}
