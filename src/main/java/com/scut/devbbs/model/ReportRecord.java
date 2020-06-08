package com.scut.devbbs.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "report_record")
public class ReportRecord {
    public enum ReportType {
        post,
        reply,
        userInfo;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long reportRecordId;

    private long reportUserId;
    private long reportedUserId;

    private long reportTime;
    private String reportInfo;
    private ReportType type;
    private long typeId;
    private long plateId;
    private boolean isDeal;
}
