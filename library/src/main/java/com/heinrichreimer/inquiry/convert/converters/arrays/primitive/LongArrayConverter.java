package com.heinrichreimer.inquiry.convert.converters.arrays.primitive;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.heinrichreimer.inquiry.ContentValue;
import com.heinrichreimer.inquiry.Inquiry;
import com.heinrichreimer.inquiry.convert.Converter;

import java.io.IOException;

public class LongArrayConverter extends Converter<Long[], String> {
    @Nullable
    @Override
    public Long[] convert(@NonNull Inquiry inquiry, @NonNull ContentValue<String> value, @NonNull Class<? extends Long[]> fieldType) throws IOException {
        if (TextUtils.isEmpty(value.getContent()))
            return null;
        String[] longStrings = value.getContent().split(",");
        Long[] longs = new Long[longStrings.length];
        for (int i = 0; i < longStrings.length; i++) {
            try {
                longs[i] = Long.valueOf(longStrings[i]);
            } catch (NumberFormatException e) {
                longs[i] = null;
            }
        }
        return longs;
    }

    @NonNull
    @Override
    public ContentValue<String> convert(@NonNull Inquiry inquiry, @Nullable Long[] value) throws IOException {
        if (value == null || value.length == 0)
            return ContentValue.valueOf((String) null);
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (Long aLong : value) {
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
    public Class<Long[]> getInputType() {
        return Long[].class;
    }

    @NonNull
    @Override
    public Class<String> getOutputType() {
        return String.class;
    }
}
