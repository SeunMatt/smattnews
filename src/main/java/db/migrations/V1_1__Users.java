/*
 * Copyright (c) Seun Matt (smatt382@gmail.com) 2018
 */

package db.migrations;

import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Created by Seun Matt on 15-Sep-18
 */
public class V1_1__Users implements SpringJdbcMigration {

    @Override
    public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
        String sql = "CREATE TABLE IF NOT EXISTS users (" +
                "  id INT NOT NULL AUTO_INCREMENT, " +
                "  id_string varchar(50) NOT NULL, " +
                "  email varchar(255) NOT NULL, " +
                "  first_name varchar(255) NOT NULL, " +
                "  last_name varchar(255) NOT NULL, " +
                "  middle_name varchar(255) DEFAULT NULL, " +
                "  password varchar(255) NOT NULL, " +
                "  status INT NOT NULL, " +
                "  username varchar(255) NOT NULL, " +
                "  bio varchar(255) DEFAULT NULL, " +
                "  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP , " +
                "  abolished BOOLEAN DEFAULT FALSE, " +
                "  abolished_at TIMESTAMP DEFAULT NULL, " +
                "  PRIMARY KEY (id), " +
                "  UNIQUE (email), " +
                "  UNIQUE (id_string), " +
                "  UNIQUE (username)\n" +
                ")";
        jdbcTemplate.execute(sql);
    }
}
