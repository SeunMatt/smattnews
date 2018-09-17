package com.smattnews.seeders;

import com.smattnews.config.Constants;
import org.apache.commons.lang3.RandomStringUtils;
import org.flywaydb.core.api.callback.Callback;
import org.flywaydb.core.api.callback.Context;
import org.flywaydb.core.api.callback.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by Seun Matt on 13-Aug-18
 */
@SuppressWarnings("ALL")
public class FlywayAwareDatabaseSeeder implements Callback {

    private Logger logger = LoggerFactory.getLogger(getClass());

    public FlywayAwareDatabaseSeeder() {}


    @Override
    public boolean supports(Event event, Context context) {
        return event.name().equals(Event.AFTER_MIGRATE.name());
    }

    @Override
    public boolean canHandleInTransaction(Event event, Context context) {
        return true;
    }

    @Override
    public void handle(Event event, Context context) {
        seedUsersTable(context.getConnection());
        seedAuthTokensTable(context.getConnection());
        seedPostsTable(context.getConnection());
    }


    /**
     * This function will seed the users table with
     * default users
     * @param connection
     */
    private void seedUsersTable(Connection connection) {

        try {

            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            statement.execute("SELECT * FROM users");
            ResultSet resultSet = statement.getResultSet();
            resultSet.last();

            if(resultSet.getRow() <= 0) {

                //it's empty run an insert query
                String sql = "INSERT INTO users (id_string, email, first_name, last_name, password, " +
                        "status, username) VALUES " +
                        "('" + RandomStringUtils.randomAlphanumeric(10).toUpperCase() + "', 'test@test.com', 'Seun', 'Matt', '" + new BCryptPasswordEncoder().encode("test123") + "', " +
                        "'" + Constants.AccountStatus.ACCOUNT_STATUS_ACTIVE + "', 'smatt');";

                logger.debug("SQL for Seeding Users: \n" + sql);

                statement.executeUpdate(sql);

            } else {
                logger.info("Default Users Seeded Already");
            }

        } catch (SQLException e) {
            logger.error("An exception occurred while seeding users: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private void seedAuthTokensTable(Connection connection) {

        try {

            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);

            statement.execute("SELECT * FROM users WHERE email = 'test@test.com'");
            ResultSet userResult =statement.getResultSet();
            userResult.last();
            if(userResult.getRow() <= 0) {
                //no users has been seeded so no need to see a token
                logger.debug("No Users have been seeded hence token seeding not neccessary");
                return;
            }
            int idUser = userResult.getInt(1);

            statement.execute("SELECT * FROM auth_tokens");
            ResultSet resultSet = statement.getResultSet();
            resultSet.last();

            if(resultSet.getRow() <= 0) {

                //it's empty run an insert query
                String sql = "INSERT INTO auth_tokens (id_string, id_user, token, expiry_date) VALUES " +
                        "('" + RandomStringUtils.randomAlphanumeric(10).toUpperCase() + "', '" + idUser + "', " +
                        "'" + RandomStringUtils.randomAlphanumeric(15).toUpperCase() + "', '"
                         + LocalDateTime.now().plusHours(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "');";

                logger.debug("SQL for Seeding auth tokens: \n" + sql);

                statement.executeUpdate(sql);

            } else {
                logger.info("Default Auth Token Seeded Already");
            }

        } catch (SQLException e) {
            logger.error("An exception occurred while seeding users: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private void seedPostsTable(Connection connection) {

        try {

            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            statement.execute("SELECT * FROM users WHERE email = 'test@test.com'");
            ResultSet userResult =statement.getResultSet();
            userResult.last();
            if(userResult.getRow() <= 0) {
                //no users has been seeded so no need to see a token
                logger.debug("No Users have been seeded hence posts seeding not neccessary");
                return;
            }
            int idUser = userResult.getInt(1);

            statement.execute("SELECT * FROM posts");
            ResultSet resultSet = statement.getResultSet();
            resultSet.last();

            if(resultSet.getRow() <= 0) {

                //it's empty run an insert query
                String sql = "INSERT INTO posts (id_string, id_user, post, title, url, tags) VALUES " +
                        "('" + RandomStringUtils.randomAlphanumeric(10).toUpperCase() + "', '" + idUser + "', " +
                        "'But I must explain to you how all this mistaken idea of denouncing pleasure and praising pain was born and I will give you a complete account of the system, and expound the actual teachings of the great explorer of the truth, the master-builder of human happiness. No one rejects, dislikes, or avoids pleasure itself, because it is pleasure, but because those who do not know how to pursue pleasure rationally encounter consequences that are extremely painful. Nor again is there anyone who loves or pursues or desires to obtain pain of itself, because it is pain, but because occasionally circumstances occur in which toil and pain can procure him some great pleasure. To take a trivial example, which of us ever undertakes laborious physical exercise, except to obtain some advantage from it? But who has any right to find fault with a man who chooses to enjoy a pleasure that has no annoying consequences, or one who avoids a pain that produces no resultant pleasure?'," +
                        "'SmattNews Company Launched!', 'smattnews-sample-one', 'SmattNews, Seun Matt, Java, Spring Boot')," +
                        "('" + RandomStringUtils.randomAlphanumeric(10).toUpperCase() + "', '" + idUser + "', " +
                        "'On the other hand, we denounce with righteous indignation and dislike men who are so beguiled and demoralized by the charms of pleasure of the moment, so blinded by desire, that they cannot foresee the pain and trouble that are bound to ensue; and equal blame belongs to those who fail in their duty through weakness of will, which is the same as saying through shrinking from toil and pain. These cases are perfectly simple and easy to distinguish. In a free hour, when our power of choice is untrammelled and when nothing prevents our being able to do what we like best, every pleasure is to be welcomed and every pain avoided. But in certain circumstances and owing to the claims of duty or the obligations of business it will frequently occur that pleasures have to be repudiated and annoyances accepted. The wise man therefore always holds in these matters to this principle of selection: he rejects pleasures to secure other greater pleasures, or else he endures pains to avoid worse pains'," +
                        "'SmattNews Company Launched2!', 'smattnews-sample-two', 'SmattNews, Seun Matt, Java, Spring Boot');";

                logger.debug("SQL for Seeding posts: \n" + sql);

                statement.executeUpdate(sql);

            } else {
                logger.info("Default Posts Seeded Already");
            }

        } catch (SQLException e) {
            logger.error("An exception occurred while seeding posts: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
