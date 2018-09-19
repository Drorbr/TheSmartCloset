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

    public User getUser(String token) {
        logger.info("Database  - Get user by token ID");
        return userRepository.findByTokenID(token);
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

    public Item addItem(Item newItem) {
        logger.info("Database  - Add new item");
        return itemRepository.saveAndFlush(newItem);
    }
}
