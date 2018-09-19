package com.morandror.database.repositories;

import com.morandror.models.dbmodels.UserHasCloset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserHasClosetRepository extends JpaRepository<UserHasCloset, Integer> {
}
