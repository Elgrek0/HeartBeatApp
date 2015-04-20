package database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {


    public DatabaseHelper(Context context) {
        super(context, "Heartbeat", null, 1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL("CREATE TABLE IF NOT EXISTS Beats(id INTEGER,frequency REAL,strength INTEGER,timems INTEGER,date INTEGER);");

    }
    protected void redo(SQLiteDatabase database){
        database.execSQL("DROP TABLE Beats");
        database.execSQL("CREATE TABLE IF NOT EXISTS Beats(id INTEGER,frequency REAL,strength INTEGER,timems INTEGER,date INTEGER);");


    }
}
