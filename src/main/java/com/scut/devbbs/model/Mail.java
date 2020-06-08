package com.scut.devbbs.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "mail")
public class Mail {
    public enum MailState {
        unRead,
        read,
        toDelete,
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long mailId;

    private String title;
    private String content;
    private long fromId;
    private long toId;

    private boolean fromDelete;
    private long sendTime;
    private MailState state;
}
