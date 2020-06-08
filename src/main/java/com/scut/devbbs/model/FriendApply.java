package com.scut.devbbs.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "friend_apply")
public class FriendApply {
    public enum FriendApplyState {
        unRead,
        agree,
        disagree,
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long friendApplyId;

    private long applyId;
    private long appliedId;
    private String applyInfo;

    private long applyTime;
    private FriendApplyState state;




}
