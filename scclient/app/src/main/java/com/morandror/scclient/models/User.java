package com.morandror.scclient.models;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.Set;

public class User extends BaseModel{
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String gender;
    private Set<Closet> closets;
    private String tokenID;

    public User(String firstName, String lastName, String email, String gender) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.gender = gender;
    }

    public User(GoogleSignInAccount account){
        this.email = account.getEmail();
        this.firstName = account.getGivenName();
        this.lastName = account.getFamilyName();
        this.tokenID = account.getIdToken();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Set<Closet> getClosets() {
        return closets;
    }

    public void setClosets(Set<Closet> closets) {
        this.closets = closets;
    }

    public String getTokenID() {
        return tokenID;
    }

    public void setTokenID(String tokenID) {
        this.tokenID = tokenID;
    }
}
