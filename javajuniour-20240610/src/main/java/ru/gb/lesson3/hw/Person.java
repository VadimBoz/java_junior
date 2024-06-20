package ru.gb.lesson3.hw;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Person {
    private long id;
    private String name;
    private int age;
    private boolean active;
    Department department;

    public Person(long id, String name, int age, boolean active, Department department) {
        this.id = id;
        this.name = name;
        this.active = active;
        this.department = department;
        this.age  = age;
    }

    public Person() {
        this(-1, "Unknown", -1, false, new Department());
    }

    public Person(ResultSet resultSet) throws SQLException {
        this(resultSet, new Department(resultSet.getLong("Dep_id"), resultSet.getString("Dep_name")));
    }

    public Person(ResultSet resultSet, Department department) throws SQLException {
        this.id = resultSet.getLong("id");
        this.name = resultSet.getString("name");
        this.age = resultSet.getInt("age");
        this.active = resultSet.getBoolean("active");
        this.department = department;
    }



    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }


    @Override
    public String toString() {
        return "Person{" + "id=" + id + ", name=" + name + ", age=" + age + ", active=" + active + ", department=" + department + '}';
    }
}
