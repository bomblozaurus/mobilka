package com.teamE;

import com.teamE.fileUpload.property.FileStorageProperties;
import com.teamE.rooms.RoomWithConfigurationProjectionProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@Configuration
@EnableConfigurationProperties({
        FileStorageProperties.class
})
public class MobileApiApplication {

    @Bean
    RoomWithConfigurationProjectionProcessor roomProcessor() {
		return new RoomWithConfigurationProjectionProcessor();
	}

    public static void main(String[] args) {
        SpringApplication.run(MobileApiApplication.class, args);
    }

}
