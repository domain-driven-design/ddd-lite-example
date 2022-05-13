package com.example.domain.user.model;

import lombok.Getter;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;

@Getter
@Entity
@Table(name = "user")
@FieldNameConstants
public class User {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    private String id;

    private String name;

    private String email;

    private String password;

    private Instant createdAt;

    private Instant updatedAt;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private Status status;

    public void setName(String name) {
        this.name = name;
        this.updatedAt = Instant.now();
    }

    public void setPassword(String password) {
        this.password = password;
        this.updatedAt = Instant.now();
    }

    public void setStatus(Status status) {
        this.status = status;
        this.updatedAt = Instant.now();
    }

    public enum Role {
        ADMIN, USER
    }

    public enum Status {
        NORMAL, FROZEN
    }

    public static User build(String name, String email, String password) {
        User user = new User();

        user.name = name;
        user.email = email;
        user.password = password;
        user.role = Role.USER;
        user.status = Status.NORMAL;

        return user;
    }
}
