package com.example.potalend;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)  // 마지막 수정 일시 저장위함?
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    @CreatedDate //등록된 일시 자동 저장
    private LocalDateTime createdDate;
    @LastModifiedDate // 마지막 수정 일시 저장
    private LocalDateTime midufedDate;
    @ManyToOne(cascade = CascadeType.ALL)  // Post로 name을 변경하게되면 실제 name이 바귐 이거 안하면 변경하더라도 DB는 바뀌지 않음
    @JsonIgnoreProperties({"comments"})
    private User user;
    @Version //처음에는 0으로 저장됨. 수정을하면 버전 자동으로 바뀜
    private Long version;
}
