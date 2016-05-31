package com.heinrichreimer.inquiry.convert.converters;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.heinrichreimer.inquiry.ContentValue;
import com.heinrichreimer.inquiry.Inquiry;
import com.heinrichreimer.inquiry.convert.Converter;

import java.io.IOException;

public class CharacterArrayConverter extends Converter<Character[]> {
    @Override
    public Character[] convert(@NonNull Inquiry inquiry, @NonNull ContentValue value, @NonNull Class<? extends Character[]> fieldType) throws IOException {
        String string = (String) value.getContent();
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
    public ContentValue convert(@NonNull Inquiry inquiry, @NonNull Character[] value) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (Character character : value) {
            sb.append(character);
        }
        return ContentValue.valueOf(sb.toString());
    }

    @NonNull
    @Override
    public Class<? extends Character[]> getInputType() {
        return Character[].class;
    }

    @NonNull
    @Override
    public Class<?> getOutputType() {
        return String.class;
    }
}
