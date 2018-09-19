package com.morandror.models;

import java.io.Serializable;

public class UseClosetID implements Serializable {

    private int closet_id;
    private int user_id;

    public UseClosetID() {
    }

    public UseClosetID(int closet_id, int user_id) {
        this.closet_id = closet_id;
        this.user_id = user_id;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
