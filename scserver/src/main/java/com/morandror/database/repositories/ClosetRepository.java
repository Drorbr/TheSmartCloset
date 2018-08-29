package com.morandror.database.repositories;

import com.morandror.models.dbmodels.Closet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClosetRepository extends JpaRepository<Closet, Integer> {
}
