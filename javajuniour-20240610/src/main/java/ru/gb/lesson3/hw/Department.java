package ru.gb.lesson3.hw;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Department {
    private long id;
    private String name;

    public Department(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Department() {
        this(-1, "no department");
    }

    public Department(ResultSet resultSet) throws SQLException {
        this(resultSet.getLong("Dep_id"), resultSet.getString("Dep_name"));
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

    @Override
    public String toString() {
        return "Department{" + "id=" + id + ", name='" + name  + '}';
    }
}
