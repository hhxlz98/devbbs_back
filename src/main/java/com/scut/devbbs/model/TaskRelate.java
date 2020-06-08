package com.scut.devbbs.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "task_relate")
public class TaskRelate {
    public enum TaskState {
        doing,
        canReward,
        finish,
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long taskRelateId;

    private long userId;
    private long taskId;

    private long addTime;
    private long finishTime;

    private TaskState state;
}
