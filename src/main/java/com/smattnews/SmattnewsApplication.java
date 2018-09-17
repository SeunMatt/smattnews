package com.smattnews;

import com.smattnews.config.ApplicationProperties;
import com.smattnews.seeders.FlywayAwareDatabaseSeeder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;

/**
 * Created by Seun Matt on 15-Sep-18
 */
@SpringBootApplication
public class SmattnewsApplication {

    private Logger logger = LoggerFactory.getLogger(getClass());

    public static void main(String... args) {
        SpringApplication.run(SmattnewsApplication.class, args);
    }


    @Bean
    public FlywayMigrationStrategy flywayMigrationStrategy(ApplicationProperties properties) {
        return (flyway) -> {
            flyway.setCallbacks(new FlywayAwareDatabaseSeeder());
            //clean and run migration programmatically only if not on local
            if (!properties.getEnv().equalsIgnoreCase("LOCAL")) {
                flyway.clean();
                flyway.migrate();
            }
        };
    }

}
