package com.Chatapi.Entities;

import com.Chatapi.Enums.Role;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long userId;

    @Column(length = 35,unique = true)
    private String login;

    @Column(nullable = false, length = 100)
    private String password;

    private String avatar;

    private String surname;

    private String name;

    private String middleName;

    private Long dialogId;

    @Column(nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime created_on;
}