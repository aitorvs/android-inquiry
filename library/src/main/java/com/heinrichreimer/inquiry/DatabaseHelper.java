package com.heinrichreimer.inquiry;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.heinrichreimer.inquiry.callbacks.UpgradeCallback;

class DatabaseHelper extends SQLiteOpenHelper {

    private final String table;
    private final UpgradeCallback mOnUpgradeCallback;

    public DatabaseHelper(
            Context context,
            String databaseName,
            @NonNull String table,
            @Nullable String columns,
            @IntRange(from = 1, to = Integer.MAX_VALUE) int version,
            @Nullable UpgradeCallback upgradeCallback) {

        super(context, databaseName, null, version);

        mOnUpgradeCallback = upgradeCallback;
        this.table = table;
        if (columns != null && (!databaseExists(context, databaseName) || !tableExists(table))) {
            String createStatement = String.format("CREATE TABLE IF NOT EXISTS %s (%s)", table, columns);
            getWritableDatabase().execSQL(createStatement);
        }
    }

    private boolean databaseExists(Context context, String name) {
        return context.getDatabasePath(name).exists();
    }

    private boolean tableExists(String name) {
        Cursor cursor = getReadableDatabase()
                .rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"+name+"'", null);
        if(cursor != null) {
            if(cursor.getCount() > 0) {
                // close the cursor
                cursor.close();
                // table does exist
                return true;
            }
            // make sure we close the cursor
            cursor.close();
        }
        return false;
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (mOnUpgradeCallback != null) {
            // OK, somebody will take care of this
            mOnUpgradeCallback.onUpgrade(db, oldVersion, newVersion);
        } else {
            // default behavior when no handler is register is to drop the database
            if (BuildConfig.DEBUG)
                Log.w(Inquiry.DEBUG_TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
            dropTable(db);
            onCreate(db);
        }
    }

    public final Cursor query(String[] projection, String selection,
            String[] selectionArgs, String sortOrder) {
        return getReadableDatabase().query(table, projection, selection, selectionArgs, null, null, sortOrder);
    }

    public final long insert(ContentValues values) {
        return getWritableDatabase().insert(table, null, values);
    }

    public final long insertWithOnConflict(ContentValues values, int conflictAlgorithm) {
        return getWritableDatabase().insertWithOnConflict(table, null, values, conflictAlgorithm);
    }

    public final long insertOrIgnore(ContentValues values) {
        return getWritableDatabase().insertWithOnConflict(table, null, values, SQLiteDatabase.CONFLICT_IGNORE);
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

    public final int updateWithOnConflict(ContentValues values, String selection, String[] selectionArgs, int conflictAlgorithm) {
        return getWritableDatabase().updateWithOnConflict(table, values, selection, selectionArgs, conflictAlgorithm);
    }

    public final void dropTable() {
        dropTable(getWritableDatabase());
    }

    private void dropTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + table);
    }
}