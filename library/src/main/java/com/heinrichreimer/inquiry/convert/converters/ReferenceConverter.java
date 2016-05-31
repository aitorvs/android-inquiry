package com.heinrichreimer.inquiry.convert.converters;

import android.support.annotation.NonNull;

import com.heinrichreimer.inquiry.ContentValue;
import com.heinrichreimer.inquiry.Inquiry;
import com.heinrichreimer.inquiry.annotations.Table;
import com.heinrichreimer.inquiry.convert.Converter;

import java.io.IOException;

public class ReferenceConverter extends Converter<Object> {
    @Override
    public Object convert(@NonNull Inquiry inquiry, @NonNull ContentValue value, @NonNull Class<?> fieldType) throws IOException {
        long id = (long) value.getContent();
        return inquiry
                .select(fieldType)
                .where("_id = ?", id)
                .one();
    }

    @NonNull
    @Override
    public ContentValue convert(@NonNull Inquiry inquiry, @NonNull Object value) throws IOException {
        long id = -1;
        //TODO look up if object already exists first
        Long[] ids = inquiry
                .insert(value.getClass())
                .value(value)
                .run();
        if (ids != null && ids.length > 0)
            id = ids[0];
        if (id >= 0) {
            return ContentValue.valueOf(id);
        }
        return ContentValue.valueOfNull();
    }

    @NonNull
    @Override
    public Class<?> getInputType() {
        return Object.class;
    }

    @NonNull
    @Override
    public Class<?> getOutputType() {
        return long.class;
    }

    @Override
    public boolean isSupported(Class<?> fieldType) {
        return super.isSupported(fieldType) && isReference(fieldType);
    }

    private static boolean isReference(Class<?> type) {
        return type != null && type.getAnnotation(Table.class) != null;
    }
}
