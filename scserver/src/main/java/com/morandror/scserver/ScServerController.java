package com.morandror.scserver;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.morandror.database.DBHandler;
import com.morandror.scmanager.LoggingController;
import com.morandror.scmanager.scManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.sql.ResultSet;

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
    public String checkDB() throws Exception {
        logger.info("check DB request");
        ResultSet res = DBHandler.getInstance().executeQuery("SELECT * FROM  testtable");
        return convertToJSON(res).toString();
    }

    private JsonArray convertToJSON(ResultSet resultSet)
            throws Exception {
        JsonArray jsonArray = new JsonArray();
        while (resultSet.next()) {
            int total_rows = resultSet.getMetaData().getColumnCount();
            for (int i = 0; i < total_rows; i++) {
                JsonObject obj = new JsonObject();
                obj.addProperty(resultSet.getMetaData().getColumnLabel(i + 1), resultSet.getObject(i + 1).toString());
                jsonArray.add(obj);
            }
        }
        return jsonArray;
    }
}