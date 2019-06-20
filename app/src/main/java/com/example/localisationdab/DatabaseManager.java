package com.example.localisationdab;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class DatabaseManager extends SQLiteAssetHelper {


    public static final String DATABASE_NAME= "Localisation.db";
    public static final int DATABASE_VERSION=1;


    public DatabaseManager( Context context) {
        super(context, DATABASE_NAME, null, 1);
    }


}
