package com.morandror.scManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class scManager {
    Logger logger = LogManager.getLogger(LoggingController.class);

    public String checkIfAlive() {
        logger.info("Got is alive request");
        return "Alive!";
    }
}
