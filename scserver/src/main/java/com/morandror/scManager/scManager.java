package com.morandror.scmanager;

import com.morandror.database.DBHandler;
import com.morandror.models.Statistics;
import com.morandror.models.dbmodels.Closet;
import com.morandror.models.dbmodels.Item;
import com.morandror.models.dbmodels.User;
import com.morandror.models.dbmodels.UserHasCloset;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class scManager {
    Logger logger = LogManager.getLogger(LoggingController.class);

    @Autowired
    DBHandler dbHandler;


    public String checkIfAlive() {
        logger.info("Got is alive request");
        return "Alive!";
    }

    public List<User> checkDB() {
        return dbHandler.getAllUsers();
    }

    public User addUser(User newUser) {
        return dbHandler.addUser(newUser);
    }

    public User getUser(String emailObj) {
        return dbHandler.getUser(emailObj);
    }

    public Optional<Closet> getCloset(int closetID) {
        return dbHandler.getCloset(closetID);
    }

    public Closet addCloset(Closet newCloset) {
        return dbHandler.addCloset(newCloset);
    }

    public void assignClosetToUser(UserHasCloset userHasCloset) {
        dbHandler.assignClosetToUser(userHasCloset);
    }

    public void addItem(Item newItem, int closetID) {
        dbHandler.addItem(newItem, closetID);
    }

    public Statistics getClosetStatistics(int closetID) {
        String color = dbHandler.getFavoriteColor(closetID);
        Item favoriteItem = dbHandler.getFavoriteItem(closetID);
        List<Item> lastDays = dbHandler.getLast7DaysItems(closetID);
        List<Item> newestItems = dbHandler.getNewestItems(closetID);

        return new Statistics(favoriteItem, color, lastDays, newestItems);
    }

    public ResponseEntity<?> deleteItem(int itemID) {
        return dbHandler.deleteItem(itemID);
    }

    public ResponseEntity<?> deleteCloset(int closetID) {
        return dbHandler.deleteCloset(closetID);
    }

    public Statistics getUserStatistics(int userID) {
        String color = dbHandler.getUserFavoriteColor(userID);
        Item favoriteItem = dbHandler.getUserFavoriteItem(userID);
        List<Item> lastDays = dbHandler.getUserLast7DaysItems(userID);
        List<Item> newestItems = dbHandler.getUserNewestItems(userID);

        return new Statistics(favoriteItem, color, lastDays, newestItems);
    }
}
