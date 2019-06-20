package com.example.localisationdab;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import static android.support.constraint.Constraints.TAG;

public class DatabaseAccess {

    private SQLiteDatabase database;
    private SQLiteOpenHelper openHelper;
    private static DatabaseAccess instance;


    private DatabaseAccess(Context context){
        this.openHelper=new DatabaseManager(context);

    }

    public static DatabaseAccess getInstance(Context context){
        if(instance==null){
          instance =new DatabaseAccess(context);
        }
        return instance;
    }

    public void open(){
        this.database=openHelper.getWritableDatabase();


    }
    public void close(){
        if (database!= null){
            this.database.close();
        }
    }

    public Cursor  getAdmin(String email ,String pass ){

        Cursor cursor = database.rawQuery("SELECT * FROM admin where email =? and pass =?", new String[] {email,pass});


        return  cursor;
    }



    public List<String> getInsData(){

        List<String> list=new ArrayList<>();

        Cursor cursor = database.rawQuery("select DISTINCT nom_institut from dab", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }


    public List<Dab> getInsDab(String insName) {
        List<Dab> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM dab where nom_institut='"+insName+"'" , null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            Dab dab=new Dab(cursor.getInt(0),cursor.getDouble(1),cursor.getDouble(2),cursor.getString(3),cursor.getString(4)
                    ,cursor.getString(5));
            list.add(dab);
            cursor.moveToNext();

        }
        cursor.close();
        return list;
    }

    public List<Dab> getDab() {
        List<Dab> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM dab" ,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            Dab dab=new Dab(cursor.getInt(0),cursor.getDouble(1),cursor.getDouble(2),cursor.getString(3),cursor.getString(4)
                             ,cursor.getString(5));
            list.add(dab);
            cursor.moveToNext();

        }
        cursor.close();
        return list;
    }


    public Cursor getAllLocations(){

        Cursor cursor = database.rawQuery("Select * from dab", null);
      return cursor;

    }

    public void insertDAB(double lat,double lng ,String adrs,String etat,String instName){

          String sql="insert into dab (latitude,longitude,adresse,etat ,nom_institut) values ("+lat+","+lng+",'"+adrs+"','"+etat+"','"+instName+"')";

           database.execSQL(sql);

           Log.d(TAG, "insertDAB: insertion effectuer");
    }


    public void updateDAB(long id ,double lat,double lng ,String adrs,String etat,String instName){

        String sql="update  dab set latitude= "+lat+",longitude="+lng+", adresse='"+adrs+"'," +
                " etat='"+etat+"',nom_institut='"+instName+"' where _id="+id;
        database.execSQL(sql);
        Log.d(TAG, "updateDAB: bien update");

    }
    public void deleteDAB(long id){
        String sql="delete from dab where _id="+id;
        database.execSQL(sql);
    }

    public Cursor getItemByID(long id){

        Log.d(TAG, "getItemByID: called");
        String sql="select * from dab where _id="+id;
        Cursor data=database.rawQuery(sql,null);
        return data;

    }
}



