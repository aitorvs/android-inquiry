package com.heinrichreimer.inquiry;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.heinrichreimer.inquiry.convert.Converter;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public final class Inquiry {

    public static final String ID = "_id";

    static final String DEBUG_TAG = "Inquiry";

    private static Inquiry inquiry;

    Context context;
    Handler handler;
    @Nullable String databaseName;
    int databaseVersion = 1;
    private final List<Converter> converters = new LinkedList<>();

    public Inquiry(@NonNull Context context, @Nullable String databaseName,
            @IntRange(from = 1, to = Integer.MAX_VALUE) int databaseVersion) {
        handler = new Handler();
        this.context = context;
        this.databaseName = databaseName;
        this.databaseVersion = databaseVersion;
    }

    @NonNull
    public static Inquiry init(@NonNull Context context, @Nullable String databaseName,
                               @IntRange(from = 1, to = Integer.MAX_VALUE) int databaseVersion) {
        inquiry = new Inquiry(context, databaseName, databaseVersion);
        return inquiry;
    }

    public void destroy() {
        context = null;
        handler = null;
        databaseName = null;
        databaseVersion = 0;
    }

    public boolean isAlive() {
        return context != null && handler != null && databaseName != null && databaseVersion > 0;
    }

    public static void deinit() {
        if (inquiry != null) {
            inquiry.destroy();
            inquiry = null;
        }
    }

    @NonNull
    public static Inquiry get() {
        if (inquiry == null)
            throw new IllegalStateException("Inquiry not initialized, or has been garbage collected.");
        return inquiry;
    }

    public void addConverter(Converter converter) {
        converters.add(converter);
    }

    List<Converter> getConverters() {
        List<Converter> converters = new LinkedList<>();
        converters.addAll(ConverterFactory.getHighPriorityConverters());
        converters.addAll(this.converters);
        converters.addAll(ConverterFactory.getLowPriorityConverters());
        return Collections.unmodifiableList(converters);
    }

    public void dropTable(@NonNull Class<?> type) {
        if (!DatabaseSchemaParser.isTable(type))
            throw new UnsupportedOperationException("Unable to drop table for type " + type.getSimpleName());

        String table = DatabaseSchemaParser.getTableName(type);
        DatabaseHelper database = new DatabaseHelper(context, databaseName, table, null, databaseVersion);
        database.dropTable();
        database.close();
    }

    @NonNull
    public <RowType> Query<RowType, Long[]> select(@NonNull Class<RowType> rowType) {
        return new Query<>(this, Query.SELECT, rowType);
    }

    @NonNull
    public <RowType> Query<RowType, Long[]> insert(@NonNull Class<RowType> rowType) {
        return new Query<>(this, Query.INSERT, rowType);
    }

    @NonNull
    public <RowType> Query<RowType, Long[]> insertOrIgnore(@NonNull Class<RowType> rowType) {
        return new Query<>(this, Query.INSERT_OR_IGNORE, rowType);
    }

    @NonNull
    public <RowType> Query<RowType, Long[]> replace(@NonNull Class<RowType> rowType) {
        return new Query<>(this, Query.REPLACE, rowType);
    }

    @NonNull
    public <RowType> Query<RowType, Integer> update(@NonNull Class<RowType> rowType) {
        return new Query<>(this, Query.UPDATE, rowType);
    }

    @NonNull
    public <RowType> Query<RowType, Integer> delete(@NonNull Class<RowType> rowType) {
        return new Query<>(this, Query.DELETE, rowType);
    }
}