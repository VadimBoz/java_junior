DROP DATABASE If EXISTS `new_schema1`;
CREATE DATABASE `new_schema1`;
USE `new_schema1`;

CREATE TABLE department (
	id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(256) NOT NULL
);

CREATE TABLE person (
	id INT AUTO_INCREMENT PRIMARY KEY,
	name VARCHAR(256),
	age INTEGER,
	active BOOLEAN,
    id_department INTEGER,
    CONSTRAINT department_fk FOREIGN KEY (id_department) REFERENCES department (id)
);

CREATE TABLE department (
	id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(256) NOT NULL
);

insert into department (name) values
 ("d1"),
 ("d2");

insert into person(name, age, active, id_department) values 
("Ivan", 34, TRUE, 1),
("Ivan2", 35, TRUE, 2),
("Ivan3", 36, TRUE, 2);

SELECT person.id, person.name, person.age, department.name  FROM person 
JOIN department on person.id_department = department.id
where person.id = 1;


SELECT person.id AS ID, person.name AS Name, person.age AS age, person.active AS activity, 
department.name AS Dep_name, department.id AS Dep_id FROM person
JOIN department ON person.id_department = Dep_id
where ID = 1
