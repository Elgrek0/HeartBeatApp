package database;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.Calendar;
import java.util.LinkedList;

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
    protected int id=0;
    protected FrequencyCalculator fc;
    protected final Object dbacceslock = new Object();
    public boolean debug = true;

    public DatabaseAccess(Context c, String name) {
        //CREATE TABLE IF NOT EXISTS Beats(id INTEGER,frequency REAL,strength INTEGER,timems INTEGER,date INTEGER);
        dbhelper = new DatabaseHelper(c, name);
        db = dbhelper.getWritableDatabase();
        id = getlastid();
        reset();
        Log.d(Logcat, "db ready to use");

    }

    public void createtable(String table_name, String[] param_names, Class<?>[] param_types) throws ParameterLengthMismatch {
        dbhelper.createtable(db, table_name, param_names, param_types);

        //id = getlastid();
        //if (debug) System.out.println("id is :" + id);
        fc = new FrequencyCalculator(2);
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
            c = db.rawQuery("SELECT id FROM Beats", null);
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

    private String get(String table_name, String comp_var_name, Object variable, String toreturn_var_name) {
        Cursor c = null;
        try {
            c = db.rawQuery("SELECT "+ toreturn_var_name +" FROM " + table_name + " WHERE " + comp_var_name + "=" + TypeHandler.convert(variable) + "", null);
            if (c.moveToFirst()) {
                String s = c.getString(0);
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

    private Object[] getbetween(String table_name, String comp_var_name, Object above,Object below,String toreturn_var_name,Class typetoreturn) throws NoDataException {
        Cursor c = null;
        LinkedList<Object> data=new LinkedList<>();
        Object [] toreturn;
        try {
            System.out.println("SELECT "+ toreturn_var_name +" FROM " + table_name +
                    " WHERE " + comp_var_name + ">" + TypeHandler.convert(above)+
                    " AND "+ comp_var_name + "<=" + TypeHandler.convert(below)+ "");

            c = db.rawQuery("SELECT "+ toreturn_var_name +" FROM " + table_name +
                    " WHERE " + comp_var_name + ">" + TypeHandler.convert(above)+
                    " AND "+ comp_var_name + "<=" + TypeHandler.convert(below)+ "", null);
            while(c.moveToNext()){
                String s = c.getString(0);
                try {
                    data.add(TypeHandler.rconvert(typetoreturn,s));
                } catch (NoSuchTypeException e) {
                    e.printStackTrace();
                }
                if (debug) System.out.println(s);
            }

            toreturn=new Float[data.size()];
            for(int i=0;i<data.size();i++){
                toreturn[i]=data.get(i);
            }
            c.close();
            return(toreturn);
        } catch (NullPointerException e) {

            throw new NoDataException();
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


    public Object[] getData(int count, String table_name,String tocompare, String toreturn,Class toreturn_type) throws NoDataException {



                synchronized (dbacceslock)

                {
                    return(getbetween(table_name,tocompare,id-count,id,toreturn,toreturn_type));
                }

    }
    public Object getLast( String table_name, String tocompare, String toreturn,Class toreturn_type) throws NoDataException {



        synchronized (dbacceslock)

        {
            return(getbetween(table_name,tocompare,id-1,id,toreturn,toreturn_type));
        }

    }

}
