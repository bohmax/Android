package com.example.datasharing;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.CursorAdapter;
import android.widget.SimpleCursorAdapter;

public class MyDatabase {

    private static SQLiteDatabase database;
    public final static String DIP_TABLE="Dipartimento"; // name of table

    public final static String DIP_ID="_id"; // id value for employee
    public final static String DIP_NAME="name";  // name of employee

    public MyDatabase(Context context){
        Database dbHelper = Database.getInstance(context);
        database = dbHelper.getWritableDatabase();
    }

    public long createRecords(String name) throws SQLiteConstraintException {
        ContentValues values = new ContentValues();
        //values.put(DIP_ID, id);
        values.put(DIP_NAME, name);
        return database.insertOrThrow(DIP_TABLE, null, values);
    }

    public boolean delete(String key, String elem, SimpleCursorAdapter spc){
        boolean ris = false;
        String whereclause;
        String[] cols = new String[] {DIP_ID, DIP_NAME};
        if (key!=null) whereclause = DIP_ID+"="+key;
        else whereclause = DIP_NAME+"="+elem;
        ris = database.delete("Dipartimento", whereclause,null) > 0;
        if (ris)
            spc.swapCursor(database.query(true, DIP_TABLE,cols,null
                    , null, null, null, null, null));//refresha il cursore
        return ris;
    }

    public SimpleCursorAdapter selectRecords(Context context) {
        String[] cols = new String[] {DIP_ID, DIP_NAME};
        return new SimpleCursorAdapter(context, R.layout.linear,
                database.query(true, DIP_TABLE,cols,null
                , null, null, null, null, null), new String[]{"_id","name"},new int[]{R.id.number_entry,R.id.name_entry}, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
    }
}
