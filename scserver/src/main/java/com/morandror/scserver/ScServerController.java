package com.morandror.scserver;

import com.morandror.models.EmailObj;
import com.morandror.models.Friend;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/scServer")
public class ScServerController {
    scManager manager;
    Logger logger = LogManager.getLogger(ScServerController.class);

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
    public @ResponseBody ResponseEntity<?> addUser(@RequestBody User newUser) {
        logger.info("Add user post request - Add user with email: " + newUser.getEmail());
        if (newUser.getId() == 0) {
            User addedUser =  manager.addUser(newUser);
            return new ResponseEntity<Object>(addedUser, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/user/get", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<?> getUserByEmail(@RequestBody EmailObj emailObj) {
        logger.info("Get user by email - post request. Search for user with email: " + emailObj.getEmail());
        User user = manager.getUserByEmail(emailObj.getEmail());
        if(user != null){
            return new ResponseEntity<Object>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/user/get/{id}", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<?> getUserByID(@PathVariable("id") int userID) {
        logger.info("Get user by id - post request. Search for user with id: " + userID);
        Optional<User> user = manager.getUserByID(userID);
        return user.<ResponseEntity<?>>map(user1 -> new ResponseEntity<>(user1, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @RequestMapping(value = "/closet/get/{id}", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<?> getCloset(@PathVariable("id") int closetID) {
        logger.info("Get closet GET request - got closet id: " + closetID);
        Optional<Closet> closet = manager.getCloset(closetID);
        if(closet.isPresent()){
            return new ResponseEntity<Object>(closet, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/closet/add/{userID}", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<?> addCloset(@RequestBody Closet newCloset, @PathVariable("userID") int userID) {
        logger.info("Add closet post request. Add the closet to user id: " + userID);
        if (newCloset.getId() == 0) {
            Closet closet =  manager.addCloset(newCloset);
            logger.info("Assign Closet id: " + closet.getId() + " to user id: " + userID);
            UserHasCloset userHasCloset = new UserHasCloset(userID, closet.getId());
            manager.assignClosetToUser(userHasCloset);
            return new ResponseEntity<Object>(closet, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/item/add/{closetID}", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<?> addItem(@RequestBody Item newItem, @PathVariable("closetID") int closetID) {
        logger.info("Add item post request");
        if (newItem.getId() == 0) {
            Closet closet = manager.addItem(newItem, closetID);
            return new ResponseEntity<Object>(closet, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/closet/statistics/{id}", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<?> getStatistics(@PathVariable("id") int closetID) {
        logger.info("Get closet statistics - calculate statistics for closet id: " + closetID);
        Statistics statistics = manager.getClosetStatistics(closetID);
        if(statistics != null){
            return new ResponseEntity<Object>(statistics, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/user/statistics/{id}", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<?> getStatisticsOfUser(@PathVariable("id") int userID) {
        logger.info("Get user statistics - calculate statistics for user id: " + userID);
        Statistics statistics = manager.getUserStatistics(userID);
        if(statistics != null){
            return new ResponseEntity<Object>(statistics, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/user/AssignCloset/{userId}/{closetId}", method = RequestMethod.GET)
    public void assignClosetToUser(@PathVariable("userId") int userId, @PathVariable("closetId") int closetID) {
        logger.info("Assign Closet id: " + closetID + " to user id: " + userId);
        UserHasCloset userHasCloset = new UserHasCloset(userId, closetID);
        manager.assignClosetToUser(userHasCloset);
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

    @RequestMapping(value = "/item/loan/{itemID}", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<?> loanItem(@RequestBody Friend friend, @PathVariable("itemID") int itemID) {
        logger.info("Loan item post request - loan item id " + itemID + "to friend with email " + friend.getEmail());
        Item updatedItem = manager.loanItem(itemID, friend);
        if(updatedItem != null){
            return new ResponseEntity<Object>(updatedItem, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/item/setback/{itemID}", method = RequestMethod.GET)
    public ResponseEntity<?> setItemBackInCloset(@PathVariable("itemID") int itemID) {
        logger.info("set item back in closet - get request. Got item id: " + itemID);
        return manager.setItemBackInCloset(itemID);
    }
}