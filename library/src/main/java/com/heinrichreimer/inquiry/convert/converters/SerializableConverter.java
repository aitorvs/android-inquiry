package com.heinrichreimer.inquiry.convert.converters;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.heinrichreimer.inquiry.ContentValue;
import com.heinrichreimer.inquiry.Inquiry;
import com.heinrichreimer.inquiry.convert.Converter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class SerializableConverter extends Converter<Serializable, byte[]> {
    @Nullable
    @Override
    public Serializable convert(@NonNull Inquiry inquiry, @NonNull ContentValue<byte[]> value, @NonNull Class<? extends Serializable> fieldType) throws IOException {
        try {
            byte[] blob = value.getContent();
            return blob == null ? null : deserializeObject(blob, fieldType);
        } catch (IllegalArgumentException e) {
            throw new IOException(e);
        }
    }

    @NonNull
    @Override
    public ContentValue<byte[]> convert(@NonNull Inquiry inquiry, @Nullable Serializable value) throws IOException {
        return ContentValue.valueOf(value == null ? null : serializeObject(value));
    }

    @NonNull
    @Override
    public Class<Serializable> getInputType() {
        return Serializable.class;
    }

    @NonNull
    @Override
    public Class<byte[]> getOutputType() {
        return byte[].class;
    }

    @Override
    public boolean isSupported(Class<?> fieldType) {
        //Arrays in java implement Serializable. However they are only serializable if their components are.
        return super.isSupported(fieldType) &&
                (!fieldType.isArray() || isSupported(fieldType.getComponentType()));
    }

    @NonNull
    private static byte[] serializeObject(@NonNull Object object) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutput output = null;
        try {
            output = new ObjectOutputStream(outputStream);
            output.writeObject(object);
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new IllegalStateException("Unable to serialize object of type " + object.getClass().getName(), e);
        } finally {
            try {
                if (output != null)
                    output.close();
            } catch (IOException ignored) {
            }
            try {
                outputStream.close();
            } catch (IOException ignored) {
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Nullable
    private static <T extends Serializable> T deserializeObject(@NonNull byte[] data, @NonNull Class<T> type) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        ObjectInput input = null;
        try {
            input = new ObjectInputStream(inputStream);
            Object object = input.readObject();
            if (type.isAssignableFrom(object.getClass()))
                return (T) object;
            return null;
        } catch (Exception e) {
            throw new IllegalStateException("Unable to deserialize data to type " + type.getName(), e);
        } finally {
            closeQuietely(inputStream);
            if (input != null) {
                try {
                    input.close();
                } catch (IOException ignored) {
                }
            }
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
