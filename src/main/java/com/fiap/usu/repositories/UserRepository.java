package com.fiap.usu.repositories;

import com.fiap.usu.dtos.user.UserPagedDto;
import com.fiap.usu.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    boolean existsByLogin(String email);

    boolean existsByDocument(String email);

    Optional<User> findByEmail(String email);

    @Query("""
                SELECT new com.fiap.usu.dtos.user.UserPagedDto(
                    u.id, u.name, u.email, u.login,
                    u.createdAt, u.updatedAt, u.status,
                    u.birthday, u.document
                )
                FROM User u
                WHERE (:name IS NULL OR LOWER(u.name) LIKE LOWER(CONCAT('%', :name, '%')))
            """)
    Page<UserPagedDto> getUsersPaged(Pageable pageable, @Param("name") String name);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.phones LEFT JOIN FETCH u.types LEFT JOIN FETCH u.addresses LEFT JOIN FETCH u.addresses.address WHERE u.id = :id")
    Optional<User> findByIdWithAssociations(@Param("id") Long id);

    @Query("SELECT u FROM User u WHERE u.email = :identifier OR u.login = :identifier")
    Optional<User> findByLoginOrEmail(@Param("identifier") String identifier);
}
