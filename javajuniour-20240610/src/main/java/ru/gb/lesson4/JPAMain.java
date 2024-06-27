package ru.gb.lesson4;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import ru.gb.lesson4.entity.Author;
import ru.gb.lesson4.entity.AuthorBook;
import ru.gb.lesson4.entity.Book;


public class JPAMain {

  // employee(id, department_id)
  // department(id)

  // Employee e = session.find(Employee.class, 1L);
  // Department d2 = session.find(Department.class, 2L);
  // e.setDepartment(d2);
  // session.merge(e)

  public static void main(String[] args) {
    // ORM - Object Relation Model
    // JPA - Jakarta Persistence API -
    // Hibernate - реализация JPA
    // EclipseLink - тоже реализация JPA

    // connection poll
    // hikari



    Configuration configuration = new Configuration()
            .configure()
            .addPackage("ru.gb.lesson4");
//     !!! иначе cfg.xml не прочитается
//    try (SessionFactory sessionFactory = configuration.buildSessionFactory()) {
//       sessionFactory <-> connection
//      withSession(sessionFactory);
//      withSessionCRUD(sessionFactory);
//    }
  }

  private static void withSession(SessionFactory sessionFactory) {
    try (Session session = sessionFactory.openSession()) {
      Author author = new Author();
      author.setId(1L);
      author.setName("Author1");
      author.setAge(21);

      Book book = new Book();
      book.setId(1L);
      book.setName("Book");
//      book.setAuthor(author);

      AuthorBook authorBook = new AuthorBook();
      authorBook.setId(1L); // id
      authorBook.setAuthor(author); // author_id
      authorBook.setBook(book); // book_id

      Transaction tx = session.beginTransaction();
      session.createQuery("delete from " + Book.class.getSimpleName()).executeUpdate();
      session.createQuery("delete from " + Author.class.getSimpleName()).executeUpdate();
      session.persist(author);
      session.persist(book);
//      if (true) {
//        throw new RuntimeException();
//      }

//      session.persist(authorBook);
      tx.commit();
    }

    try (Session session = sessionFactory.openSession()) {
      Author author = session.find(Author.class, 1L);
      System.out.println("---------" + author);
      Book book  = session.find(Book.class, 1L);
      System.out.println(book);
//      for (Book book : author.getBooks()) {
//        System.out.println(book);
//      }
    }
  }

  // create read update delete
  private static void withSessionCRUD(SessionFactory sessionFactory) {
    try (Session session = sessionFactory.openSession()) {
      // session <-> statement

      Author author = session.find(Author.class, 1L);
      System.out.println("Author(1) = " + author);
    }

    try (Session session = sessionFactory.openSession()) {
      Transaction tx = session.beginTransaction();
      Author author = new Author();
      author.setId(22L);
      author.setName("Author #22");
      author.setAge(30);

      session.persist(author); // insert
      tx.commit();
    }

    try (Session session = sessionFactory.openSession()) {
      Author toUpdate = session.find(Author.class, 22L);
      session.detach(toUpdate);
      toUpdate.setName("UPDATED");

      Transaction tx = session.beginTransaction();
      session.merge(toUpdate); // update
      tx.commit();
    }

    try (Session session = sessionFactory.openSession()) {
      Author toDelete = session.find(Author.class, 1L);

      Transaction tx = session.beginTransaction();
      session.remove(toDelete); // delete
      tx.commit();
    }



  }

}
