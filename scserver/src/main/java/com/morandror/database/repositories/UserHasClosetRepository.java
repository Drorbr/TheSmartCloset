package com.morandror.database.repositories;

import com.morandror.models.dbmodels.UserHasCloset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserHasClosetRepository extends JpaRepository<UserHasCloset, Integer> {

    @Modifying
    @Transactional
    @Query(value = "delete from user_has_closet where closet_id = ?1", nativeQuery = true)
    void deleteByClosetID(int closetID);
}
