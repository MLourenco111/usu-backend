package com.fiap.usu.dtos.user;

import java.time.Instant;
import java.time.LocalDate;

public class UserPagedDto {
    private Long id;
    private String name;
    private String email;
    private String login;
    private Instant createdAt;
    private Instant updatedAt;
    private boolean status;
    private LocalDate birthday;
    private String document;

    public UserPagedDto(Long id, String name, String email, String login, Instant createdAt, Instant updatedAt, boolean status,
                        LocalDate birthday, String document) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.login = login;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.status = status;
        this.birthday = birthday;
        this.document = document;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getLogin() {
        return login;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public boolean isStatus() {
        return status;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public String getDocument() {
        return document;
    }
}
