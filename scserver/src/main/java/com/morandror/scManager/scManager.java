package com.morandror.scmanager;

import com.morandror.database.DBHandler;
import com.morandror.models.dbmodels.Closet;
import com.morandror.models.dbmodels.Item;
import com.morandror.models.dbmodels.User;
import com.morandror.models.dbmodels.UserHasCloset;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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

    public User getUser(String token) {
        return dbHandler.getUser(token);
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

    public Item addItem(Item newItem) {
        return dbHandler.addItem(newItem);
    }
}
