package com.example.potalend;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity(name = "userinfo")
public class User {
    @Id  //primary Key
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // DB에서 자동 Increment 된 값을 쓸꺼야
    private Long id;
    private String name;
    private String password;

    @JsonIgnoreProperties({"user"})
    @OneToMany(mappedBy = "user")
    private List<Comment> comments;
}
