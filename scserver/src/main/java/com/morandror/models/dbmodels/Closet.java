package com.morandror.models.dbmodels;

import com.morandror.models.BaseModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "closet")
public class Closet extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    private String name;

    @NotNull
    private byte[] image;

    @NotNull
    private String rfidScanner;

    @NotNull
    private String description;

    @NotNull
    private String location;

    @NotNull
    private Date insertDate;
}
