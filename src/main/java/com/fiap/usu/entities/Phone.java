package com.fiap.usu.entities;

import com.fiap.usu.enums.EnumPhoneType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "phones", indexes = {@Index(name = "ix_phones_user", columnList = "user_id")})
public class Phone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    private String number;
    @Enumerated(EnumType.STRING)
    private EnumPhoneType type;
}
