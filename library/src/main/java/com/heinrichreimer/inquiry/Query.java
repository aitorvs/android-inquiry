package com.heinrichreimer.inquiry;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.IntDef;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.heinrichreimer.inquiry.callbacks.GetCallback;
import com.heinrichreimer.inquiry.callbacks.RunCallback;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Array;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public final class Query<RowType, RunReturn> {

    @IntDef({SELECT, INSERT, REPLACE, UPDATE, DELETE})
    @Retention(RetentionPolicy.SOURCE)
    @interface QueryType {
    }

    final static int SELECT = 1;
    final static int INSERT = 2;
    final static int REPLACE = 3;
    final static int UPDATE = 4;
    final static int DELETE = 5;

    @NonNull
    private final Inquiry inquiry;
    @QueryType
    private final int queryType;
    @NonNull
    private final Class<RowType> rowType;
    @NonNull
    private DatabaseHelper database;

    private String[] onlyUpdate;
    private final List<String> selection = new LinkedList<>();
    private final List<Object> selectionArgs = new LinkedList<>();
    private final List<String> sortOrder = new LinkedList<>();
    private int limit;
    private int offset;
    private RowType[] values;

    Query(@NonNull Inquiry inquiry, @QueryType int type, @NonNull Class<RowType> rowType) {
        this.inquiry = inquiry;
        queryType = type;
        this.rowType = rowType;
        if (inquiry.databaseName == null)
            throw new IllegalStateException("Inquiry was not initialized with a database name, it can only use content providers in this configuration.");
        database = new DatabaseHelper(inquiry.context, inquiry.databaseName,
                DatabaseSchemaParser.getTableName(rowType),
                DatabaseSchemaParser.getClassSchema(inquiry.getConverters(), rowType), inquiry.databaseVersion);
    }

    @SuppressLint("DefaultLocale")
    public Query<RowType, RunReturn> atPosition(@IntRange(from = 0, to = Integer.MAX_VALUE) int position) {
        Cursor cursor = database.query(null, getSelection(), getSelectionArgs(), null);
        if (cursor != null) {
            if (position < 0 || position >= cursor.getCount()) {
                cursor.close();
                throw new IndexOutOfBoundsException(String.format("Position %d is out of bounds for cursor of size %d.",
                        position, cursor.getCount()));
            }
            if (!cursor.moveToPosition(position)) {
                cursor.close();
                throw new IllegalStateException(String.format("Unable to move to position %d in cursor of size %d.",
                        position, cursor.getCount()));
            }
            final int idIndex = cursor.getColumnIndex("_id");
            if (idIndex < 0) {
                cursor.close();
                throw new IllegalStateException("Didn't find a column named _id in this Cursor.");
            }
            final int idValue = cursor.getInt(idIndex);
            selection.clear();
            selectionArgs.clear();
            where("_id = ?", idValue);
            cursor.close();
        }
        return this;
    }

    public Query<RowType, RunReturn> where(@NonNull String selection, @Nullable Object... selectionArgs) {
        int args = Utils.countOccurrences(selection, '?');
        if ((selectionArgs == null && args != 0) || (selectionArgs != null && selectionArgs.length != args))
            throw new IllegalArgumentException("There must be exactly as many selection args as '?' characters in the selection string.");
        this.selection.add(selection);
        if (selectionArgs != null) {
            Collections.addAll(this.selectionArgs, selectionArgs);
        }
        return this;
    }

    public Query<RowType, RunReturn> whereIn(@NonNull String column, @Nullable Object... selectionArgs) {
        if (selectionArgs == null)
            return this;

        StringBuilder in = new StringBuilder();
        in.append(column);
        in.append(" IN (");
        boolean first = true;
        for (Object ignored : selectionArgs) {
            if (first)
                first = false;
            else
                in.append(",");
            in.append("?");
        }
        in.append(")");
        this.selection.add(in.toString());
        Collections.addAll(this.selectionArgs, selectionArgs);
        return this;
    }

    public Query<RowType, RunReturn> clearWhere() {
        selection.clear();
        selectionArgs.clear();
        return this;
    }

    public Query<RowType, RunReturn> sort(@NonNull String... columns) {
        Collections.addAll(this.sortOrder, columns);
        return this;
    }

    public Query<RowType, RunReturn> clearSort() {
        sortOrder.clear();
        return this;
    }

    public Query<RowType, RunReturn> limit(int limit) {
        this.limit = limit;
        return this;
    }

    public Query<RowType, RunReturn> clearLimit() {
        this.limit = -1;
        return this;
    }

    public Query<RowType, RunReturn> offset(int offset) {
        this.offset = offset;
        return this;
    }

    public Query<RowType, RunReturn> clearOffset(int offset) {
        this.offset = offset;
        return this;
    }

    @SuppressWarnings("unchecked")
    public final Query<RowType, RunReturn> value(@NonNull Object value) {
        values = (RowType[]) Array.newInstance(rowType, 1);
        Array.set(values, 0, value);
        return this;
    }

    @SafeVarargs
    public final Query<RowType, RunReturn> values(@NonNull RowType... values) {
        this.values = values;
        return this;
    }

    public Query<RowType, RunReturn> onlyUpdate(@NonNull String... columns) {
        onlyUpdate = columns;
        return this;
    }

    @SuppressWarnings("unchecked")
    @Nullable
    private RowType[] getInternal() {
        if (inquiry.context == null)
            return null;
        final String[] projection = DatabaseSchemaParser.generateProjection(rowType);
        if (queryType == SELECT) {
            StringBuilder sort = new StringBuilder(getSortOrder());
            if (limit > -1) sort.append(String.format(Locale.getDefault(), " LIMIT %d", limit));
            if (offset > -1) sort.append(String.format(Locale.getDefault(), " OFFSET %d", offset));
            Cursor cursor = database.query(projection, getSelection(), getSelectionArgs(), sort.toString());
            if (cursor != null) {
                RowType[] results = null;
                if (cursor.getCount() > 0) {
                    results = (RowType[]) Array.newInstance(rowType, cursor.getCount());
                    int index = 0;
                    while (cursor.moveToNext()) {
                        results[index] = DatabaseAdapter.load(inquiry, inquiry.getConverters(), cursor, rowType);
                        index++;
                    }
                }
                cursor.close();
                database.close();
                return results;
            }
        }
        else {
            throw new UnsupportedOperationException("one() and all() can only be used with Inquiry.select().");
        }
        database.close();
        return null;
    }

    @Nullable
    public RowType one() {
        int tempLimit = limit;
        limit = 1;
        RowType[] results = getInternal();
        limit = tempLimit;
        if (results == null || results.length == 0)
            return null;
        return results[0];
    }

    @Nullable
    public RowType[] all() {
        return getInternal();
    }

    public void all(@NonNull final GetCallback<RowType> callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final RowType[] results = all();
                if (inquiry.handler == null) return;
                inquiry.handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.result(results);
                    }
                });
            }
        }).start();
    }

    @SuppressWarnings("unchecked")
    public RunReturn run() {
        if (queryType != DELETE && (values == null || values.length == 0))
            throw new IllegalStateException("No values were provided for this query to run.");
        else if (inquiry.context == null) {
            try {
                return (RunReturn) (Integer) 0;
            } catch (Throwable t) {
                return (RunReturn) (Long) 0L;
            }
        }
        switch (queryType) {
            case INSERT:
                Long[] insertedIds = new Long[values.length];
                for (int i = 0; i < values.length; i++) {
                    ContentValues contentValues = DatabaseAdapter.save(inquiry, inquiry.getConverters(), values[i], null);
                    insertedIds[i] = database.insert(contentValues);
                }
                database.close();
                return (RunReturn) insertedIds;
            case REPLACE:
                Long[] replacedIds = new Long[values.length];
                for (int i = 0; i < values.length; i++) {
                    ContentValues contentValues = DatabaseAdapter.save(inquiry, inquiry.getConverters(), values[i], null);
                    replacedIds[i] = database.replace(contentValues);
                }
                database.close();
                return (RunReturn) replacedIds;
            case UPDATE: {
                ContentValues contentValues = DatabaseAdapter.save(inquiry, inquiry.getConverters(), values[values.length - 1], onlyUpdate);
                RunReturn value = (RunReturn) (Integer) database.update(contentValues, getSelection(), getSelectionArgs());
                database.close();
                return value;
            }
            case DELETE: {
                RunReturn value = (RunReturn) (Integer) database.delete(getSelection(), getSelectionArgs());
                database.close();
                return value;
            }
            case SELECT:
            default:
                throw new UnsupportedOperationException("run() can only be used with Inquiry.insert(), Inquiry.update() or Inquiry.delete().");
        }
    }

    public void run(@NonNull final RunCallback<RunReturn> callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final RunReturn changed = Query.this.run();
                if (inquiry.handler == null) return;
                inquiry.handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.result(changed);
                    }
                });
            }
        }).start();
    }

    private String getSelection() {
        if (selection.isEmpty()) return null;
        boolean first = true;
        StringBuilder sb = new StringBuilder();
        for (String sel : selection) {
            if (first) first = false;
            else sb.append(" AND ");
            sb.append(sel);
        }
        return sb.toString();
    }

    private String[] getSelectionArgs() {
        if (selectionArgs.isEmpty()) return null;
        String[] args = new String[selectionArgs.size()];
        for (int i = 0; i < selectionArgs.size(); i++) {
            args[i] = (selectionArgs.get(i) + "");
        }
        return args;
    }

    private String getSortOrder() {
        if (sortOrder.isEmpty()) return null;
        boolean first = true;
        StringBuilder sb = new StringBuilder();
        for (String sort : sortOrder) {
            if (first) first = false;
            else sb.append(", ");
            sb.append(sort);
        }
        return sb.toString();
    }
}