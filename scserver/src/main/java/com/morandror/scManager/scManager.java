package com.morandror.scmanager;

import com.morandror.database.DBHandler;
import com.morandror.models.dbmodels.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

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

    public void addUser(User newUser) {
        dbHandler.addUser(newUser);
    }
}
