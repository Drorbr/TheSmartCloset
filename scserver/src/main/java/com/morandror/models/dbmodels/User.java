package com.morandror.models.dbmodels;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.morandror.models.BaseModel;
import com.morandror.models.dbmodels.enums.Gender;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/*@Getter
@Setter*/
@Entity
@Table(name = "user")
public class User extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int id;

    @NotNull
    @Column(name = "first_name")
    private String firstName;

    @NotNull
    @Column(name = "last_name")
    private String lastName;

    @NotNull
    @Column(name = "email")
    private String email;

    //@Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "gender")
    private String gender;


    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "user_has_closet",
            joinColumns = { @JoinColumn(name = "user_id") },
            inverseJoinColumns = { @JoinColumn(name = "closet_id") }
    )
    private Set<Closet> closets = new HashSet<>();


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
}
