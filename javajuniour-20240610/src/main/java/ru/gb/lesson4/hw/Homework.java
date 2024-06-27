package ru.gb.lesson4.hw;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class Homework {

  /**
   * Используя hibernate, создать таблицы:
   * 1. Post (публикация) (id, title)
   * 2. PostComment (комментарий к публикации) (id, text, post_id)
   *
   * Написать стандартные CRUD-методы: создание, загрузка, удаление.
   *
   * Доп. задания:
   * 1. * В сущностях post и postComment добавить поля timestamp с датами.
   * 2. * Создать таблицу users(id, name) и в сущностях post и postComment добавить ссылку на юзера.
   * 3. * Реализовать методы:
   * 3.1 Загрузить все комментарии публикации
   * 3.2 Загрузить все публикации по идентификатору юзера
   * 3.3 ** Загрузить все комментарии по идентификатору юзера
   * 3.4 **** По идентификатору юзера загрузить юзеров, под чьими публикациями он оставлял комменты.
   * // userId -> List<User>
   *
   *
   * Замечание:
   * 1. Можно использовать ЛЮБУЮ базу данных (например, h2)
   * 2. Если запутаетесь, приходите в группу в телеграме или пишите мне @inchestnov в личку.
   */

  public static void main(String[] args) {

      Configuration configuration = new Configuration()
              .configure();

        try (SessionFactory sessionFactory = configuration.buildSessionFactory()) {
            createData(sessionFactory);
            System.out.println();
            System.out.println("___________addCommentToPost______________________________");
            addCommentToPost(sessionFactory,  1L,  1L, "The comment has been added");
            System.out.println();
            System.out.println("___________modifyCommentToPost______________________________");
            modifyCommentToPost(sessionFactory,  1L,  "The comment has been modified");
            System.out.println();
            System.out.println("___________removeCommentToPost______________________________");
            removeCommentToPost(sessionFactory,  5L);
            System.out.println();
            System.out.println("___________getAllCommentToPost______________________________");
            System.out.println(getAllCommentToPost(sessionFactory, 1L));
            System.out.println();
            System.out.println("___________getAllPostUser______________________________");
            System.out.println(getAllPostUser(sessionFactory,  1L));
            System.out.println();
            System.out.println("___________etAllPostUser______________________________");
            System.out.println(getAllPostUser(sessionFactory,  1L));
            System.out.println();
            System.out.println("___________getUsersGiveCommentFromUser______________________________");
            System.out.println(getUsersGiveCommentFromUser(sessionFactory,  4L));
        } catch (Exception e)  { e.printStackTrace();}
  }



public static void addCommentToPost(SessionFactory sessionFactory, Long postId, Long userId, String textComment)  {
    try (Session session = sessionFactory.openSession()) {
        Transaction tx = session.beginTransaction();
            Post post = session.find(Post.class, postId);
            User userCommentToPost  = session.find(User.class, userId);
            PostComment postComment  = new PostComment(textComment, userCommentToPost);
            post.addPostComment(postComment);
        tx.commit();
    }
}

public static void modifyCommentToPost(SessionFactory sessionFactory,
                                       Long postCommentId, String newTextComment)   {
    try (Session session = sessionFactory.openSession()) {
        Transaction tx = session.beginTransaction();
        PostComment postComment  = session.find(PostComment.class, postCommentId);
        postComment.setCommentStr(newTextComment);
        tx.commit();
    }
}

public static void removeCommentToPost(SessionFactory sessionFactory, Long postCommentId) {
    try (Session session = sessionFactory.openSession()) {
        Transaction tx = session.beginTransaction();
            PostComment postComment = session.find(PostComment.class, postCommentId);
            session.remove(postComment);
        tx.commit();
    }
}




public static List<PostComment> getAllCommentToPost(SessionFactory sessionFactory, Long postId) {
    try (Session session = sessionFactory.openSession()) {
        Transaction tx = session.beginTransaction();
        Post post = session.find(Post.class, postId);
        List<PostComment> result = post.getPostComments();
        tx.commit();
        return result;
    }
}

public static List<Post> getAllPostUser(SessionFactory sessionFactory, Long postId) {
    try (Session session = sessionFactory.openSession()) {
        Transaction tx = session.beginTransaction();
            Query<Post> query = session.createQuery("select p from Post p where p.user.id = :id");
            query.setParameter("id", postId);
            Post post = session.find(Post.class, postId);
            List<Post> result = query.list();
        tx.commit();
        return result;
    }
}


    public static void createData(SessionFactory sessionFactory) {
        User user1 = new User("ivan1");
        User user2 = new User("ivan2");
        User user3 = new User("ivan4");
        User user4 = new User("ivan4");

        Post post1  = new Post(user1, "post1");
        Post post2  = new Post(user1, "post2");
        Post post3   = new Post(user2,  "post3");

        PostComment postComment1  = new PostComment("comment1", user3);
        PostComment postComment2  = new PostComment("comment2", user4);
        PostComment postComment3  = new PostComment("comment3", user2);

        PostComment postComment4  = new PostComment("comment4", user1);

        PostComment postComment5  = new PostComment("comment5", user1);
        PostComment postComment6  = new PostComment("comment6", user3);

        post1.addPostComment(postComment1);
        post1.addPostComment(postComment2);
        post1.addPostComment(postComment6);
        post2.addPostComment(postComment4);
        post3.addPostComment(postComment3);
        post3.addPostComment(postComment5);

        try (Session session = sessionFactory.openSession()){
            Transaction tx = session.beginTransaction();
            session.persist(user1);
            session.persist(user2);
            session.persist(user3);
            session.persist(user4);

            session.persist(post1);
            session.persist(post2);
            session.persist(post3);
            tx.commit();
        }
    }


    public static List<User> getUsersGiveCommentFromUser(SessionFactory sessionFactory, Long userId) {
        List<User> result = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
                result = session.createQuery(
                        "select p.user from Post p " +
                           "join p.postComments pc " +
                           "where pc.user.id = :idUserComment",
                        User.class).setParameter("idUserComment", userId).list();
            tx.commit();
        return result;

        }
    }

}
