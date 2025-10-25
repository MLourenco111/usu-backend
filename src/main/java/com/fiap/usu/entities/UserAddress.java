package com.fiap.usu.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fiap.usu.enums.EnumAddressType;
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
@Table(name = "users_address", indexes = {@Index(name = "ix_user_address_user", columnList = "user_id"),
        @Index(name = "ix_user_address_address", columnList = "address_id")})
public class UserAddress {

    @EmbeddedId
    private UserAddressId id;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @MapsId("addressId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "address_id", nullable = false)
    @JsonIgnore
    private Address address;

    private String complement;

    @Enumerated(EnumType.STRING)
    @Column(name = "address_type", length = 30, nullable = false)
    private EnumAddressType addressType = EnumAddressType.HOME;

    @Column(name = "is_primary", nullable = false)
    private Boolean primary = Boolean.FALSE;
}
