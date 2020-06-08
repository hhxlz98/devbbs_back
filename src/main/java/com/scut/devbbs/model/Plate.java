package com.scut.devbbs.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "plate")
public class Plate {
    public enum PlateState {
        normal,
        delete,
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long plateId;

    private String plateName;
    private String plateIntro;
    private String plateImg = "/plateImg/default.jpg";

    private int followNumber = 0;
    private int postNumber = 0;
    private PlateState state;
}
