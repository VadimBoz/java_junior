package ru.gb.lesson4.hw;

import jakarta.persistence.*;

import java.sql.Timestamp;


@Entity
@Table(name = "post_comment")
public class PostComment {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "commentStr")
    private String commentStr;

//    @ManyToOne
//    @JoinColumn(name = "post_id")
//    private Post post;

    @ManyToOne
    @JoinColumn(name  = "user_id")
    private User user;

    @Column(name  = "date")
    private java.sql.Timestamp timestamp;


    public PostComment(String commentStr, User user) {
        this.commentStr = commentStr;
        this.user = user;
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }

    public PostComment() {
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCommentStr() {
        return commentStr;
    }

    public void setCommentStr(String commentStr) {
        this.commentStr = commentStr;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "PostComment{" +
                "commentStr='" + commentStr + '\'' +
                ", user=" + user +
                ", timestamp=" + timestamp +
                '}' + "\n";
    }
}
