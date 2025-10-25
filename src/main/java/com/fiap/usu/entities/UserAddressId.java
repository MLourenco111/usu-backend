package com.fiap.usu.entities;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Setter
@Getter
@Embeddable
public class UserAddressId implements Serializable {
    private Long userId;
    private Long addressId;

    public UserAddressId() {
    }

    public UserAddressId(Long userId, Long addressId) {
        this.userId = userId;
        this.addressId = addressId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof UserAddressId that))
            return false;
        return Objects.equals(userId, that.userId) && Objects.equals(addressId, that.addressId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, addressId);
    }

}
