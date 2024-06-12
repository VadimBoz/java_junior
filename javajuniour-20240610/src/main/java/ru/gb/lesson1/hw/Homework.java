package main.java.ru.gb.lesson1.hw;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Homework {
  private static final List<Person> people = getPerson(20);




  public static void main(String[] args) {
//    System.out.println(people);
    System.out.println("_______findMostYoungestPerson_________");
    System.out.println(findMostYoungestPerson(people));

    System.out.println("_______findMostExpensiveDepartment_________");
    System.out.println(findMostExpensiveDepartment(people));
    System.out.println();
    System.out.println("_____groupByDepartment_________");
    Map<Department, List<Person>> mapDep = groupByDepartment(people);
    System.out.println(mapDep);
    System.out.println();
    System.out.println("_____groupByDepartmentName_________");
    Map<String, List<Person>> mapDep2 = groupByDepartmentName(people);
    System.out.println(mapDep2);
    System.out.println();
    System.out.println("_____getDepartmentOldestPersone_________");
    System.out.println(getDepartmentOldestPerson(people));
    System.out.println();
    System.out.println("_____cheapPersonsInDepartment_________");
    System.out.println(cheapPersonsInDepartment(people));



  }



  /**
   * Найти самого молодого сотрудника
   */
  static Optional<Person> findMostYoungestPerson(List<Person> people) {
    // FIXME: ваша реализация здесь
    return people.stream().min(Comparator.comparingInt(Person::getAge));
  }


  /**
   * Найти департамент, в котором работает сотрудник с самой большой зарплатой
   */
  static Optional<Department> findMostExpensiveDepartment(List<Person> people) {
    // FIXME: ваша реализация здесь
    return people.stream().max(Comparator.comparingDouble(Person::getSalary))
            .map(Person::getDepart);
  }

  /**
//   * Сгруппировать сотрудников по департаментам
//   */
  static Map<Department, List<Person>> groupByDepartment(List<Person> people) {
    // FIXME: ваша реализация здесь
    return people.stream().collect(Collectors.groupingBy(Person::getDepart));
  }



  /**
   * Сгруппировать сотрудников по названиям департаментов
   */
  static Map<String, List<Person>> groupByDepartmentName(List<Person> people) {
    // FIXME: ваша реализация здесь
    return people.stream()
            .collect(Collectors.groupingBy(s -> s.getDepart().getName()));
  }



  /**
   * В каждом департаменте найти самого старшего сотрудника
   */
  static Map<String, Person> getDepartmentOldestPerson(List<Person> people) {
    // FIXME: ваша реализация здесь
    return people.stream()
            .collect(Collectors.toMap(
                    s -> s.getDepart().getName(),
                    Function.identity(),
                    (a, b) -> a.getAge() > b.getAge() ? a : b));
  }




  /**
   * *Найти сотрудников с минимальными зарплатами в своем отделе
   * (прим. можно реализовать в два запроса)
   */
  static List<Person> cheapPersonsInDepartment(List<Person> people) {
    // FIXME: ваша реализация здесь
    return people.stream()
            .collect(Collectors.groupingBy(Person::getDepart)) // group by department (Map<Department, List<Person>>)
            .values().stream()
            .map(s -> s.stream().min(Comparator.comparingDouble(Person::getSalary)).get())
            .collect(Collectors.toList());
  }




  private static List<Person> getPerson(int countPersons)  {
    List<Person>  persons = new ArrayList<>();
    List<String> listAllPersons = Data.firstNames;
    List<String> listAllDepartName = Data.depatNames;
    int countAllPersons = listAllPersons.size();
    int countAllPDepartName = listAllDepartName.size();
    if (countPersons > countAllPersons)   throw new IllegalArgumentException("countPersons > countAllPersons");
    int i  =  0;
    while (i < countPersons){
      String curName = listAllPersons.get((int)(Math.random() * countAllPersons));
        if (persons.stream().noneMatch(s -> s.getName().equals(curName)))  {
          Department curDepartment =  new Department(listAllDepartName.get((int)(Math.random()  * countAllPDepartName)));
          int curAge = (int)(Math.random() * 47) + 18;
          double curSalary = Math.random() * 100_000 + 30_000;
          persons.add(new Person(curName, curAge, curSalary, curDepartment));
          i++;
      }
    }
    return persons;
  }







}
