package com.heinrichreimer.inquiry;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

class DatabaseHelper extends SQLiteOpenHelper {

    private final String table;

    public DatabaseHelper(Context context, String databaseName, @NonNull String table, @Nullable String columns, int version) {
        super(context, databaseName, null, version);
        this.table = table;
        if (columns != null) {
            getWritableDatabase(); //This will invoke onUpgrade if necessary
            String createStatement = String.format("CREATE TABLE IF NOT EXISTS %s (%s)", table, columns);
            getWritableDatabase().execSQL(createStatement);
        }
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (BuildConfig.DEBUG)
            Log.w(Inquiry.DEBUG_TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
        dropTable(db);
        onCreate(db);
    }

    public final Cursor query(String[] projection, String selection,
                              String[] selectionArgs, String sortOrder) {
        return getReadableDatabase().query(table, projection, selection, selectionArgs, null, null, sortOrder);
    }

    public final long insert(ContentValues values) {
        return getWritableDatabase().insert(table, null, values);
    }

    public final long replace(ContentValues values) {
        return getWritableDatabase().replace(table, null, values);
    }

    public final int delete(String selection, String[] selectionArgs) {
        if (selection == null) selection = "1";
        return getWritableDatabase().delete(table, selection, selectionArgs);
    }

    public final int update(ContentValues values, String selection, String[] selectionArgs) {
        return getWritableDatabase().update(table, values, selection, selectionArgs);
    }

    public final void dropTable() {
        dropTable(getWritableDatabase());
    }

    private void dropTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + table);
    }
}