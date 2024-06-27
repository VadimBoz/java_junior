package ru.gb.lesson2.anno.lib;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RandomDate {
    long minDate() default 1704067200000L; // 1 января 2024 года UTC0

    long maxDate() default 1735689600000L; // 1 января 2025 года UTC0

    int utcOffset() default 0;
}
