package com.heinrichreimer.inquiry.convert.converters.arrays.primitive;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.heinrichreimer.inquiry.ContentValue;
import com.heinrichreimer.inquiry.Inquiry;
import com.heinrichreimer.inquiry.convert.Converter;

import java.io.IOException;

public class PrimitiveLongArrayConverter extends Converter<long[], String> {
    @Nullable
    @Override
    public long[] convert(@NonNull Inquiry inquiry, @NonNull ContentValue<String> value, @NonNull Class<? extends long[]> fieldType) throws IOException {
        if (TextUtils.isEmpty(value.getContent()))
            return null;
        String[] longStrings = value.getContent().split(",");
        long[] longs = new long[longStrings.length];
        for (int i = 0; i < longStrings.length; i++) {
            try {
                longs[i] = Long.valueOf(longStrings[i]);
            } catch (NumberFormatException e) {
                longs[i] = 0L;
            }
        }
        return longs;
    }

    @NonNull
    @Override
    public ContentValue<String> convert(@NonNull Inquiry inquiry, @Nullable long[] value) throws IOException {
        if (value == null || value.length == 0)
            return ContentValue.valueOf((String) null);
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (long aLong : value) {
            if (first)
                first = false;
            else
                sb.append(",");
            sb.append(aLong);
        }
        return ContentValue.valueOf(sb.toString());
    }

    @NonNull
    @Override
    public Class<long[]> getInputType() {
        return long[].class;
    }

    @NonNull
    @Override
    public Class<String> getOutputType() {
        return String.class;
    }
}
