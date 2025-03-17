package com.kgy.usedCar.model;

import com.kgy.usedCar.dto.request.consult.ConsultRequestDto;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "consult")
public class ConsultEntity extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "status_type")
    private String statusType;

    @Column(name = "task_type")
    private String taskType = "접수중";

    @Column(name = "answer")
    private String answer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @OneToMany(mappedBy = "consult")
    private List<ConsultImageEntity> images;

    public static ConsultEntity of(UserEntity entity, ConsultRequestDto dto){
        return ConsultEntity.builder()
                .email(dto.getEmail())
                .title(dto.getTitle())
                .content(dto.getContent())
                .statusType(dto.getStatusType())
                .taskType("접수완료")
                .user(entity)
                .build();
    }

}
