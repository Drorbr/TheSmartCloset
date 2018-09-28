package com.morandror.database;

import com.morandror.database.repositories.ClosetRepository;
import com.morandror.database.repositories.ItemRepository;
import com.morandror.database.repositories.UserHasClosetRepository;
import com.morandror.database.repositories.UserRepository;
import com.morandror.models.dbmodels.Closet;
import com.morandror.models.dbmodels.Item;
import com.morandror.models.dbmodels.User;
import com.morandror.models.dbmodels.UserHasCloset;
import com.morandror.scmanager.LoggingController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class DBHandler {
    Logger logger = LogManager.getLogger(LoggingController.class);

    @Autowired
    private ClosetRepository closetRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserHasClosetRepository userHasClosetRepository;

    private DBHandler() {
        logger.info("Database connected!");
    }

    public User getUser(String email) {
        User user = userRepository.findByEmail(email);
        if(user != null)
            logger.info("Database  - Got user id: " + user.getId() + " with email: " + email);
        else
            logger.info("Database - User with email: " + email + "does not exist");
        return user;
    }

    public List<User> getAllUsers() {
        logger.info("Database  - Get all users");
        return userRepository.findAll();
    }

    public User addUser(User newUser) {
        logger.info("Database  - Add new user");
        return userRepository.saveAndFlush(newUser);
    }

    public Optional<Closet> getCloset(int closetID) {
        logger.info("Database  - Get closet by ID");
        return closetRepository.findById(closetID);
    }

    public Closet addCloset(Closet newCloset) {
        logger.info("Database  - Add closet");
        return closetRepository.saveAndFlush(newCloset);
    }

    public void assignClosetToUser(UserHasCloset userHasCloset) {
        logger.info("Database  - Assign user to closet - user_has_closet table");
        userHasClosetRepository.saveAndFlush(userHasCloset);
    }

    public void addItem(Item newItem, int closetID) {
        logger.info("Database  - Add new item");
        Optional<Closet> closet = getCloset(closetID);
        if(closet.isPresent()){
            newItem.setCloset(closet.get());
            closet.get().getItems().add(newItem);
            closetRepository.saveAndFlush(closet.get());
        }

        //return itemRepository.saveAndFlush(newItem);
    }

    public String getFavoriteColor(int closetID) {
        String res = itemRepository.findMostFavoriteColor(closetID);
        logger.info("Database  - The favorite color in closet id " + closetID + " is " + res);
        return res;
    }

    public Item getFavoriteItem(int closetID) {
        Item item = itemRepository.findMostFavoriteItem(closetID);
        logger.info("Database  - The favorite item in closet id " + closetID + " is item.id = " + item.getId());
        return item;
    }

    public List<Item> getLast7DaysItems(int closetID) {
        List<Item> list =  itemRepository.getLastUsed(closetID);
        logger.info("Database - found " + list.size() + " item(s) in closet id: " + closetID + " that used in the last 7 days");
        return list;
    }

    public List<Item> getNewestItems(int closetID) {
        List<Item> list = itemRepository.getRecentlyAdded(closetID);
        logger.info("Database - found " + list.size() + " item(s) in closet id: " + closetID + " that added to the closet in the last 7 days");
        return list;
    }

    public ResponseEntity<?> deleteItem(int itemID) {
        return itemRepository.findById(itemID).map(item -> {
            itemRepository.delete(item);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("Item ID " + itemID + " not found"));
    }


    public ResponseEntity<?> deleteCloset(int closetID) {
        userHasClosetRepository.deleteByClosetID(closetID);
        logger.info("Database - deleted from user_has_closet all rows with closet_id " + closetID);
        return closetRepository.findById(closetID).map(closet -> {
            closetRepository.delete(closet);
            logger.info("Database - deleted closet id " + closetID + " from 'closet'");
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("Closet ID " + closetID + " not found"));
    }

    public String getUserFavoriteColor(int userID) {
        String color = itemRepository.findUserMostFavoriteColor(userID);
        logger.info("Database  - The favorite color of user id: " + userID + " is " + color);
        return color;
    }

    public Item getUserFavoriteItem(int userID) {
        Item item = itemRepository.findUserMostFavoriteItem(userID);
        logger.info("Database  - The favorite item of user id: " + userID + " is item.id = " + item.getId());
        return item;
    }

    public List<Item> getUserLast7DaysItems(int userID) {
        List<Item> list =  itemRepository.getUserLastUsed(userID);
        logger.info("Database - found " + list.size() + " item(s) of user id: " + userID + " that used in the last 7 days");
        return list;
    }

    public List<Item> getUserNewestItems(int userID) {
        List<Item> list = itemRepository.getUserRecentlyAdded(userID);
        logger.info("Database - found " + list.size() + " item(s) of user id: " + userID + " that added to the closet in the last 7 days");
        return list;
    }
}
