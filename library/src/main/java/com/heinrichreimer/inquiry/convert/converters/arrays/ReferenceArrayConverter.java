package com.heinrichreimer.inquiry.convert.converters.arrays;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.heinrichreimer.inquiry.ContentValue;
import com.heinrichreimer.inquiry.Inquiry;
import com.heinrichreimer.inquiry.Query;
import com.heinrichreimer.inquiry.annotations.Table;
import com.heinrichreimer.inquiry.convert.Converter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

public class ReferenceArrayConverter extends Converter<Object[], String> {
    @Nullable
    @Override
    public Object[] convert(@NonNull Inquiry inquiry, @NonNull ContentValue<String> value, @NonNull Class<? extends Object[]> fieldType) throws IOException {
        if (value.getContent() == null) return null;

        String[] ids = value.getContent().split(",");

        Map<String, Integer> order = new TreeMap<>();
        StringBuilder where = new StringBuilder();
        boolean first = true;
        for (int i = 0; i < ids.length; i++) {
            order.put(ids[i], i);
            if (first)
                first = false;
            else
                where.append(" OR ");
            where.append(Inquiry.ID);
            where.append(" = ?");
        }

        Object[] unordered = inquiry
                .select(fieldType.getComponentType())
                .where(where.toString(), ids)
                .all();

        Object[] ordered = new Object[unordered.length];
        int i = 0;
        for (Map.Entry<String, Integer> entry : order.entrySet()) {
            ordered[entry.getValue()] = unordered[i];
            i++;
        }

        return Arrays.copyOf(ordered, ordered.length, fieldType);
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public ContentValue<String> convert(@NonNull Inquiry inquiry, @Nullable Object[] value) throws IOException {
        if (value == null || value.length == 0)
            return ContentValue.valueOf((String) null);

        Class componentType = value.getClass().getComponentType();
        Query<Object, Long[]> query = inquiry.insert(componentType);
        Long[] ids = query.values(value)
                .run();
        StringBuilder idString = new StringBuilder();
        boolean first = true;
        for (Long id : ids) {
            if (first)
                first = false;
            else
                idString.append(",");
            if (id >= 0)
                idString.append(id.toString());
        }
        return ContentValue.valueOf(idString.length() == 0 ? null : idString.toString());
    }

    @NonNull
    @Override
    public Class<Object[]> getInputType() {
        return Object[].class;
    }

    @NonNull
    @Override
    public Class<String> getOutputType() {
        return String.class;
    }

    @Override
    public boolean isSupported(Class<?> fieldType) {
        return super.isSupported(fieldType) && isReferenceArray(fieldType);
    }

    private static boolean isReferenceArray(Class<?> type) {
        return type != null && type.isArray() && type.getComponentType() != null &&
                type.getComponentType().getAnnotation(Table.class) != null;
    }
}
