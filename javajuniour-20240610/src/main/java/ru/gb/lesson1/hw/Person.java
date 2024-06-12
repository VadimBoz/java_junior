package main.java.ru.gb.lesson1.hw;


public class Person {
    private String name;
    private int age;
    private double salary;
    private Department depart;

    // TODO: геттеры, сеттеры
    public Person(String name, int age, double salary, Department depart) {
        this.name = name;
        this.age = age;
        this.salary = salary;
        this.depart = depart;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public Department getDepart() {
        return depart;
    }

    public void setDepart(Department depart) {
        this.depart = depart;
    }

    @Override
    public String toString() {
        return "Person{" + name + ", age= "+ age + ", salary= " + salary + ", depart= " + depart + '}' + "\n";
    }
}