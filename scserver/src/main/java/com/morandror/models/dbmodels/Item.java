package com.morandror.models.dbmodels;

import com.morandror.models.BaseModel;
import com.morandror.models.dbmodels.enums.ItemType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "item")
public class Item extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    private String foundAt;

    @NotNull
    private byte[] image;

    @NotNull
    private Date recentlyUsed;

    @NotNull
    private int usage;

    @NotNull
    private String brand;

    @NotNull
    private String color;

    @NotNull
    private float size;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ItemType type;

    @NotNull
    private int closetId;
}