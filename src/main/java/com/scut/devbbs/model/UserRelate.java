package com.scut.devbbs.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "user_relate")
public class UserRelate {

    public enum UserRelateType {
        friend,
        blacklist,
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userRelateId;

    private long user1Id;
    private long user2Id;
    private UserRelateType type;
    private long addTime;
}
