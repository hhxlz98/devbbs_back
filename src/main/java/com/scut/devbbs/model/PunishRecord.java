package com.scut.devbbs.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "punish_record")
public class PunishRecord {
    public enum PunishType {
        forbiddenSpeak,
        ban;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long punishRecordId;

    private long userId;
    private PunishType type;
    private long startTime;
    private long lastTime;

    //0表示全版块,封禁时有用
    private long plateId;
    //0表示系统管理员操作
    private long dealUserId;
}
