package com.teamE.rooms;

import com.teamE.common.Transformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class RoomPOJOToRoomTransformer implements Transformer<RoomPOJO, Room> {
    RoomConfigurationRepository roomConfigurationRepository;

    @Autowired
    public RoomPOJOToRoomTransformer(RoomConfigurationRepository roomConfigurationRepository) {
        this.roomConfigurationRepository = roomConfigurationRepository;
    }

    @Override
    public Room transform(RoomPOJO pojo) {
        return Room.builder()
                .description(pojo.getDescription())
                .dsNumber(pojo.getDsNumber())
                .name(pojo.getName())
                .mainImage(pojo.getMainImage())
                .configuration(this.getConfiguration(pojo.getConfigurationId()))
                .build();
    }

    private RoomConfiguration getConfiguration(Long configurationId) {
        if (Objects.equals(null, configurationId)) {
            return RoomConfiguration.getDefaultConfiguration();
        } else {
            return roomConfigurationRepository.findById(configurationId).orElseThrow(ResourceNotFoundException::new);
        }
    }
}
