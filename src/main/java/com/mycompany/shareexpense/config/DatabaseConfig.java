package com.mycompany.shareexpense.config;

import com.mongodb.Mongo;
import com.mongodb.WriteConcern;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.authentication.UserCredentials;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;


/**
 * DOCUMENT ME!
 *
 * @author Muthukumaran Swaminathan
 * @version $Revision$
 */
@Configuration
@EnableMongoRepositories(basePackages = {
        "com.mycompany.shareexpense.repository"}
)
public class DatabaseConfig {
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     * @throws Exception DOCUMENT ME!
     */
    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        String openshiftMongoDbHost = System.getenv("OPENSHIFT_MONGODB_DB_HOST");

        //String openshiftMongoDbHost = "localhost";
        int openshiftMongoDbPort = Integer.parseInt(System.getenv("OPENSHIFT_MONGODB_DB_PORT"));

        //int openshiftMongoDbPort = Integer.parseInt("27017");
        String username = System.getenv("OPENSHIFT_MONGODB_DB_USERNAME");

        //String username = "accountUser";
        String password = System.getenv("OPENSHIFT_MONGODB_DB_PASSWORD");

        //String password = "password";
        Mongo mongo = new Mongo(openshiftMongoDbHost, openshiftMongoDbPort);

        UserCredentials userCredentials = new UserCredentials(username, password);

        String databaseName = System.getenv("OPENSHIFT_APP_NAME");

        //String databaseName = "mydb";
        MongoDbFactory mongoDbFactory = new SimpleMongoDbFactory(mongo, databaseName, userCredentials);

        MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory);

        mongoTemplate.setWriteConcern(WriteConcern.SAFE);

        return mongoTemplate;
    }
}
