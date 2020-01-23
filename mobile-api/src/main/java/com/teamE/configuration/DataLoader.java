package com.teamE.configuration;

import com.teamE.rooms.RoomConfiguration;
import com.teamE.rooms.RoomConfigurationRepository;
import com.teamE.users.UserRole;
import com.teamE.users.UserRoleRepository;
import com.teamE.users.UserRoleType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {
    private RoomConfigurationRepository roomConfigurationRepository;
    private UserRoleRepository userRoleRepository;

    @Autowired
    public DataLoader(RoomConfigurationRepository roomConfigurationRepository, UserRoleRepository userRoleRepository) {
        this.roomConfigurationRepository = roomConfigurationRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public void run(String... args) {
        _loadDeafultRoomConfiguration();
        _loadKeyholderRole();
    }

    private void _loadKeyholderRole() {
        UserRoleType keyholderRole = UserRoleType.KEYHOLDER;
        UserRole role = userRoleRepository.getByType(keyholderRole);
        if (role == null) {
            userRoleRepository.save(new UserRole(keyholderRole));
        }
    }

    private void _loadDeafultRoomConfiguration() {
        roomConfigurationRepository.save(RoomConfiguration.getDefaultConfiguration());
    }
}
