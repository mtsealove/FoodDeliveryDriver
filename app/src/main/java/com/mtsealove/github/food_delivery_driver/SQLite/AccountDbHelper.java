package com.mtsealove.github.food_delivery_driver.SQLite;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import androidx.annotation.RequiresApi;

public class AccountDbHelper extends SQLiteOpenHelper {
    public static String table="Account";
    public AccountDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public AccountDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public AccountDbHelper(Context context, String name, int version, SQLiteDatabase.OpenParams openParams) {
        super(context, name, version, openParams);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query="create table "+table+" (" +
                "ID varchar(45),"+
                "Password varchar(100))";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void InsertAccount(SQLiteDatabase db, String ID, String pw) {
        db.beginTransaction();
        try {
            String removeQuery="delete from "+table;
            String insertQuery="insert into "+table+" values('"+ID+"', '"+pw+"')";
            db.execSQL(removeQuery);
            db.execSQL(insertQuery);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public void RemoveAccount(SQLiteDatabase db) {
        String query="delete from "+table;
        db.beginTransaction();
        try{
            db.execSQL(query);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }
}
