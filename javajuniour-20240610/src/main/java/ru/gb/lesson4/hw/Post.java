package ru.gb.lesson4.hw;

import jakarta.persistence.*;
import ru.gb.lesson4.hw.PostComment;
import ru.gb.lesson4.hw.User;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "post")
public class Post {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name  =  "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name  = "postComment_id")
    private List<PostComment> postComments;


    @Column(name = "name")
    private String name;


    @Column(name  = "date")
    private java.sql.Timestamp timestamp;

    public Post(User user, String name) {
        this.id = id;
        this.user = user;
        this.postComments = new ArrayList<>();
        this.name = name;
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }


    public Post() {
        this.timestamp = new Timestamp(System.currentTimeMillis());
        this.postComments = new ArrayList<>();
    }

    public List<PostComment> getPostComments() {
        return postComments;
    }

    public void setPostComments(List<PostComment> postComments) {
        this.postComments = postComments;
    }

    public void addPostComment(PostComment postComment)  {
        postComments.add(postComment);

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", user=" + user +
                ", name='" + name + '\'' +
                '}' + "\n";
    }
}
