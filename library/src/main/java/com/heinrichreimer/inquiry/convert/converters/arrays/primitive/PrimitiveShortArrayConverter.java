package com.heinrichreimer.inquiry.convert.converters.arrays.primitive;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.heinrichreimer.inquiry.ContentValue;
import com.heinrichreimer.inquiry.Inquiry;
import com.heinrichreimer.inquiry.convert.Converter;

import java.io.IOException;

public class PrimitiveShortArrayConverter extends Converter<short[], String> {
    @Nullable
    @Override
    public short[] convert(@NonNull Inquiry inquiry, @NonNull ContentValue<String> value, @NonNull Class<? extends short[]> fieldType) throws IOException {
        if (TextUtils.isEmpty(value.getContent()))
            return null;
        String[] shortStrings = value.getContent().split(",");
        short[] shorts = new short[shortStrings.length];
        for (int i = 0; i < shortStrings.length; i++) {
            try {
                shorts[i] = Short.valueOf(shortStrings[i]);
            } catch (NumberFormatException e) {
                shorts[i] = 0;
            }
        }
        return shorts;
    }

    @NonNull
    @Override
    public ContentValue<String> convert(@NonNull Inquiry inquiry, @Nullable short[] value) throws IOException {
        if (value == null || value.length == 0)
            return ContentValue.valueOf((String) null);
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (short aShort : value) {
            if (first)
                first = false;
            else
                sb.append(",");
            sb.append(aShort);
        }
        return ContentValue.valueOf(sb.toString());
    }

    @NonNull
    @Override
    public Class<short[]> getInputType() {
        return short[].class;
    }

    @NonNull
    @Override
    public Class<String> getOutputType() {
        return String.class;
    }
}
