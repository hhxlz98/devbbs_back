package com.scut.devbbs.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "post")
public class Post {
    public enum PostStatus {
        unsolved,
        solved,
        outDate;
    }

    public enum postState {
       normal,
       selfDelete,
       beDelete;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long postId;

    private long authorId;
    private long plateId;
    private String title;
    private String content;
    private String tab;
    private long publishTime;
    private long replyTime;
    private long editTime;
    private postState state;

    private boolean good;
    //Share, Ask
    private boolean top;
    private int replyNumber = 0;
    private int visitNumber = 0;

    //share帖子
    private int likeNumber = 0;

    //ask帖子
    private int reward = 0;
    private int helpfulReward = 0;
    private int helpfulNumber;
    private int lastTime;
    private PostStatus status;
}
