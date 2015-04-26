package database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.Calendar;

import dataAnalytics.FrequencyCalculator;
import exceptions.NoDataException;
import exceptions.NoSuchTypeException;
import exceptions.NotEnoughDataException;
import exceptions.ParameterLengthMismatch;

/**
 * Created by Paris on 14/4/2015.
 */
public class DatabaseAccess {

    private DatabaseHelper dbhelper;
    public SQLiteDatabase db;
    protected String Logcat = "database";
    protected int id;
    protected FrequencyCalculator fc;
    protected final Object dbacceslock = new Object();
    public boolean debug = true;

    public DatabaseAccess(Context c, String name) {
        //CREATE TABLE IF NOT EXISTS Beats(id INTEGER,frequency REAL,strength INTEGER,timems INTEGER,date INTEGER);
        dbhelper = new DatabaseHelper(c, name);
        db = dbhelper.getWritableDatabase();

        id = getlastid();
        if (debug) System.out.println("id is :" + id);
        fc = new FrequencyCalculator(2);
        Log.d(Logcat, "db ready to use");
        reset();
    }

    public void createtable(String table_name, String[] param_names, Class<?>[] param_types) throws ParameterLengthMismatch {
        dbhelper.createtable(db, table_name, param_names, param_types);
    }

    public void reset() {
        dbhelper.redo(db);
    }

    public int size() {
        return (id);
    }

    private int getlastid() {
        Cursor c = null;
        try {
            c = db.rawQuery("SELECT * FROM Beats WHERE id>'0'", null);
            if (c.moveToLast()) {
                String s = c.getString(0);
                c.close();
                return (Integer.getInteger(s));
            } else
                c.close();
            return (0);
        } catch (NullPointerException e) {
            return (0);
        }
    }

    private String get(String table_name, String var_name, Object variable, String toreturn) {
        Cursor c = null;
        try {
            c = db.rawQuery("SELECT * FROM " + table_name + " WHERE " + var_name + "=" + TypeHandler.convert(variable) + "", null);
            if (c.moveToLast()) {
                String s = c.getString(c.getColumnIndex(toreturn));
                c.close();
                if (debug) System.out.println(s);
                return (s);
            } else
                c.close();
            return ("");
        } catch (NullPointerException e) {
            return ("");
        }
    }

    private int getid(int id) throws Exception {
        return (Integer.parseInt(get("Beats", "id", id, "id")));
    }

    private double getfrequency(int id) throws NoDataException {
        return (Float.parseFloat(get("Beats", "id", id, "frequency")));
    }


    public void commitNewHBSample(float strength) {
        new ItemPlacer(this, strength).start();

    }


    public Object[] getData(int count, String table_name, Class type, String toreturn) throws NoDataException {
        if (id > 1){
            if (id > count+1) {
                Object[] datatoreturn = new Object[count];

                synchronized (dbacceslock)

                {
                    for (int i = 0; i < count; i++)
                        try {
                            datatoreturn[i] = TypeHandler.rconvert(type, get(table_name, "id", id - i - 1, toreturn));
                        } catch (NoSuchTypeException e) {
                            e.printStackTrace();
                        }
                }
                return (datatoreturn);
            } else {
                Object[] datatoreturn = new Object[id-1];

                synchronized (dbacceslock)

                {
                    for (int i = 0; i < id-1; i++)
                        try {
                            datatoreturn[i] = TypeHandler.rconvert(type, get(table_name, "id", id - i - 1, toreturn));
                        } catch (NoSuchTypeException e) {
                            e.printStackTrace();
                        }
                }
                return (datatoreturn);
            }
    }
        else
            throw(new NoDataException());
}


}
