package com.heinrichreimer.inquiry.convert.converters.arrays.primitive;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.heinrichreimer.inquiry.ContentValue;
import com.heinrichreimer.inquiry.Inquiry;
import com.heinrichreimer.inquiry.convert.Converter;

import java.io.IOException;

public class FloatArrayConverter extends Converter<Float[], String> {
    @Nullable
    @Override
    public Float[] convert(@NonNull Inquiry inquiry, @NonNull ContentValue<String> value, @NonNull Class<? extends Float[]> fieldType) throws IOException {
        if (TextUtils.isEmpty(value.getContent()))
            return null;
        String[] floatStrings = value.getContent().split(",");
        Float[] floats = new Float[floatStrings.length];
        for (int i = 0; i < floatStrings.length; i++) {
            try {
                floats[i] = Float.valueOf(floatStrings[i]);
            } catch (NumberFormatException e) {
                floats[i] = null;
            }
        }
        return floats;
    }

    @NonNull
    @Override
    public ContentValue<String> convert(@NonNull Inquiry inquiry, @Nullable Float[] value) throws IOException {
        if (value == null || value.length == 0)
            return ContentValue.valueOf((String) null);
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (Float aFloat : value) {
            if (first)
                first = false;
            else
                sb.append(",");
            sb.append(aFloat);
        }
        return ContentValue.valueOf(sb.toString());
    }

    @NonNull
    @Override
    public Class<Float[]> getInputType() {
        return Float[].class;
    }

    @NonNull
    @Override
    public Class<String> getOutputType() {
        return String.class;
    }
}
