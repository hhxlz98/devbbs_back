package com.scut.devbbs.model;


import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "user")
public class User {
    public enum UserStatus {
        normal,
        baned;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String userName;
    private String userPassword;
    private String userEmail;
    private String userSex;

    private String userImage;
    private String userShow;
    private String userIntro;
    private long registerTime;

    private int pts;
    private int changeCount;

    private UserStatus status;
}
