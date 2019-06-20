package com.example.localisationdab;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

public class LocationsContentProvider extends ContentProvider {

    public static final String PROVIDER_NAME = "com.mapv2.demo.locations";

    /** A uri to do operations on locations table. A content provider is identified by its uri */
    public static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/locations" );

    /** Constant to identify the requested operation */
    private static final int LOCATIONS = 1;

    private static final UriMatcher uriMatcher ;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "locations", LOCATIONS);
    }

    /** This content provider does the database operations by this object */
    DatabaseAccess db;

    /** A callback method which is invoked when the content provider is starting up */
    @Override
    public boolean onCreate() {
      db.getInstance(this.getContext());
        return true;
    }



    @Override
    public Cursor query(Uri uri, String[] projection, String selection,  String[] selectionArgs,  String sortOrder) {

        if (uriMatcher.match(uri) == LOCATIONS) {
            return db.getAllLocations();
        }
        return null;
    }


    @Override
    public String getType( Uri uri) {
        return null;
    }


    @Override
    public Uri insert( Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete( Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update( Uri uri,  ContentValues values,  String selection,  String[] selectionArgs) {
        return 0;
    }
}
