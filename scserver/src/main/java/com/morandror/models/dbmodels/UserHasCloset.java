package com.morandror.models.dbmodels;


import com.morandror.models.BaseModel;
import com.morandror.models.UserClosetID;

import javax.persistence.*;

@Entity
@IdClass(UserClosetID.class)
@Table(name = "user_has_closet")
public class UserHasCloset extends BaseModel{

    @Id
    @Column(name = "closet_id")
    private int closet_id;

    @Id
    @Column(name = "user_id")
    private int user_id;

    public UserHasCloset() {
    }

    public UserHasCloset(int user_id, int closet_id) {
        this.closet_id = closet_id;
        this.user_id = user_id;
    }

    public int getCloset_id() {
        return closet_id;
    }

    public void setCloset_id(int closet_id) {
        this.closet_id = closet_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
