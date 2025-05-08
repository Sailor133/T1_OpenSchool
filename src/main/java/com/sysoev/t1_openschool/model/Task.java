package com.sysoev.t1_openschool.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name="tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String status;
    private String description;
    private Long userId;
}
