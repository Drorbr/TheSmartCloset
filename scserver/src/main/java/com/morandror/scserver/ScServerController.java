package com.morandror.scserver;

import com.morandror.models.TokenID;
import com.morandror.models.dbmodels.Closet;
import com.morandror.models.dbmodels.User;
import com.morandror.scmanager.LoggingController;
import com.morandror.scmanager.scManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/scServer")
public class ScServerController {
    scManager manager;
    Logger logger = LogManager.getLogger(LoggingController.class);

    @Autowired
    public void setManager(scManager manager) {
        this.manager = manager;
    }

    @RequestMapping(value = "/alive", method = RequestMethod.GET)
    public String isAlive() {
        logger.info("is alive request");
        return manager.checkIfAlive();
    }

    @RequestMapping(value = "/checkdb", method = RequestMethod.GET)
    public List<User> checkDB() throws Exception {
        return manager.checkDB();
    }

    @RequestMapping(value = "/user/add", method = RequestMethod.POST)
    public User addUser(@RequestBody User newUser) {
        if (newUser.getId() == 0) {
            manager.addUser(newUser);
        }

        return manager.getUser(newUser.getTokenID());
    }

    @RequestMapping(value = "/user/get", method = RequestMethod.POST)
    public User getUser(@RequestBody TokenID token) {
        return manager.getUser(token.getToken());
    }

    @RequestMapping(value = "/closet/get/{id}", method = RequestMethod.GET)
    public Closet getCloset(@PathVariable("id") int closetID) {
        return manager.getCloset(closetID).get();
    }
}