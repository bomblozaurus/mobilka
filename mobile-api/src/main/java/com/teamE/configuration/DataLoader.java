package com.teamE.configuration;

import com.teamE.rooms.RoomConfiguration;
import com.teamE.rooms.RoomConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {
    private RoomConfigurationRepository roomConfigurationRepository;

    @Autowired
    public DataLoader(RoomConfigurationRepository roomConfigurationRepository) {
        this.roomConfigurationRepository = roomConfigurationRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        roomConfigurationRepository.save(RoomConfiguration.getDefaultConfiguration());
    }
}
