package com.heinrichreimer.inquiry.convert.converters.arrays;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.heinrichreimer.inquiry.ContentValue;
import com.heinrichreimer.inquiry.Inquiry;
import com.heinrichreimer.inquiry.convert.Converter;

import java.io.IOException;

public class CharArrayConverter extends Converter<char[], String> {
    @Override
    public char[] convert(@NonNull Inquiry inquiry, @NonNull ContentValue<String> value, @NonNull Class<? extends char[]> fieldType) throws IOException {
        String string = value.getContent();
        return !TextUtils.isEmpty(string) ? string.toCharArray() : null;
    }

    @NonNull
    @Override
    public ContentValue<String> convert(@NonNull Inquiry inquiry, @NonNull char[] value) throws IOException {
        return ContentValue.valueOf(String.valueOf(value));
    }

    @NonNull
    @Override
    public Class<? extends char[]> getInputType() {
        return char[].class;
    }

    @NonNull
    @Override
    public Class<String> getOutputType() {
        return String.class;
    }
}
