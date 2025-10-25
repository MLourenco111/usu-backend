package com.fiap.usu.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fiap.usu.entities.UserAddress;

public interface UserAddressRepository extends JpaRepository<UserAddress, Long> {
}
