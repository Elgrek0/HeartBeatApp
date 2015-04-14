package database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.Calendar;

import DataAnalytics.FrequencyCalculator;
import Exceptions.NoDataException;

/**
 * Created by Paris on 14/4/2015.
 */
public class DatabaseAccess implements Database {

    private DatabaseHelper dbhelper;
    private SQLiteDatabase db;
    protected String Logcat = "database";
    protected int id;
    protected FrequencyCalculator fc ;
    protected final Object dbacceslock=new Object();
    public boolean debug=false;

    public DatabaseAccess(Context c){
        dbhelper=new DatabaseHelper(c);
        db=dbhelper.getWritableDatabase();
        id = getid();
        fc = new FrequencyCalculator(2);
        Log.d(Logcat, "db ready to use");
        reset();
    }
    public void reset(){
        dbhelper.redo(db);
    }


    private int getid() {
        try{
            Cursor c = db.rawQuery("SELECT * FROM Beats WHERE id>'0'", null);
            if (c.moveToLast()) {
                return (Integer.getInteger(c.getString(0)));
            } else
                return (0);}
        catch(NullPointerException e){
            return(0);
        }
    }

    private int getid(int id) throws Exception {
        Cursor c = db.rawQuery("SELECT * FROM Beats WHERE id='" + id + "'", null);
        if (c.moveToFirst()) {
            return (Integer.getInteger(c.getString(0)));
        }
        throw new Exception("item does not exist");
    }

    private double getfrequency(int id) throws NoDataException {
        Cursor c = db.rawQuery("SELECT * FROM Beats WHERE id='" + id + "'", null);
        if (c.moveToFirst()) {
            return (Double.parseDouble(c.getString(1)));
        }
        else
        throw new NoDataException();
    }

    @Override
    public void commitNewHBSample( float strength) {
    new ItemPlacer(this,strength).start();

    }

    @Override
    public float[] getData(int a) throws NoDataException {
        float[] datatoreturn = new float[a];
        synchronized(dbacceslock)

        {
            for (int i = 0; i <a; i++)
                datatoreturn[i] = (float) getfrequency(id-1 - i);
        }
        return (datatoreturn);

    }

    @Override
    public float[] getDataRange(int start, int end) {
        synchronized(dbacceslock)

        {
            return new float[0];
        }
    }

    @Override
    public float[] getDataBefore(int end) {
        synchronized(dbacceslock)

        {
            return new float[0];
        }
    }

    @Override
    public float[] getDataAfter(int start) {
        synchronized(dbacceslock)

        {
            return new float[0];
        }
    }

    @Override
    public float getSpecificData(int pos) throws Exception {
        synchronized(dbacceslock)

        {
            return ((float) getfrequency(pos));
        }
    }
}
