package com.vietle.pizzeria.repo;

import com.vietle.pizzeria.domain.Role;
import com.vietle.pizzeria.domain.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int i) throws SQLException {
        List<Role> roles = null;
        String[] rolesArray = (String[]) rs.getArray("ROLES").getArray();
        List<String> roleList = Arrays.asList(rolesArray);
        if(roleList != null && !roleList.isEmpty()) {
            roles = new ArrayList<>(roleList.size());
            for(String r : roleList) {
                if(r.equals("ROLE_USER")) {
                    roles.add(Role.ROLE_USER);
                }
                if(r.equals("ROLE_ADMIN")) {
                    roles.add(Role.ROLE_ADMIN);
                }
            }
        }
        User user = User.builder().email(rs.getString("EMAIL")).password(null)
                .nickName(rs.getString("NICK_NAME")).id(rs.getInt("USER_ID"))
                .password(rs.getString("PASSWORD")).roles(roles).build();
        return user;
    }
}
