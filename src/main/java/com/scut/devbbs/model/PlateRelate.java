package com.scut.devbbs.model;


import com.sun.deploy.services.PlatformType;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "plate_relate")
public class PlateRelate {
    public enum PlateRelateType {
        follow,
        manage;

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long plateRelateId;

    private long userId;
    private long plateId;
    private PlateRelateType type;

}
