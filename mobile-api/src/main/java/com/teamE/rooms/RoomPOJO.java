package com.teamE.rooms;

import lombok.Data;

@Data
public class RoomPOJO {

    private int id;
    private int dsNumber;
    private String name;
    private String description;
    private Long configurationId;

}
