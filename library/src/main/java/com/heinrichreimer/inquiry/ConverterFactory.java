package com.heinrichreimer.inquiry;

import com.heinrichreimer.inquiry.convert.Converter;
import com.heinrichreimer.inquiry.convert.converters.BitmapConverter;
import com.heinrichreimer.inquiry.convert.converters.ReferenceConverter;
import com.heinrichreimer.inquiry.convert.converters.SerializableConverter;
import com.heinrichreimer.inquiry.convert.converters.arrays.ReferenceArrayConverter;
import com.heinrichreimer.inquiry.convert.converters.arrays.primitive.BooleanArrayConverter;
import com.heinrichreimer.inquiry.convert.converters.arrays.primitive.ByteArrayConverter;
import com.heinrichreimer.inquiry.convert.converters.arrays.primitive.CharArrayConverter;
import com.heinrichreimer.inquiry.convert.converters.arrays.primitive.DoubleArrayConverter;
import com.heinrichreimer.inquiry.convert.converters.arrays.primitive.FloatArrayConverter;
import com.heinrichreimer.inquiry.convert.converters.arrays.primitive.IntArrayConverter;
import com.heinrichreimer.inquiry.convert.converters.arrays.primitive.LongArrayConverter;
import com.heinrichreimer.inquiry.convert.converters.arrays.primitive.PrimitiveBooleanArrayConverter;
import com.heinrichreimer.inquiry.convert.converters.arrays.primitive.PrimitiveCharArrayConverter;
import com.heinrichreimer.inquiry.convert.converters.arrays.primitive.PrimitiveDoubleArrayConverter;
import com.heinrichreimer.inquiry.convert.converters.arrays.primitive.PrimitiveFloatArrayConverter;
import com.heinrichreimer.inquiry.convert.converters.arrays.primitive.PrimitiveIntArrayConverter;
import com.heinrichreimer.inquiry.convert.converters.arrays.primitive.PrimitiveLongArrayConverter;
import com.heinrichreimer.inquiry.convert.converters.arrays.primitive.PrimitiveShortArrayConverter;
import com.heinrichreimer.inquiry.convert.converters.arrays.primitive.ShortArrayConverter;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

class ConverterFactory {
    private static final List<Converter> HIGH_PRIORITY_CONVERTERS = createHighPriorityConverters();
    private static final List<Converter> LOW_PRIORITY_CONVERTERS = createLowPriorityConverters();

    private ConverterFactory() {
    }

    public static List<Converter> getHighPriorityConverters() {
        return HIGH_PRIORITY_CONVERTERS;
    }

    public static List<Converter> getLowPriorityConverters() {
        return LOW_PRIORITY_CONVERTERS;
    }

    private static List<Converter> createHighPriorityConverters() {
        List<Converter> converters = new LinkedList<>();
        //References
        converters.add(new ReferenceConverter());
        //Primitive arrays
        converters.add(new ByteArrayConverter());
        converters.add(new PrimitiveShortArrayConverter());
        converters.add(new ShortArrayConverter());
        converters.add(new PrimitiveIntArrayConverter());
        converters.add(new IntArrayConverter());
        converters.add(new PrimitiveLongArrayConverter());
        converters.add(new LongArrayConverter());
        converters.add(new PrimitiveFloatArrayConverter());
        converters.add(new FloatArrayConverter());
        converters.add(new PrimitiveDoubleArrayConverter());
        converters.add(new DoubleArrayConverter());
        converters.add(new PrimitiveBooleanArrayConverter());
        converters.add(new BooleanArrayConverter());
        converters.add(new PrimitiveCharArrayConverter());
        converters.add(new CharArrayConverter());
        //Reference arrays
        converters.add(new ReferenceArrayConverter());
        return Collections.unmodifiableList(converters);
    }

    private static List<Converter> createLowPriorityConverters() {
        List<Converter> converters = new LinkedList<>();
        converters.add(new BitmapConverter());
        //Serializables
        converters.add(new SerializableConverter());
        return Collections.unmodifiableList(converters);
    }
}
