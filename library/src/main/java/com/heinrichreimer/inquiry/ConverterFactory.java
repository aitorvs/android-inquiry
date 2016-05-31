package com.heinrichreimer.inquiry;

import com.heinrichreimer.inquiry.convert.Converter;
import com.heinrichreimer.inquiry.convert.converters.BitmapConverter;
import com.heinrichreimer.inquiry.convert.converters.CharArrayConverter;
import com.heinrichreimer.inquiry.convert.converters.CharacterArrayConverter;
import com.heinrichreimer.inquiry.convert.converters.ReferenceConverter;
import com.heinrichreimer.inquiry.convert.converters.SerializableConverter;

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
        converters.add(new CharArrayConverter());
        converters.add(new CharacterArrayConverter());
        converters.add(new ReferenceConverter());
        return Collections.unmodifiableList(converters);
    }

    private static List<Converter> createLowPriorityConverters() {
        List<Converter> converters = new LinkedList<>();
        converters.add(new BitmapConverter());
        //Always insert SerializableConverter as last element.
        converters.add(new SerializableConverter());
        return Collections.unmodifiableList(converters);
    }
}
