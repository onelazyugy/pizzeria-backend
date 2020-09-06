package com.vietle.pizzeria.repo;

import com.vietle.pizzeria.domain.Role;
import com.vietle.pizzeria.domain.User;
import com.vietle.pizzeria.util.PizzeriaUtil;

public class QueryBuilder {
    private static String DOUBLE_QUOTE = "\"";
    private static String SINGLE_QOUTE = "'";
    private static char SEMICOLON = ';';
    private static char COMMA = ',';
    private static char OPEN_PARAN = '(';
    private static char CLOSE_PARAN = ')';
    private QueryBuilder(){}

    /**
     * SELECT * FROM pizzeria."user" WHERE "EMAIL"='xxx@mail.com' AND "PASSWORD"='xxx';
     * @param user
     * @return
     */
    public static String buildRetrieveUserQuery(User user) {
        StringBuilder sb = new StringBuilder();
        String rawPassword = user.getPassword();
        String hashedPassword = PizzeriaUtil.hash(rawPassword.trim());
        sb.append("SELECT * FROM pizzeria.").append(DOUBLE_QUOTE).append("user").append(DOUBLE_QUOTE);
        sb.append(" WHERE ").append(DOUBLE_QUOTE).append("EMAIL").append(DOUBLE_QUOTE).append("=");
        sb.append(SINGLE_QOUTE).append(user.getEmail()).append(SINGLE_QOUTE);
        sb.append(" AND ").append(DOUBLE_QUOTE).append("PASSWORD").append(DOUBLE_QUOTE).append("=");
        sb.append(SINGLE_QOUTE).append(hashedPassword).append(SINGLE_QOUTE).append(SEMICOLON);
        return sb.toString();
    }


    /**
     * SELECT * FROM pizzeria."user" WHERE "EMAIL"='xxx@gmail.com';
     * @param email
     * @return
     */
    public static String buildRetrieveUserQuery(String email) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM pizzeria.").append(DOUBLE_QUOTE).append("user").append(DOUBLE_QUOTE);
        sb.append(" WHERE ").append(DOUBLE_QUOTE).append("EMAIL").append(DOUBLE_QUOTE).append("=");
        sb.append(SINGLE_QOUTE).append(email).append(SINGLE_QOUTE).append(SEMICOLON);
        return sb.toString();
    }

    /**
     * INSERT INTO pizzeria."user" ("EMAIL", "PASSWORD", "ROLES", "NICK_NAME", "CREATED_TIMESTAMP")
     * values('tester@gmail.com', 'some hashed password', '{"user"}', 'tester', now());
     * @param user
     * @return
     */
    public static String buildSaveUserQuery(User user) {
        String email = SINGLE_QOUTE + user.getEmail().trim() + SINGLE_QOUTE + COMMA;
        String hashedPassword = SINGLE_QOUTE + user.getPassword() + SINGLE_QOUTE + COMMA;
        //everyone who register, by default their role will be "ROLE_USER", need an admin page to update their role to admin or whatever later
        String roles = SINGLE_QOUTE + "{"+DOUBLE_QUOTE + Role.ROLE_USER + DOUBLE_QUOTE + "}" + SINGLE_QOUTE + COMMA;
        String nickname = SINGLE_QOUTE + user.getNickName() + SINGLE_QOUTE + COMMA;

        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO pizzeria.").append(DOUBLE_QUOTE).append("user").append (DOUBLE_QUOTE).append(OPEN_PARAN);
        sb.append(DOUBLE_QUOTE).append("EMAIL").append(DOUBLE_QUOTE).append(COMMA);
        sb.append(DOUBLE_QUOTE).append("PASSWORD").append(DOUBLE_QUOTE).append(COMMA);
        sb.append(DOUBLE_QUOTE).append("ROLES").append(DOUBLE_QUOTE).append(COMMA);
        sb.append(DOUBLE_QUOTE).append("NICK_NAME").append(DOUBLE_QUOTE).append(COMMA);
        sb.append(DOUBLE_QUOTE).append("CREATED_TIMESTAMP").append(DOUBLE_QUOTE).append(CLOSE_PARAN);
        sb.append(" values(").append(email).append(hashedPassword).append(roles).append(nickname).append("now()");
        sb.append(CLOSE_PARAN).append(SEMICOLON);
        return sb.toString();
    }
}
