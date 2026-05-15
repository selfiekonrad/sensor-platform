package com.ceniuch.db.model;

import lombok.Data;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "sensor")
public class Sensor implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private Long id;

    private String name;
    private String apiKey;
    private Date created;
}
