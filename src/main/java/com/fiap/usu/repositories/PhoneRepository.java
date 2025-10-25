package com.fiap.usu.repositories;

import com.fiap.usu.entities.Phone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PhoneRepository extends JpaRepository<Phone, Long> {

    @Query("SELECT p FROM Phone p WHERE p.user.id = :userId AND p.number = :number")
    Optional<Phone> findByUserAndNumber(@Param("userId") Long userId, @Param("number") String number);

}
