package com.will.givegreenville;

import com.will.givegreenville.storage.StorageProperties;
import com.will.givegreenville.storage.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class GiveGreenvilleApplication {

	public static void main(String[] args) {
		SpringApplication.run(GiveGreenvilleApplication.class, args);
	}

//	@Bean
//	CommandLineRunner init(StorageService storageService) {
//		return (args) -> {
////			storageService.deleteAll();
//			storageService.init();
//		};
//	}
}
