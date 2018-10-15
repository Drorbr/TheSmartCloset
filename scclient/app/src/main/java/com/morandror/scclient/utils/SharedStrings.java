package com.morandror.scclient.utils;

import java.util.concurrent.TimeUnit;

public class SharedStrings {

    //server urls
    public static final String SERVER_IP = "192.168.1.8:8080";
    public static final String SERVER_URL = "http://" + SERVER_IP + "/scServer";

    public static final String USER_URL = SERVER_URL + "/user";
    public static final String GET_USER_BY_EMAIL_URL = USER_URL + "/get";
    public static final String GET_USER_BY_ID_URL = USER_URL + "/get/%s";
    public static final String ADD_USER_URL = USER_URL + "/add";
    public static final String GET_USER_STATS = USER_URL + "/statistics/%s";
    public static final String ASSIGN_CLOSET_TO_USER_URL = USER_URL + "/AssignCloset/%s/%s";

    public static final String CLOSET_URL = SERVER_URL+ "/closet";
    public static final String ADD_CLOSET_AND_ASSIGN_URL = CLOSET_URL + "/add/%s";
    public static final String GET_CLOSET_URL = CLOSET_URL + "/get/%s";
    public static final String GET_CLOSET_STATS_URL = CLOSET_URL + "/statistics/%s";
    public static final String DELETE_CLOSET_URL = CLOSET_URL + "/delete/%s";

    public static final String ITEM_URL = SERVER_URL + "/item";
    public static final String DELETE_ITEM_URL = ITEM_URL + "/delete/%s";
    public static final String ADD_ITEM_URL = ITEM_URL + "/add/%s";
    public static final String LOAN_ITEM_URL = ITEM_URL + "/loan/%s";
    public static final String BACK_ITEM_URL = ITEM_URL + "/setback/%s";

    //constants
    public static final int REQUEST_TIMEOUT = ((int) TimeUnit.SECONDS.toMillis(8));
    public static final String GET_USER_KEY = "email";

    //Http responses
    public static final int RESPONSE_USER_NO_EXIST = 400;
}
