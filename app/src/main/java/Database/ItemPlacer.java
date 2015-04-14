package database;

import android.content.ContentValues;

import java.util.Calendar;

import Exceptions.NotEnoughDataException;

/**
 * Created by Paris on 14/4/2015.
 */
public class ItemPlacer extends Thread{

    private DatabaseAccess da;
    private float strength;
    private double frequency;
    private int id;
    Calendar c;
        public ItemPlacer(DatabaseAccess da,float strength) {
            this.da=da;
            this.strength=strength;
            try {
                frequency =da.fc.CalculateFrequency(strength);
            } catch (NotEnoughDataException e) {
                frequency=0;
            }
            this.id=da.id++;
            c = Calendar.getInstance();
            if(da.debug)  System.out.println("freq is :"+frequency);
        }
    public void run(){
        synchronized(da.dbacceslock)
        { ContentValues values = new ContentValues();
            values.put("id", id);
            values.put("frequency", frequency);
            values.put("strength", strength);
            values.put("timems", c.getTimeInMillis());
            values.put("date", 12122012);
            da.db.insert("Beats", null, values);

            }

       if(da.debug) System.out.println("item added to Database id:"+da.id);
    }
}
