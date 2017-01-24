package com.example.valentin.nexusinventory;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

/**
 * Created by Valentin on 19/01/2017.
 */

public class SQLiteHelper extends SQLiteOpenHelper
{

    public SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, name, factory, version);
    }

    public void queryData(String sql)
    {
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
    }

    public Integer delete(String id)
    {
        SQLiteDatabase database = getWritableDatabase();
        return database.delete("ITEM","Id = ?",new String[] {id});
    }

    public Integer update(String id, ContentValues contentValues)
    {
        SQLiteDatabase database = getWritableDatabase();
        return database.update("ITEM",contentValues,"Id = ?",new String[] {id});

    }

    public void insertData(String brand, String model, String description, String price, String stock, byte[] image)
    {
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO ITEM VALUES (NULL, ?, ?, ?, ?, ?, ?)";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1,brand);
        statement.bindString(2,model);
        statement.bindString(3,description);
        statement.bindString(4,price);
        statement.bindString(5,stock);
        statement.bindBlob(6,image);

        statement.executeInsert();

    }

    public Cursor getData(String sql)
    {
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql, null);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }
}
