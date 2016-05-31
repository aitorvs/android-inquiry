package com.heinrichreimer.inquiry;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.SparseArray;

import com.heinrichreimer.inquiry.convert.Converter;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

class DatabaseAdapter {

    public static boolean isSQLPrimitive(@NonNull Class<?> type) {
        return type == byte[].class ||
                type == byte.class || type == Byte.class ||
                type == short.class || type == Short.class ||
                type == int.class || type == Integer.class ||
                type == long.class || type == Long.class ||
                type == float.class || type == Float.class ||
                type == double.class || type == Double.class ||
                type == boolean.class || type == Boolean.class ||
                type == char.class || type == Character.class ||
                type == String.class;
    }

    public static boolean isSQLPrimitive(@Nullable Object object) {
        return object == null || isSQLPrimitive(object.getClass());
    }

    public static boolean isSQLPrimitive(@NonNull Field field) {
        return isSQLPrimitive(field.getType());
    }

    public static ContentValues save(@NonNull Inquiry inquiry, @NonNull List<Converter> converters, @NonNull Object object, @Nullable String[] projection) {
        ContentValues contentValues = new ContentValues();
        List<Field> fields = DatabaseSchemaParser.getAllFields(object.getClass());

        int columnCount = 0;
        for (Field field : fields) {
            field.setAccessible(true);

            if (!DatabaseSchemaParser.isColumn(field)) continue;

            String columnName = DatabaseSchemaParser.getColumnName(field);
            if (projection != null && projection.length > 0) {
                boolean skip = true;
                for (String column : projection) {
                    if (column != null && column.equalsIgnoreCase(columnName)) {
                        skip = false;
                        break;
                    }
                }
                if (skip) continue;
            }

            columnCount++;

            if (DatabaseSchemaParser.isAutoIncrement(field)) continue;

            Object fieldValue;
            try {
                fieldValue = field.get(object);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(String.format("Field %s could not be inserted.", field.getName()), e);
            }

            if (!save(inquiry, converters, contentValues, columnName, fieldValue)) {
                throw new RuntimeException(String.format("Field %s could not be inserted. This is most likely a problem with a custom converter.", field.getName()));
            }
        }
        if (columnCount == 0)
            throw new IllegalStateException("Class " + object.getClass().getName() + " has no column fields.");
        return contentValues;
    }

    private static boolean save(@NonNull Inquiry inquiry, @NonNull List<Converter> converters, @NonNull ContentValues contentValues, @NonNull String column, @Nullable Object object) {
        Class<?> type = object == null ? null : object.getClass();
        if (!isSQLPrimitive(object)) {
            for (Converter converter : converters) {
                if (converter == null) continue;
                //noinspection unchecked
                if (!converter.isSupported(type)) continue;
                try {
                    //noinspection unchecked
                    ContentValue contentValue = converter.convert(inquiry, object);
                    object = contentValue.getContent();
                    type = object == null ? null : object.getClass();
                    break;
                } catch (IOException e) {
                    return false;
                }
            }
            if (!isSQLPrimitive(object)) {
                return false;
            }
        }

        if (object == null) {
            contentValues.putNull(column);
        }
        else if (type == byte[].class) {
            contentValues.put(column, (byte[]) object);
        }
        else if (type == byte.class || type == Byte.class) {
            contentValues.put(column, new byte[]{(byte) object});
        }
        else if (type == short.class || type == Short.class) {
            contentValues.put(column, (short) object);
        }
        else if (type == int.class || type == Integer.class) {
            contentValues.put(column, (int) object);
        }
        else if (type == long.class || type == Long.class) {
            contentValues.put(column, (long) object);
        }
        else if (type == float.class || type == Float.class) {
            contentValues.put(column, (float) object);
        }
        else if (type == double.class || type == Double.class) {
            contentValues.put(column, (double) object);
        }
        else if (type == boolean.class || type == Boolean.class) {
            contentValues.put(column, (boolean) object);
        }
        else if (type == char.class || type == Character.class) {
            contentValues.put(column, String.valueOf((char) object));
        }
        else if (type == String.class) {
            contentValues.put(column, (String) object);
        }
        return true;
    }

    public static <T> T load(@NonNull Inquiry inquiry, @NonNull List<Converter> converters, @NonNull Cursor cursor, @NonNull Class<T> type) {
        T instance = Utils.newInstance(type);

        //Build field cache
        SparseArray<Field> fieldCache = new SparseArray<>();
        List<Field> fields = DatabaseSchemaParser.getAllFields(type);
        for (Field field : fields) {
            if (!DatabaseSchemaParser.isColumn(field)) continue;
            fieldCache.put(cursor.getColumnIndex(DatabaseSchemaParser.getColumnName(field)), field);
        }

        for (int i = 0; i < cursor.getColumnCount(); i++) {
            Field field = fieldCache.get(i);
            if (!load(inquiry, converters, cursor, i, instance, field)) {
                throw new RuntimeException(String.format("Field %s could not be loaded. This is most likely a problem with a custom converter.", field.getName()));
            }
        }
        return instance;
    }

