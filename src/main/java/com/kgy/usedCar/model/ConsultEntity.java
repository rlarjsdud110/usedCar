package com.kgy.usedCar.model;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "consult")
public class ConsultEntity extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "status_type")
    private String statusType;

    @Column(name = "task_type")
    private String taskType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

}
