package com.teamE.rooms;

import lombok.Data;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.Entity;

@Data
@Entity
public class Room {


    public Room(int dsNumber, String name) {

        this.dsNumber = dsNumber;
        this.name = name;
    }

    public Room() {

    }

    @Id
    private int id;
    @Column(nullable = false)
    private int dsNumber;

    @Column(nullable = false)
    private String name;

}
