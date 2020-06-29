package com.vietle.pizzeria.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.WriteResultChecking;

// https://mongodb.github.io/mongo-java-driver/3.9/driver/getting-started/quick-start/
// https://www.concretepage.com/spring-5/spring-data-mongotemplate
@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration {
    @Value("${spring.data.mongodb.database}")
    private String databaseName;

    @Value("${spring.data.mongodb.host}")
    private String host;

    @Value("${spring.data.mongodb.port}")
    private int port;

    @Value("${spring.data.mongodb.username}")
    private String userName;

    @Value("${spring.data.mongodb.password}")
    private String password;

    @Value("${spring.data.mongodb.retryWrites}")
    private boolean retryWrites;

    @Override
    @Bean
    public MongoClient mongoClient() {
        StringBuilder sb = new StringBuilder();
        sb.append("mongodb://").append(userName).append(":").append(password).append("@")
                .append(host).append(":").append(port).append("/").append(databaseName).append("?")
                .append("retryWrites=").append(retryWrites);
        MongoClient client = MongoClients.create(sb.toString());
        return client;
    }

    @Override
    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        MongoTemplate template =  new MongoTemplate(mongoDbFactory(), mappingMongoConverter());
        template.setWriteResultChecking(WriteResultChecking.EXCEPTION);
        return template;
    }

    @Override
    protected String getDatabaseName() {
        return databaseName;
    }
}
