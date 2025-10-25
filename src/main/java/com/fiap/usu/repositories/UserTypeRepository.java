package com.fiap.usu.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fiap.usu.entities.UserType;

@Repository
public interface UserTypeRepository extends JpaRepository<UserType, Long> {

}
