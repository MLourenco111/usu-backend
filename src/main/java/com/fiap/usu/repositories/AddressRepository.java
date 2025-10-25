package com.fiap.usu.repositories;

import com.fiap.usu.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    Optional<Address> findByStreetAndNumberAndCityAndStateAndZipCode(String street, String number, String city, String state,
                                                                     String zipCode);
}
