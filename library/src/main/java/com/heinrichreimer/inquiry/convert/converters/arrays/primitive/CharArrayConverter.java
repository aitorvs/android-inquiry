package com.heinrichreimer.inquiry.convert.converters.arrays.primitive;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.heinrichreimer.inquiry.ContentValue;
import com.heinrichreimer.inquiry.Inquiry;
import com.heinrichreimer.inquiry.convert.Converter;

import java.io.IOException;

public class CharArrayConverter extends Converter<Character[], String> {
    @Nullable
    @Override
    public Character[] convert(@NonNull Inquiry inquiry, @NonNull ContentValue<String> value, @NonNull Class<? extends Character[]> fieldType) throws IOException {
        String string = value.getContent();
        char[] chars = !TextUtils.isEmpty(string) ? string.toCharArray() : null;
        if (chars == null) return null;
        Character[] characters = new Character[chars.length];
        for (int i = 0; i < chars.length; i++) {
            characters[i] = chars[i];
        }
        return characters;
    }

    @NonNull
    @Override
    public ContentValue<String> convert(@NonNull Inquiry inquiry, @Nullable Character[] value) throws IOException {
        if (value == null || value.length == 0)
            return ContentValue.valueOf((String) null);
        StringBuilder sb = new StringBuilder();
        for (Character character : value) {
            sb.append(character);
        }
        return ContentValue.valueOf(sb.toString());
    }

    @NonNull
    @Override
    public Class<Character[]> getInputType() {
        return Character[].class;
    }

    @NonNull
    @Override
    public Class<String> getOutputType() {
        return String.class;
    }
}
