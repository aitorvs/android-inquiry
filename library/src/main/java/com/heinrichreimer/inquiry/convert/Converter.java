package com.heinrichreimer.inquiry.convert;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.heinrichreimer.inquiry.ContentValue;
import com.heinrichreimer.inquiry.Inquiry;

import java.io.IOException;

public abstract class Converter<T, S> {
    @Nullable
    public abstract T convert(@NonNull Inquiry inquiry, @NonNull ContentValue<S> value, @NonNull Class<? extends T> fieldType) throws IOException;

    @NonNull
    public abstract ContentValue<S> convert(@NonNull Inquiry inquiry, @Nullable T value) throws IOException;

    @NonNull
    public abstract Class<T> getInputType();

    @NonNull
    public abstract Class<S> getOutputType();

    public boolean isSupported(Class<?> fieldType) {
        return getInputType().isAssignableFrom(fieldType);
    }
}