    private static boolean load(@NonNull Inquiry inquiry, @NonNull List<Converter> converters, @NonNull Cursor cursor, @IntRange(from = 0) int column, @NonNull Object object, @NonNull Field field) {
        field.setAccessible(true);
        Class<?> type = field.getType();
        try {
            if (isSQLPrimitive(field)) {
                //We can load this directly
                if (cursor.isNull(column)) {
                    //Set default value
                    if (type == byte[].class) {
                        field.set(object, null);
                    }
                    if (type == byte.class || type == Byte.class) {
                        field.set(object, (byte) 0);
                    }
                    else if (type == short.class || type == Short.class) {
                        field.set(object, (short) 0);
                    }
                    else if (type == int.class || type == Integer.class) {
                        field.set(object, 0);
                    }
                    else if (type == long.class || type == Long.class) {
                        field.set(object, 0L);
                    }
                    else if (type == float.class || type == Float.class) {
                        field.set(object, 0.0f);
                    }
                    else if (type == double.class || type == Double.class) {
                        field.set(object, 0.0d);
                    }
                    else if (type == boolean.class || type == Boolean.class) {
                        field.set(object, false);
                    }
                    else if (type == char.class || type == Character.class) {
                        field.set(object, '\u0000');
                    }
                    else if (type == String.class) {
                        field.set(object, null);
                    }
                }
                else {
                    if (type == byte[].class) {
                        field.set(object, cursor.getBlob(column));
                    }
                    if (type == byte.class || type == Byte.class) {
                        byte[] blob = cursor.getBlob(column);
                        field.set(object, blob != null && blob.length > 0 ? blob[0] : (byte) 0);
                    }
                    else if (type == short.class || type == Short.class) {
                        field.set(object, cursor.getShort(column));
                    }
                    else if (type == int.class || type == Integer.class) {
                        field.set(object, cursor.getInt(column));
                    }
                    else if (type == long.class || type == Long.class) {
                        field.set(object, cursor.getLong(column));
                    }
                    else if (type == float.class || type == Float.class) {
                        field.set(object, cursor.getFloat(column));
                    }
                    else if (type == double.class || type == Double.class) {
                        field.set(object, cursor.getDouble(column));
                    }
                    else if (type == boolean.class || type == Boolean.class) {
                        field.set(object, cursor.getInt(column) == 1);
                    }
                    else if (type == char.class || type == Character.class) {
                        String string = cursor.getString(column);
                        field.set(object, string != null && !string.isEmpty() ? string.charAt(0) : '\u0000');
                    }
                    else if (type == String.class) {
                        field.set(object, cursor.getString(column));
                    }
                }
                return true;
            }
            else {
                for (Converter converter : converters) {
                    if (converter == null) continue;
                    //noinspection unchecked
                    if (!converter.isSupported(type)) continue;
                    try {
                        type = converter.getOutputType();

                        ContentValue contentValue;
                        if (type == byte[].class) {
                            contentValue = ContentValue.valueOf(cursor.getDouble(column));
                        }
                        else if (type == byte.class || type == Byte.class) {
                            byte[] blob = cursor.getBlob(column);
                            contentValue = ContentValue.valueOf(blob != null && blob.length > 0 ? blob[0] : (byte) 0);
                        }
                        else if (type == short.class || type == Short.class) {
                            contentValue = ContentValue.valueOf(cursor.getShort(column));
                        }
                        else if (type == int.class || type == Integer.class) {
                            contentValue = ContentValue.valueOf(cursor.getInt(column));
                        }
                        else if (type == long.class || type == Long.class) {
                            contentValue = ContentValue.valueOf(cursor.getLong(column));
                        }
                        else if (type == float.class || type == Float.class) {
                            contentValue = ContentValue.valueOf(cursor.getFloat(column));
                        }
                        else if (type == double.class || type == Double.class) {
                            contentValue = ContentValue.valueOf(cursor.getDouble(column));
                        }
                        else if (type == boolean.class || type == Boolean.class) {
                            contentValue = ContentValue.valueOf(cursor.getInt(column) == 1);
                        }
                        else if (type == char.class || type == Character.class) {
                            String string = cursor.getString(column);
                            contentValue = ContentValue.valueOf(string != null && !string.isEmpty() ? string.charAt(0) : '\u0000');
                        }
                        else if (type == String.class) {
                            contentValue = ContentValue.valueOf(cursor.getString(column));
                        }
                        else {
                            return false;
                        }

                        //noinspection unchecked
                        Object value = converter.convert(inquiry, contentValue, field.getType());
                        field.set(object, value);
                        //Successfully converted the SQLite primitive to an object
                        return true;
                    } catch (IOException e) {
                        return false;
                    }
                }
                return false;
            }
        } catch (IllegalAccessException e) {
            return false;
        }
    }
}
