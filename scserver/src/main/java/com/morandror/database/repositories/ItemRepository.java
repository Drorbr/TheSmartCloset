package com.morandror.database.repositories;

import com.morandror.models.dbmodels.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {


    @Query(value = "select color " +
            "from Item i " +
            "where i.closet_id = ?1 " +
            "group by i.color " +
            "order by count(i.item_id) desc limit 1",
            nativeQuery = true)
    String findMostFavoriteColor(int closetID);

    @Query(value = "select * " +
            "from Item i " +
            "where i.closet_id = ?1 " +
            "order by i.usage_number desc limit 1",
            nativeQuery = true)
    Item findMostFavoriteItem(int closetID);

    @Query(value = "select * from Item i where i.closet_id = ?1 and (i.recently_used >= DATE(NOW()) - INTERVAL 7 DAY)", nativeQuery = true)
    List<Item> getLastUsed(int closetID);

    @Query(value = "select * from Item i where i.closet_id = ?1 and (i.insert_date >= DATE(NOW()) - INTERVAL 7 DAY)", nativeQuery = true)
    List<Item> getRecentlyAdded(int closetID);
}
