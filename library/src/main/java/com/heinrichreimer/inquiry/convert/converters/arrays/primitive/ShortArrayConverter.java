package com.heinrichreimer.inquiry.convert.converters.arrays.primitive;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.heinrichreimer.inquiry.ContentValue;
import com.heinrichreimer.inquiry.Inquiry;
import com.heinrichreimer.inquiry.convert.Converter;

import java.io.IOException;

public class ShortArrayConverter extends Converter<Short[], String> {
    @Nullable
    @Override
    public Short[] convert(@NonNull Inquiry inquiry, @NonNull ContentValue<String> value, @NonNull Class<? extends Short[]> fieldType) throws IOException {
        if (TextUtils.isEmpty(value.getContent()))
            return null;
        String[] shortStrings = value.getContent().split(",");
        Short[] shorts = new Short[shortStrings.length];
        for (int i = 0; i < shortStrings.length; i++) {
            try {
                shorts[i] = Short.valueOf(shortStrings[i]);
            } catch (NumberFormatException e) {
                shorts[i] = null;
            }
        }
        return shorts;
    }

    @NonNull
    @Override
    public ContentValue<String> convert(@NonNull Inquiry inquiry, @Nullable Short[] value) throws IOException {
        if (value == null || value.length == 0)
            return ContentValue.valueOf((String) null);
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (Short aShort : value) {
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
    public Class<Short[]> getInputType() {
        return Short[].class;
    }

    @NonNull
    @Override
    public Class<String> getOutputType() {
        return String.class;
    }
}
