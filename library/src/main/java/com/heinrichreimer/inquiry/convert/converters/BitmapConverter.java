package com.heinrichreimer.inquiry.convert.converters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.heinrichreimer.inquiry.ContentValue;
import com.heinrichreimer.inquiry.Inquiry;
import com.heinrichreimer.inquiry.convert.Converter;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;

public class BitmapConverter extends Converter<Bitmap, byte[]> {
    @Override
    public Bitmap convert(@NonNull Inquiry inquiry, @NonNull ContentValue<byte[]> value, @NonNull Class<? extends Bitmap> fieldType) throws IOException {
        byte[] blob = value.getContent();
        try {
            return blob == null ? null : BitmapFactory.decodeByteArray(blob, 0, blob.length);
        } catch (IllegalArgumentException e) {
            throw new IOException(e);
        }
    }

    @NonNull
    @Override
    public ContentValue<byte[]> convert(@NonNull Inquiry inquiry, @NonNull Bitmap value) throws IOException {
        return ContentValue.valueOf(bitmapToBytes(value));
    }

    @NonNull
    @Override
    public Class<? extends Bitmap> getInputType() {
        return Bitmap.class;
    }

    @NonNull
    @Override
    public Class<byte[]> getOutputType() {
        return byte[].class;
    }


    private static byte[] bitmapToBytes(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            return outputStream.toByteArray();
        } finally {
            closeQuietely(outputStream);
        }
    }

    private static void closeQuietely(@Nullable Closeable c) {
        if (c != null) {
            try {
                c.close();
            } catch (IOException ignored) {
            }
        }
    }
}
