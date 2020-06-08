package com.scut.devbbs.model;

import com.sun.deploy.services.PlatformType;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "comment_relate")
public class CommentRelate {
    public enum CommentRelateType {
        like,
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long commentRelateId;

    private long userId;
    private long commentId;
    private CommentRelateType type;
}
