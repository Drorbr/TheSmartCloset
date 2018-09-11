package com.morandror.database;

import com.morandror.database.repositories.ClosetRepository;
import com.morandror.database.repositories.ItemRepository;
import com.morandror.database.repositories.UserRepository;
import com.morandror.models.dbmodels.Closet;
import com.morandror.models.dbmodels.User;
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

    private DBHandler() {
        logger.info("Database connected!");
    }

    public User getUser(String token) {
        return userRepository.findByTokenID(token);
//        return userRepository.findById(id).
//                orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " was not found"));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void addUser(User newUser) {
        userRepository.save(newUser);
    }

    public Optional<Closet> getCloset(int closetID) {
        return closetRepository.findById(closetID);
    }
}
