package com.morandror.scserver;

import com.morandror.models.TokenID;
import com.morandror.models.dbmodels.Closet;
import com.morandror.models.dbmodels.Item;
import com.morandror.models.dbmodels.User;
import com.morandror.models.dbmodels.UserHasCloset;
import com.morandror.scmanager.LoggingController;
import com.morandror.scmanager.scManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
        logger.info("Add user post request");
        if (newUser.getId() == 0) {
            return manager.addUser(newUser);
        }

        return null;
    }

    @RequestMapping(value = "/user/get", method = RequestMethod.POST)
    public User getUser(@RequestBody TokenID token) {
        logger.info("Get user post request");
        return manager.getUser(token.getToken());
    }

    @RequestMapping(value = "/closet/get/{id}", method = RequestMethod.GET)
    public Closet getCloset(@PathVariable("id") int closetID) {
        logger.info("Get closet GET request");
        return manager.getCloset(closetID).get();
    }

    @RequestMapping(value = "/closet/add", method = RequestMethod.POST)
    public Closet addCloset(@RequestBody Closet newCloset) {
        logger.info("Add closet post request");
        if (newCloset.getId() == 0) {
            return manager.addCloset(newCloset);
        }

        return null;
    }

    @RequestMapping(value = "/user/AssignCloset/{userId}/{closetId}", method = RequestMethod.GET)
    public void assignClosetToUser(@PathVariable("userId") int userId, @PathVariable("closetId") int closetID) {
        UserHasCloset userHasCloset = new UserHasCloset(userId, closetID);
        manager.assignClosetToUser(userHasCloset);
    }

    @RequestMapping(value = "/item/add/{closetID}", method = RequestMethod.POST)
    public void addItem(@RequestBody Item newItem, @PathVariable("closetID") int closetID) {
        logger.info("Add item post request");
        if(newItem.getId() == 0){
            manager.addItem(newItem, closetID);
        }
    }
}