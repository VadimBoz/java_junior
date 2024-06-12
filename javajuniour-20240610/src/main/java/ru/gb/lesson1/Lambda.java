package main.java.ru.gb.lesson1;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.*;

public class Lambda {
  static Integer num;


  public static void main(String[] args) {

    // () -> () - runnable
    Runnable runnable = new Runnable() {
      @Override
      public void run() {
        System.out.println("Hello, world!");
      }
    };

    Runnable helloPrinter = () -> {
      num = 1;
      System.out.print("Hello, ");
      System.out.println(" world!2");
    };

    runnable.run();
    helloPrinter.run();

    // (t) -> () - consumer (потребитель)
    Consumer<String> stringPrinter = (s) -> System.out.println(s);

    // () -> T - supplier (поставщик)
    Supplier<Integer> randIntProvider = () -> ThreadLocalRandom.current().nextInt(0, 10);

    // (T) -> R - function функция
    Function<String, Integer> stringLengthFunction = String::length;
    Function<String, String> toUpperCaseFunction = String::toUpperCase;
    UnaryOperator<String> toUpperCaseUnaryOperator = String::toUpperCase;

    stringLengthFunction.apply("abcdde");

    // s -> boolean - predicate тестер (фильтр)
    Predicate<Integer> isEvenPredicate = x -> x % 2 == 0;

    List<String> strings = new ArrayList<>(List.of("java", "c#", "c++", "python", "kotlin", "go"));
    Collections.sort(strings, (a, b) -> a.length() - b.length());
    System.out.println(strings);

    List.of(1, 2, 3, 4, 5).stream().reduce(0, Integer::sum);

    Predicate<String> isJava = "java"::equals;
    System.out.println(isJava.test("java"));
    System.out.println(isJava.test("python"));


    foo();
  }

  static Runnable foo() {
    // effectevly final
    String[] x = {"value"};
    Runnable xUpdater = () -> {
      x[0] = "updated";
    };

    xUpdater.run();
    System.out.println(Arrays.toString(x));

    return xUpdater;
  }


}
