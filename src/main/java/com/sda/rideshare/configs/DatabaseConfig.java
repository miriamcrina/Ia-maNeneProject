package com.sda.rideshare.configs;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan("com.sda.rideshare.entities")
@EnableJpaRepositories("com.sda.rideshare.repositories")
public class DatabaseConfig {
}
