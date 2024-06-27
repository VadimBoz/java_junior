package ru.gb.lesson2.anno;

//import lombok.Data;
import ru.gb.lesson2.anno.lib.ObjectCreator;
import ru.gb.lesson2.anno.lib.Random;
import ru.gb.lesson2.anno.lib.RandomDate;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class AnnotationsMain {

  public static void main(String[] args) {

    Person rndPerson = ObjectCreator.createObj(Person.class);
    System.out.println("age1 = " + rndPerson.age1);
    System.out.println("age2 = " + rndPerson.age2);
    System.out.println("RandomDate:  " + rndPerson.date1);
    System.out.println("RandomDate:  " + rndPerson.date2);
    System.out.println("RandomDate:  " + rndPerson.date3);
    System.out.println("Random LocalDateTime:  " + rndPerson.date4);

    // extPerson.isAssignableFrom(ExtPerson.class) // true
    // extPerson.isAssignableFrom(Person.class) // false
    // person.isAssignableFrom(ExtPerson.class) // true

//    Person p = new Person();
//    Person ep = new ExtPerson();

//    System.out.println(p.getClass().isAssignableFrom(Person.class)); // true
//    System.out.println(p.getClass().isAssignableFrom(ExtPerson.class)); // true
//
//    System.out.println(ep.getClass().isAssignableFrom(Person.class)); // false
//    System.out.println(ep.getClass().isAssignableFrom(ExtPerson.class)); // true

  }

  public static class ExtPerson extends Person {

  }

  public static class Person {

    @Random // рандомное число в диапазоне [0, 100)
    private int age1;

    @Random(min = 50, max = 51) // рандомное число в диапазоне [50, 51) => 50
    private int age2;

    @Random
    private String age3;

    @RandomDate(minDate = 0)
    private Date date1;
    @RandomDate(minDate = 300, maxDate  =  5000)
    private Date date2;

    @RandomDate(minDate = 0, maxDate  =  0)
    private Date date3;

    @RandomDate(utcOffset = 1, minDate = 0,  maxDate  =  1)
    private LocalDateTime date4;

  }

}
