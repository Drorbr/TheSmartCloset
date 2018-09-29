package com.morandror.scclient.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.morandror.scclient.utils.JsonHandler;

import java.io.Serializable;

public abstract class BaseModel implements Serializable{
    public String toJson()/* throws JsonProcessingException*/ {
        /*ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(this);*/
        return JsonHandler.getInstance().toString(this);
    }
}
