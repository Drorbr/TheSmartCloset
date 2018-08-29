package com.morandror.database.repositories;

import com.morandror.models.dbmodels.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Integer> {
}
