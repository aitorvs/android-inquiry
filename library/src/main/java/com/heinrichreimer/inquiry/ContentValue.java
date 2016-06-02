package com.heinrichreimer.inquiry;

import android.support.annotation.Nullable;

public class ContentValue<S> {
    private final S content;

    private ContentValue(S content) {
        this.content = content;
        // We can't use content.getClass() here as primitives were casted to their object
        // representations before.
    }

    public static ContentValue<byte[]> valueOf(@Nullable byte[] content) {
        return new ContentValue<>(content);
    }

    public static ContentValue<Byte> valueOf(@Nullable Byte content) {
        return new ContentValue<>(content);
    }

    public static ContentValue<Short> valueOf(@Nullable Short content) {
        return new ContentValue<>(content);
    }

    public static ContentValue<Integer> valueOf(@Nullable Integer content) {
        return new ContentValue<>(content);
    }

    public static ContentValue<Long> valueOf(@Nullable Long content) {
        return new ContentValue<>(content);
    }

    public static ContentValue<Float> valueOf(@Nullable Float content) {
        return new ContentValue<>(content);
    }

    public static ContentValue<Double> valueOf(@Nullable Double content) {
        return new ContentValue<>(content);
    }

    public static ContentValue<Boolean> valueOf(@Nullable Boolean content) {
        return new ContentValue<>(content);
    }

    public static ContentValue<Character> valueOf(@Nullable Character content) {
        return new ContentValue<>(content);
    }

    public static ContentValue<String> valueOf(@Nullable String content) {
        return new ContentValue<>(content);
    }

    @Nullable
    public S getContent() {
        return content;
    }

}
