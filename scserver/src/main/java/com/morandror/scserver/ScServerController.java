package com.morandror.scserver;

import com.morandror.models.EmailObj;
import com.morandror.models.Statistics;
import com.morandror.models.dbmodels.Closet;
import com.morandror.models.dbmodels.Item;
import com.morandror.models.dbmodels.User;
import com.morandror.models.dbmodels.UserHasCloset;
import com.morandror.scmanager.LoggingController;
import com.morandror.scmanager.scManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public User getUser(@RequestBody EmailObj emailObj) {
        logger.info("Get user by email - post request. Search for user with id: " + emailObj.getEmail());
        return manager.getUser(emailObj.getEmail());
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

    @RequestMapping(value = "/closet/statistics/{id}", method = RequestMethod.GET)
    public Statistics getStatistics(@PathVariable("id") int closetID) {
        logger.info("Get closet statistics - get request");
        return manager.getClosetStatistics(closetID);
    }

    @RequestMapping(value = "/user/AssignCloset/{userId}/{closetId}", method = RequestMethod.GET)
    public void assignClosetToUser(@PathVariable("userId") int userId, @PathVariable("closetId") int closetID) {
        UserHasCloset userHasCloset = new UserHasCloset(userId, closetID);
        manager.assignClosetToUser(userHasCloset);
    }

    @RequestMapping(value = "/item/add/{closetID}", method = RequestMethod.POST)
    public void addItem(@RequestBody Item newItem, @PathVariable("closetID") int closetID) {
        logger.info("Add item post request");
        if (newItem.getId() == 0) {
            manager.addItem(newItem, closetID);
        }
    }

    @RequestMapping(value = "/item/delete/{itemID}", method = RequestMethod.GET)
    public ResponseEntity<?> deleteItem(@PathVariable("itemID") int itemID) {
        logger.info("delete item post request - delete item id " + itemID);
        return manager.deleteItem(itemID);
    }

    @RequestMapping(value = "/closet/delete/{closetID}", method = RequestMethod.GET)
    public ResponseEntity<?> deleteCloset(@PathVariable("closetID") int closetID) {
        logger.info("delete closet post request - delete closet id " + closetID);
        return manager.deleteCloset(closetID);
    }

    @RequestMapping(value = "/user/statistics/{id}", method = RequestMethod.GET)
    public Statistics getStatisticsOfUser(@PathVariable("id") int userID) {
        logger.info("Get user statistics - get request");
        return manager.getUserStatistics(userID);
    }
}