package com.heinrichreimer.inquiry.convert.converters.arrays.primitive;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.heinrichreimer.inquiry.ContentValue;
import com.heinrichreimer.inquiry.Inquiry;
import com.heinrichreimer.inquiry.convert.Converter;

import java.io.IOException;

public class PrimitiveDoubleArrayConverter extends Converter<double[], String> {
    @Nullable
    @Override
    public double[] convert(@NonNull Inquiry inquiry, @NonNull ContentValue<String> value, @NonNull Class<? extends double[]> fieldType) throws IOException {
        if (TextUtils.isEmpty(value.getContent()))
            return null;
        String[] doubleStrings = value.getContent().split(",");
        double[] doubles = new double[doubleStrings.length];
        for (int i = 0; i < doubleStrings.length; i++) {
            try {
                doubles[i] = Double.valueOf(doubleStrings[i]);
            } catch (NumberFormatException e) {
                doubles[i] = 0.0d;
            }
        }
        return doubles;
    }

    @NonNull
    @Override
    public ContentValue<String> convert(@NonNull Inquiry inquiry, @Nullable double[] value) throws IOException {
        if (value == null || value.length == 0)
            return ContentValue.valueOf((String) null);
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (double aDouble : value) {
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
    public Class<double[]> getInputType() {
        return double[].class;
    }

    @NonNull
    @Override
    public Class<String> getOutputType() {
        return String.class;
    }
}
