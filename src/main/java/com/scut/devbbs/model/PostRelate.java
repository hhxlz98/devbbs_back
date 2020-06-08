package com.scut.devbbs.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "post_relate")
public class PostRelate {
    public enum PostRelateType {
        like,
        unlike,
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long postRelateId;

    private long userId;
    private long postId;
    private PostRelateType type;
}
