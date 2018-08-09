package com.morandror.scserver;

import com.morandror.scManager.LoggingController;
import com.morandror.scManager.scManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
    public String isAlive(){
        logger.info("is alive request");
        return manager.checkIfAlive();
    }
}