package com.heinrichreimer.inquiry;

import android.support.annotation.Nullable;

public class ContentValue<S> {
    private final S content;

    private ContentValue(S content) {
        this.content = content;
        // We can't use content.getClass() here as primitives were casted to their object
        // representations before.
    }

    public static ContentValue<byte[]> valueOf(byte[] content) {
        return new ContentValue<>(content);
    }

    public static ContentValue<Byte> valueOf(Byte content) {
        return new ContentValue<>(content);
    }

    public static ContentValue<Short> valueOf(Short content) {
        return new ContentValue<>(content);
    }

    public static ContentValue<Integer> valueOf(Integer content) {
        return new ContentValue<>(content);
    }

    public static ContentValue<Long> valueOf(Long content) {
        return new ContentValue<>(content);
    }

    public static ContentValue<Float> valueOf(Float content) {
        return new ContentValue<>(content);
    }

    public static ContentValue<Double> valueOf(Double content) {
        return new ContentValue<>(content);
    }

    public static ContentValue<Boolean> valueOf(Boolean content) {
        return new ContentValue<>(content);
    }

    public static ContentValue<Character> valueOf(Character content) {
        return new ContentValue<>(content);
    }

    public static ContentValue<String> valueOf(String content) {
        return new ContentValue<>(content);
    }

    @Nullable
    public S getContent() {
        return content;
    }

}
