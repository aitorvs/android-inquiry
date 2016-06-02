package com.heinrichreimer.inquiry.convert.converters.arrays.primitive;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.heinrichreimer.inquiry.ContentValue;
import com.heinrichreimer.inquiry.Inquiry;
import com.heinrichreimer.inquiry.convert.Converter;

import java.io.IOException;

public class DoubleArrayConverter extends Converter<Double[], String> {
    @Nullable
    @Override
    public Double[] convert(@NonNull Inquiry inquiry, @NonNull ContentValue<String> value, @NonNull Class<? extends Double[]> fieldType) throws IOException {
        if (TextUtils.isEmpty(value.getContent()))
            return null;
        String[] doubleStrings = value.getContent().split(",");
        Double[] doubles = new Double[doubleStrings.length];
        for (int i = 0; i < doubleStrings.length; i++) {
            try {
                doubles[i] = Double.valueOf(doubleStrings[i]);
            } catch (NumberFormatException e) {
                doubles[i] = null;
            }
        }
        return doubles;
    }

    @NonNull
    @Override
    public ContentValue<String> convert(@NonNull Inquiry inquiry, @Nullable Double[] value) throws IOException {
        if (value == null || value.length == 0)
            return ContentValue.valueOf((String) null);
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (Double aDouble : value) {
            if (first)
                first = false;
            else
                sb.append(",");
            sb.append(aDouble);
        }
        return ContentValue.valueOf(sb.toString());
    }

    @NonNull
    @Override
    public Class<Double[]> getInputType() {
        return Double[].class;
    }

    @NonNull
    @Override
    public Class<String> getOutputType() {
        return String.class;
    }
}
