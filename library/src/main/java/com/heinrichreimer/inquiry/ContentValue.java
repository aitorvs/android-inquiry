package com.heinrichreimer.inquiry;

import android.support.annotation.Nullable;

public class ContentValue {
    private final Object content;

    private ContentValue(Object content) {
        this.content = content;
        // We can't use content.getClass() here as primitives were casted to their object
        // representations before.
    }

    public static ContentValue valueOf(byte[] content) {
        return new ContentValue(content);
    }

    public static ContentValue valueOf(byte content) {
        return new ContentValue(content);
    }

    public static ContentValue valueOf(Byte content) {
        return new ContentValue(content);
    }

    public static ContentValue valueOf(short content) {
        return new ContentValue(content);
    }

    public static ContentValue valueOf(Short content) {
        return new ContentValue(content);
    }

    public static ContentValue valueOf(int content) {
        return new ContentValue(content);
    }

    public static ContentValue valueOf(Integer content) {
        return new ContentValue(content);
    }

    public static ContentValue valueOf(long content) {
        return new ContentValue(content);
    }

    public static ContentValue valueOf(Long content) {
        return new ContentValue(content);
    }

    public static ContentValue valueOf(float content) {
        return new ContentValue(content);
    }

    public static ContentValue valueOf(Float content) {
        return new ContentValue(content);
    }

    public static ContentValue valueOf(double content) {
        return new ContentValue(content);
    }

    public static ContentValue valueOf(Double content) {
        return new ContentValue(content);
    }

    public static ContentValue valueOf(boolean content) {
        return new ContentValue(content);
    }

    public static ContentValue valueOf(Boolean content) {
        return new ContentValue(content);
    }

    public static ContentValue valueOf(char content) {
        return new ContentValue(content);
    }

    public static ContentValue valueOf(Character content) {
        return new ContentValue(content);
    }

    public static ContentValue valueOf(String content) {
        return new ContentValue(content);
    }

    public static ContentValue valueOfNull() {
        return new ContentValue(null);
    }

    @Nullable
    public Object getContent() {
        return content;
    }

}
