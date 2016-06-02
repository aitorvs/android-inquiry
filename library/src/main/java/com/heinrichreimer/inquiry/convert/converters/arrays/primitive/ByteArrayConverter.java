package com.heinrichreimer.inquiry.convert.converters.arrays.primitive;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.heinrichreimer.inquiry.ContentValue;
import com.heinrichreimer.inquiry.Inquiry;
import com.heinrichreimer.inquiry.convert.Converter;

import java.io.IOException;

public class ByteArrayConverter extends Converter<Byte[], byte[]> {
    @Nullable
    @Override
    public Byte[] convert(@NonNull Inquiry inquiry, @NonNull ContentValue<byte[]> value, @NonNull Class<? extends Byte[]> fieldType) throws IOException {
        byte[] blob = value.getContent();
        if (blob == null)
            return null;
        Byte[] newBlob = new Byte[value.getContent().length];
        for (int i = 0; i < blob.length; i++) {
            newBlob[i] = blob[i];
        }
        return newBlob;
    }

    @NonNull
    @Override
    public ContentValue<byte[]> convert(@NonNull Inquiry inquiry, @Nullable Byte[] value) throws IOException {
        if (value == null)
            return ContentValue.valueOf((byte[]) null);
        byte[] blob = new byte[value.length];
        for (int i = 0; i < value.length; i++) {
            blob[i] = value[i];
        }
        return ContentValue.valueOf(blob);
    }

    @NonNull
    @Override
    public Class<Byte[]> getInputType() {
        return Byte[].class;
    }

    @NonNull
    @Override
    public Class<byte[]> getOutputType() {
        return byte[].class;
    }
}
