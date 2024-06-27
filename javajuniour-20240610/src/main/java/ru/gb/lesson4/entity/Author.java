package ru.gb.lesson4.entity;

import jakarta.persistence.*;

import java.util.List;

// create table author(
//   id bigint primary key,
//   name varchar(256)
// )

@Entity
@Table(name = "author")
public class Author { // final нельзя

  @Id
  @Column(name = "id")
  private Long id;

  @Column(name = "name")
  private String name;

  //  @OneToMany(mappedBy = "author")
  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
    name = "author_book",
    joinColumns = @JoinColumn(name = "author_id"),
    inverseJoinColumns = @JoinColumn(name = "book_id")
  )
  private List<Book> books;

  @Column(name  = "age")
  private Integer age;
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Author() {
  }
  public List<Book> getBooks() {
    return books;
  }

  public void setBooks(List<Book> books) {
    this.books = books;
  }


  public Integer getAge() {
    return age;
  }

  public void setAge(Integer age) {
    this.age = age;
  }

  @Override
  public String toString() {
    return "Author{" + "id= " + id + ", name=" + name + '}';
  }
}
