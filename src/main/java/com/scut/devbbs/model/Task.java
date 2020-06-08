package com.scut.devbbs.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long taskId;

    private String title;
    private String descWords;

    //起始时间，都为0表示永久
    private long startTime;
    private long endTime;

    //循环时间，0表示只能搞1次
    private long circleTime;

    private int reward;
}
