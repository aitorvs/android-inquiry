package com.heinrichreimer.inquiry.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)

public @interface Column {
    String value() default "";

    boolean primaryKey() default false;
    boolean unique() default false;
    boolean autoIncrement() default false;
    boolean notNull() default false;
    int version() default 1; // database version
}