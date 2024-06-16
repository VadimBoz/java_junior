package main.java.ru.gb.lesson2.anno.lib;

import java.lang.reflect.Field;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class RandomAnnotationProcessor {

    public static void processAnnotation(Object obj) {
        // найти все поля класса, над которыми стоит аннотация @Random
        // вставить туда рандомное число в диапазоне [0, 100)

        java.util.Random random = new java.util.Random();
        Class<?> objClass = obj.getClass();
        for (Field field : objClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(Random.class) && field.getType().isAssignableFrom(int.class)) {
                Random annotation = field.getAnnotation(Random.class);
                int min = annotation.min();
                int max = annotation.max();
                try {
                    field.setAccessible(true); // чтобы можно было изменять финальные поля
                    field.set(obj, random.nextInt(min, max));
                } catch (IllegalAccessException e) {
                    System.err.println("Не удалось вставить значение в поле типа int: " + e.getMessage());
                }
            }

            if (field.isAnnotationPresent(RandomDate.class)) {
                RandomDate annotation2 = field.getAnnotation(RandomDate.class);
                long minDate = annotation2.minDate();
                long maxDate = annotation2.maxDate();
                int zoneOffset = annotation2.utcOffset();
                if (maxDate > minDate) {
                    if (field.getType().isAssignableFrom(java.util.Date.class)) {
                        try {
                            field.setAccessible(true);
                            field.set(obj, new Date(random.nextLong(minDate, maxDate)));
                        } catch (IllegalAccessException e) {
                            System.err.println("Не удалось вставить значение в поле типа Data: " + e.getMessage());
                        }
                    }
                    if (field.getType().isAssignableFrom(java.time.LocalDateTime.class)) {
                        try {
                            field.setAccessible(true);
                            field.set(obj, LocalDateTime.ofInstant(Instant.ofEpochMilli(random.nextLong(minDate, maxDate)), ZoneId.of("UTC+" + zoneOffset)));
                        } catch (IllegalAccessException e) {
                            System.err.println("Не удалось вставить значение в поле типа LocalDateTime: " + e.getMessage());
                        }
                    }
                } else System.out.println("Диапазон дат не может быть меньше минимальной даты");

            }
        }
    }
}