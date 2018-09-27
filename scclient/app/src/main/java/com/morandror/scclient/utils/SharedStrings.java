package com.morandror.scclient.utils;

import java.util.concurrent.TimeUnit;

public class SharedStrings {

    //server urls
    public static final String SERVER_IP = "192.168.1.6:8080";
    public static final String SERVER_URL = "http://" + SERVER_IP + "/scServer";
    public static final String USER_URL = SERVER_URL + "/user";
    public static final String GET_USER_URL = USER_URL + "/get";
    public static final String ADD_USER_URL = USER_URL + "/add";
    public static final String GET_USER_STATS = USER_URL + "/statistics/%s";
    public static final String ASSIGN_CLOSET_TO_USER_URL = USER_URL + "/AssignCloset/%s/%s";
    public static final String CLOSET_URL = SERVER_URL+ "/closet";
    public static final String ADD_CLOSET_URL = CLOSET_URL + "/add";
    public static final String GET_CLOSET_URL = CLOSET_URL + "/get/%s";
    public static final String DELETE_CLOSET_URL = CLOSET_URL + "/delete/%s";

    //constants
    public static final int REQUEST_TIMEOUT = ((int) TimeUnit.SECONDS.toMillis(8));

}
