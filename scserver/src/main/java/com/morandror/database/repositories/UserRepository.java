package com.morandror.database.repositories;


import com.morandror.models.dbmodels.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

/*    @Query(value = "SELECT * FROM User u WHERE u.token = ?1", nativeQuery = true)
    User findByTokenID(String token);*/

    @Query(value = "SELECT * FROM User u WHERE u.email = ?1", nativeQuery = true)
    User findByEmail(String email);
}
