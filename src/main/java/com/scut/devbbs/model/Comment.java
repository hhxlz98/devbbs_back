package com.scut.devbbs.model;


import com.sun.deploy.xml.GeneralEntity;

import javax.persistence.*;

@Entity
@Table(name = "comment")
public class Comment {
    public enum CommentState {
        normal,
        selfDelete,
        beDelete,
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long commentId;

    private long authorId;
    private long postId;
    private String content;
    private long publishTime;
    private long superCommentId;
    private int likeNumber;
    private int floorNumber;

    private boolean best;
    private boolean helpful;
    private boolean read;
    private CommentState state;
}
