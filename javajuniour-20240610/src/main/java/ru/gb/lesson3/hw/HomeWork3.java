package ru.gb.lesson3.hw;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class HomeWork3 {

        /**
         * С помощью JDBC, выполнить следующие пункты:
         * 1. Создать таблицу Person (скопировать код с семниара)
         * 2. Создать таблицу Department (id bigint primary key, name varchar(128) not null)
         * 3. Добавить в таблицу Person поле department_id типа bigint (внешний ключ)
         * 4. Написать метод, который загружает Имя department по Идентификатору person
         * 5. * Написать метод, который загружает Map<String, String>, в которой маппинг person.name -> department.name
         *   Пример: [{"person #1", "department #1"}, {"person #2", "department #3}]
         * 6. ** Написать метод, который загружает Map<String, List<String>>, в которой маппинг department.name -> <person.name>
         *   Пример:
         *   [
         *     {"department #1", ["person #1", "person #2"]},
         *     {"department #2", ["person #3", "person #4"]}
         *   ]
         *
         *  7. *** Создать классы-обертки над таблицами, и в пунктах 4, 5, 6 возвращать объекты.
         */

        /**
         * Пункт 4
         */

        public static void main(String[] args) {
            try (Connection connection = DriverManager.getConnection("jdbc:h2:mem:test")) {
                createTableDepartment(connection);
                insertDataDepartment(connection);
                createTablePerson(connection);

                insertDataPerson(connection);
                String age = "55";
                System.out.println("Person с возрастом 55: " + selectNamesByAge(connection, age));

                updateData(connection);
                selectData(connection);

                System.out.println(getPersonDepartmentName(connection, 11));

                System.out.println(getPersonDepartments(connection));

                HashMap<Department, List<Person>>  map = getDepartmentPersons(connection);
                for (Department department :  map.keySet()) {
                    System.out.println(department);
                        for  (Person person  :  map.get(department))  {
                            System.out.println("     " + person);
                        }
                }

            } catch (SQLException e) {
                System.err.println("Во время подключения произошла ошибка: " + e.getMessage());
            }


        }


        private static String getPersonDepartmentName(Connection connection, long personId) throws SQLException {
            // FIXME: Ваш код тут
            try (PreparedStatement statement = connection.prepareStatement("""
                    SELECT person.id AS PersonID, person.name AS Name, person.age AS age, person.active AS active, 
                            department.name AS Dep_name, department.id AS Dep_id FROM person
                    JOIN department ON person.id_department = department.id
                    where person.id = ?
                         """)) {
                statement.setLong(1, personId);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    return resultSet.getString("Name") + " - " + resultSet.getString("Dep_name");
                } else throw new IllegalArgumentException("пользователь  c id не найден");
            }
        }

        /**
         * Пункт 5
         */
        private static HashMap<String, String> getPersonDepartments(Connection connection) throws SQLException {
            // FIXME: Ваш код тут
           HashMap<String, String> map  = new HashMap<>();
           try(Statement statement  = connection.createStatement()) {
               ResultSet resultSet = statement.executeQuery("""
                    SELECT person.id AS PersonID, person.name AS Name, person.age AS age, person.active AS active, 
                            department.name AS Dep_name, department.id AS Dep_id FROM person
                    JOIN department ON person.id_department = department.id
                    """);

               while (resultSet.next())  {
                   map.put(resultSet.getString("Name"), resultSet.getString("Dep_name"));
               }
           }
            return map;
        }

        /**
         * Пункт 6
         */
        private static HashMap<Department, List<Person>> getDepartmentPersons(Connection connection) throws SQLException {
            // FIXME: Ваш код тут
            HashMap<Department, List<Person>> resMap =  new HashMap<>();
            try(Statement statement  = connection.createStatement()) { // добавляем в map все департаменты
                ResultSet resultSet = statement.executeQuery("""
                    SELECT department.name AS Dep_name, department.id AS Dep_id FROM department;
                    """);
                while (resultSet.next())  {
                    resMap.put(new Department(resultSet), new ArrayList<>());
                }

            }

            try(Statement statement  = connection.createStatement()) {
                ResultSet resultSet = statement.executeQuery("""
                    SELECT person.id AS PersonID, person.name AS Name, person.age AS age, person.active AS active, 
                            department.name AS Dep_name, department.id AS Dep_id FROM person
                    JOIN department ON person.id_department = department.id
                    """);
                while (resultSet.next())  {
                    Department foundDep = null;
                    for (Department t : resMap.keySet()) {
                        if (t.getName().equals(resultSet.getString("Dep_name"))) {
                            foundDep = t;
                            break;
                        }
                    }
                    if  (foundDep != null)  {
                        resMap.get(foundDep).add(new Person(resultSet, foundDep));
                    }
                }
            }
            return resMap;
        }




    private static void createTablePerson(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("""
        CREATE TABLE person (
         	id BIGINT AUTO_INCREMENT PRIMARY KEY,
            name VARCHAR(256),
            age INTEGER,
            active BOOLEAN,
            id_department INTEGER,
            CONSTRAINT department_fk FOREIGN KEY (id_department) REFERENCES department (id)
         );
        """);
        } catch (SQLException e) {
            System.err.println("Во время создания таблицы произошла ошибка: " + e.getMessage());
            throw e;
        }
        System.out.println("Создана таблица person");
    }

    private static void createTableDepartment(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("""
        CREATE TABLE department (
            id BIGINT AUTO_INCREMENT PRIMARY KEY,
            name VARCHAR(256) NOT NULL)
        """);
        } catch (SQLException e) {
            System.err.println("Во время создания таблицы произошла ошибка: " + e.getMessage());
            throw e;
        }
        System.out.println("Создана таблица department");
    }

    private static void insertDataDepartment(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            StringBuilder insertQuery = new StringBuilder("insert into department(name) values\n");
            for (int i = 1; i <= 5; i++) {
                insertQuery.append(String.format("('%s')", "Department #" + i));
                if (i != 5) {
                    insertQuery.append(",\n");
                }
            }
            int insertCount = statement.executeUpdate(insertQuery.toString());
            System.out.println("Вставлено строк  в Department : " + insertCount);
        }
    }



    private static void insertDataPerson(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            StringBuilder insertQuery = new StringBuilder("insert into person(name, age, active, id_department) values\n");
            for (int i = 1; i <= 20; i++) {
                int age = ThreadLocalRandom.current().nextInt(20, 60);
                int department = ThreadLocalRandom.current().nextInt(1, 6) ;
                boolean active = ThreadLocalRandom.current().nextBoolean();
                insertQuery.append(String.format("('%s', %s, %s, %s)", "Person #" + i, age, active, department));
                if (i != 20) {
                    insertQuery.append(",\n");
                }
            }
            int insertCount = statement.executeUpdate(insertQuery.toString());
            System.out.println("Вставлено строк: " + insertCount);
        }

    }

    private static void updateData(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            int updateCount = statement.executeUpdate("update person set active = true where id > 5");
            System.out.println("Обновлено строк: " + updateCount);
        }
    }

    // static Optional<String> selectNameById(long id) {
    //   ...
    // }

    private static List<String> selectNamesByAge(Connection connection, String age) throws SQLException {
//    try (Statement statement = connection.createStatement()) {
//      statement.executeQuery("select name from person where age = " + age);
//      // where age = 1 or 1=1
//    }

        try (PreparedStatement statement =
                     connection.prepareStatement("select name from person where age = ?")) {
            statement.setInt(1, Integer.parseInt(age));
            ResultSet resultSet = statement.executeQuery();

            List<String> names = new ArrayList<>();
            while (resultSet.next()) {
                names.add(resultSet.getString("name"));
            }
            return names;
        }
    }

    private static void selectData(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("""
                SELECT person.id, person.name, person.age, person.id_department, department.name AS DepartmentName 
                FROM person
                JOIN department on person.id_department = department.id
                where active is true
                """);

            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                int idDepartment  = resultSet.getInt("id_department");
                String departmentName  = resultSet.getString("DepartmentName");
                // persons.add(new Person(id, name, age))
                System.out.println("Найдена строка: [id= " + id + ", name= " + name + ", age= " + age +
                        ", idDepartment=  "  + idDepartment  + ", Department =  " + departmentName);
            }
        }
    }




}
