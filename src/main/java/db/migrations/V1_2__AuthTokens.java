/*
 * Copyright (c) Seun Matt (smatt382@gmail.com) 2018
 * Created on 15/9/2018
 */

package db.migrations;

import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Created by Seun Matt on 15-Sep-18
 */
public class V1_2__AuthTokens implements SpringJdbcMigration {

    @Override
    public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
        String sql = "CREATE TABLE IF NOT EXISTS auth_tokens (" +
                " id INT NOT NULL AUTO_INCREMENT, " +
                " id_string varchar(50) NOT NULL, " +
                " id_user INT NOT NULL, " +
                " token VARCHAR(255) NOT NULL," +
                " expiry_date TIMESTAMP NOT NULL, " +
                " created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                " updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, " +
                " abolished BOOLEAN DEFAULT FALSE, " +
                " abolished_at TIMESTAMP DEFAULT NULL, " +
                " PRIMARY KEY (id), " +
                " UNIQUE (id_string), " +
                " UNIQUE (token), " +
                " UNIQUE (token, id_user)" +
                ");";
        jdbcTemplate.execute(sql);
    }
}
