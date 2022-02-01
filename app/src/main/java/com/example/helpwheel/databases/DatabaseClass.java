package com.example.helpwheel.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.material.tabs.TabLayout;

public class DatabaseClass extends SQLiteOpenHelper {

    Context context;
    private static final String DatabaseName = "MyNotes";
    private static final int DatabaseVersion = 1;

    private static final String TableName = "mynotes";
    private static final String ColumnId = "id";
    private static final String ColumnTitle = "title";
    private static final String ColumnDescription = "description";


    public DatabaseClass(@Nullable Context context) {
        super(context, DatabaseName, null, DatabaseVersion);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = "CREATE TABLE " + TableName +
                " (" + ColumnId + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ColumnTitle + " TEXT, " +
                ColumnDescription + " TEXT);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TableName);
        onCreate(db);
    }


    public void addNotes(String title, String description) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(ColumnTitle, title);
        cv.put(ColumnDescription, description);
        long resultValue = db.insert(TableName, null, cv);
        if (resultValue == -1) {
            Toast.makeText(context, "Data Not Added", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Data Added Successfully", Toast.LENGTH_SHORT).show();
        }
    }

    public Cursor readAllData() {
        String query = "SELECT * FROM " + TableName;
        SQLiteDatabase database = this.getReadableDatabase();

        Cursor cursor = null;
        if (database != null) {
            cursor = database.rawQuery(query, null);
        }
        return cursor;
    }

    public void updateNotes(String title, String description, String id) {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(ColumnTitle, title);
        cv.put(ColumnDescription, description);

        long result = database.update(TableName, cv, "id=?", new String[]{id});
        if (result == -1)
        {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        } else{
            Toast.makeText(context, "Done", Toast.LENGTH_SHORT).show();
        }
    }


    public void deleteSingleItem(String id){
        SQLiteDatabase database = this.getWritableDatabase();

        long result = database.delete(TableName, "id=?",new String[]{id});
        if (result==-1){
            Toast.makeText(context, "Item Not Deleted", Toast.LENGTH_SHORT).show();
        }
    }

}