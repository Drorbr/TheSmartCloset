package com.morandror.database;

import com.morandror.database.repositories.ClosetRepository;
import com.morandror.database.repositories.ItemRepository;
import com.morandror.database.repositories.UserRepository;
import com.morandror.models.dbmodels.User;
import com.morandror.scmanager.LoggingController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

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

    public User getUser(int id) {
        return userRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " was not found"));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
