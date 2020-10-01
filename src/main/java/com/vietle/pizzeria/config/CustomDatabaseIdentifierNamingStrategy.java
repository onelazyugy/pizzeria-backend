package com.vietle.pizzeria.config;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy;

import java.io.Serializable;

/**
 * A custom database table and columns name strategy. The table name is lowercase and column is uppercase
 * good explaination: https://stackoverflow.com/questions/39162976/hibernate-naming-strategy-changing-table-names
 */
public class CustomDatabaseIdentifierNamingStrategy extends SpringPhysicalNamingStrategy implements Serializable {

    public static final long serialVersionUID = 1L;
    public static final CustomDatabaseIdentifierNamingStrategy INSTANCE = new CustomDatabaseIdentifierNamingStrategy();

    /**
     * lowercase the table name so that it would match with the database table
     * this is define in the entity class: ex: @Table(name = "user", schema = "pizzeria")
     * @param name
     * @param context
     * @return
     */
    @Override
    public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment context) {
        return new Identifier(name.getText().toLowerCase(), true);
    }

    /**
     * uppercase the column name so that it would match with the database table column naming whether it is lowercase or uppercase
     * this is define in the entity clasee: ex: @Column(name = "PASSWORD")
     * @param name
     * @param context
     * @return
     */
    @Override
    public Identifier toPhysicalColumnName(Identifier name, JdbcEnvironment context) {
        // uppercase column names
        return new Identifier(name.getText().toUpperCase(), true);
    }

}