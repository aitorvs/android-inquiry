package com.heinrichreimer.inquiry;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.util.Log;

import com.heinrichreimer.inquiry.annotations.Column;
import com.heinrichreimer.inquiry.annotations.Table;
import com.heinrichreimer.inquiry.convert.Converter;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class DatabaseSchemaParser {
    @StringDef({BLOB, INTEGER, REAL, TEXT})
    @Retention(RetentionPolicy.SOURCE)
    @interface SQLType {
    }

    private static final String BLOB = "BLOB";
    private static final String INTEGER = "INTEGER";
    private static final String REAL = "REAL";
    private static final String TEXT = "TEXT";

    public static boolean isColumn(@NonNull Field field) {
        field.setAccessible(true);
        Column annotation = field.getAnnotation(Column.class);
        return annotation != null;
    }

    public static boolean isAutoIncrement(@NonNull Field field) {
        field.setAccessible(true);
        Column annotation = field.getAnnotation(Column.class);
        return annotation != null && annotation.autoIncrement();
    }

    @Nullable
    private static String getColumnName(@NonNull Column annotation) {
        if (annotation.value() != null && !annotation.value().trim().isEmpty())
            return annotation.value();
        return null;
    }

    @NonNull
    public static String getColumnName(@NonNull Field field) {
        field.setAccessible(true);
        Column annotation = field.getAnnotation(Column.class);
        if (annotation == null) return field.getName();
        String name = getColumnName(annotation);
        return name != null ? name : field.getName();
    }

    public static boolean isTable(@NonNull Class<?> type) {
        Table annotation = type.getAnnotation(Table.class);
        return annotation != null;
    }

    @Nullable
    private static String getTableName(@NonNull Table annotation) {
        if (annotation.value() != null && !annotation.value().trim().isEmpty())
            return annotation.value();
        return null;
    }

    @NonNull
    public static String getTableName(@NonNull Class<?> type) {
        Table annotation = type.getAnnotation(Table.class);
        if (annotation == null) return type.getSimpleName();
        String name = getTableName(annotation);
        return name != null ? name : type.getSimpleName();
    }

    @NonNull
    @SQLType
    private static String getClassTypeString(@NonNull List<Converter> converters, @NonNull Class<?> type) {
        if (!DatabaseAdapter.isSQLPrimitive(type)) {
            for (Converter converter : converters) {
                if (converter == null) continue;
                //noinspection unchecked
                if (!converter.isSupported(type)) continue;
                type = converter.getOutputType();
                break;
            }
            if (!DatabaseAdapter.isSQLPrimitive(type)) {
                throw new IllegalStateException(String.format("Class %s could not be converted to a SQLite data type.\n" +
                        "No supported converter found.\n" +
                        "Only annotate fields @Column if it is a SQLite compatible primitive or a converter for this type was installed.", type.getSimpleName()));
            }
        }

        if (type == byte[].class) {
            return BLOB;
        }
        else if (type == byte.class || type == Byte.class) {
            return BLOB;
        }
        else if (type == short.class || type == Short.class) {
            return INTEGER;
        }
        else if (type == int.class || type == Integer.class) {
            return INTEGER;
        }
        else if (type == long.class || type == Long.class) {
            return INTEGER;
        }
        else if (type == float.class || type == Float.class) {
            return REAL;
        }
        else if (type == double.class || type == Double.class) {
            return REAL;
        }
        else if (type == boolean.class || type == Boolean.class) {
            return INTEGER;
        }
        else if (type == char.class || type == Character.class) {
            return TEXT;
        }
        else if (type == String.class) {
            return TEXT;
        }
        return BLOB;
    }

    @Nullable
    private static String getFieldSchema(@NonNull List<Converter> converters, @NonNull Field field) {
        if (!isColumn(field)) return null;
        Column annotation = field.getAnnotation(Column.class);

        StringBuilder schema = new StringBuilder();
        schema.append(getColumnName(field));
        schema.append(" ");
        schema.append(getClassTypeString(converters, field.getType()));
        if (annotation.unique())
            schema.append(" UNIQUE");
        if (annotation.autoIncrement())
            schema.append(" AUTOINCREMENT");
        if (annotation.notNull())
            schema.append(" NOT NULL");
        return schema.toString();
    }

    @NonNull
    public static String getClassSchema(@NonNull List<Converter> converters, @NonNull Class<?> type) {
        StringBuilder schema = new StringBuilder();
        List<Field> fields = getAllFields(type);
        for (Field field : fields) {
            field.setAccessible(true);
            String fieldSchema = getFieldSchema(converters, field);
            if (fieldSchema == null) continue;
            if (fieldSchema.length() > 0)
                schema.append(", ");
            schema.append(fieldSchema);
        }
        if (schema.length() == 0)
            throw new IllegalStateException("Class " + type.getName() + " has no column fields.");
        schema.append(", _id INTEGER PRIMARY KEY AUTOINCREMENT");
        Log.d(Inquiry.DEBUG_TAG, String.format("Schema for %s: %s", type.getSimpleName(), schema.toString()));
        return schema.toString();
    }

    @NonNull
    public static String[] generateProjection(Class<?> type) {
        ArrayList<String> projection = new ArrayList<>();
        List<Field> fields = getAllFields(type);
        for (Field field : fields) {
            if (!isColumn(field)) continue;
            projection.add(getColumnName(field));
        }
        return projection.toArray(new String[projection.size()]);
    }

    public static List<Field> getAllFields(Class type) {
        List<Field> fields = new ArrayList<>();
        while (type != null) {
            Collections.addAll(fields, type.getDeclaredFields());
            type = type.getSuperclass();
        }
        return fields;
    }
}
