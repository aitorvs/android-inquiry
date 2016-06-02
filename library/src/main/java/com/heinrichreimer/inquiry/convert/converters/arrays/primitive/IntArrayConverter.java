package com.heinrichreimer.inquiry.convert.converters.arrays.primitive;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.heinrichreimer.inquiry.ContentValue;
import com.heinrichreimer.inquiry.Inquiry;
import com.heinrichreimer.inquiry.convert.Converter;

import java.io.IOException;

public class IntArrayConverter extends Converter<Integer[], String> {
    @Nullable
    @Override
    public Integer[] convert(@NonNull Inquiry inquiry, @NonNull ContentValue<String> value, @NonNull Class<? extends Integer[]> fieldType) throws IOException {
        if (TextUtils.isEmpty(value.getContent()))
            return null;
        String[] intStrings = value.getContent().split(",");
        Integer[] ints = new Integer[intStrings.length];
        for (int i = 0; i < intStrings.length; i++) {
            try {
                ints[i] = Integer.valueOf(intStrings[i]);
            } catch (NumberFormatException e) {
                ints[i] = null;
            }
        }
        return ints;
    }

    @NonNull
    @Override
    public ContentValue<String> convert(@NonNull Inquiry inquiry, @Nullable Integer[] value) throws IOException {
        if (value == null || value.length == 0)
            return ContentValue.valueOf((String) null);
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (Integer integer : value) {
            if (first)
                first = false;
            else
                sb.append(",");
            sb.append(integer);
        }
        return ContentValue.valueOf(sb.toString());
    }

    @NonNull
    @Override
    public Class<Integer[]> getInputType() {
        return Integer[].class;
    }

    @NonNull
    @Override
    public Class<String> getOutputType() {
        return String.class;
    }
}
