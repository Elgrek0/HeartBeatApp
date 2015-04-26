package database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import exceptions.ParameterLengthMismatch;

public class DatabaseHelper extends SQLiteOpenHelper {

    String dbname;
    boolean debug=true;
    public DatabaseHelper(Context context,String database_name)
    {
        super(context, database_name, null, 1);
        dbname=database_name;
    }


    protected void createtable(SQLiteDatabase database,String table_name,String[] param_names,Class<?>[] param_types) throws ParameterLengthMismatch {
        if(param_names.length!=param_types.length)
            throw(new ParameterLengthMismatch());
        String query="CREATE TABLE IF NOT EXISTS "+table_name+"(";
        for(int i=0;i<param_names.length;i++) {
            try {
                query =query+ param_names[i] + " " + TypeHandler.finddbname(param_types[i]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (i != param_names.length - 1)
                query=query+",";
        }
        query=query+");";
        if(debug)System.out.printf("query is :"+query);
        database.execSQL(query);
    }

    protected void redo(SQLiteDatabase database){
        database.execSQL("DROP TABLE Beats");
        database.execSQL("CREATE TABLE IF NOT EXISTS Beats(id INTEGER,frequency REAL,strength INTEGER,timems INTEGER,date INTEGER);");


    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println("database file created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        System.out.println("database file updated");
    }
}
