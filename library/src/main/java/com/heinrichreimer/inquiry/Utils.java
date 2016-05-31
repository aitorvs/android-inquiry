package com.heinrichreimer.inquiry;

import android.support.annotation.NonNull;

import java.lang.reflect.Constructor;

final class Utils {

    @SuppressWarnings("unchecked")
    public static <T> T newInstance(@NonNull Class<T> type) {
        final Constructor constructor = getDefaultConstructor(type);
        try {
            return (T) constructor.newInstance();
        } catch (Throwable t) {
            t.printStackTrace();
            throw new RuntimeException("Failed to instantiate " + type.getName() + ": " + t.getLocalizedMessage());
        }
    }

    public static Constructor<?> getDefaultConstructor(@NonNull Class<?> type) {
        final Constructor[] constructors = type.getDeclaredConstructors();
        Constructor defaultConstructor = null;
        for (Constructor constructor : constructors) {
            defaultConstructor = constructor;
            if (defaultConstructor.getGenericParameterTypes().length == 0)
                break;
        }
        if (defaultConstructor == null)
            throw new IllegalStateException("No default constructor found for " + type.getName());
        defaultConstructor.setAccessible(true);
        return defaultConstructor;
    }

}