package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private static final UserRowMapper ROW_MAPPER = new UserRowMapper();

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(User user) {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);
        HashMap<String, Object> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("role", user.getRoles().toString().substring(1, user.getRoles().toString().length() - 1));
        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
            objectObjectHashMap.put("user_id", user.id());
            SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("user_roles");
            simpleJdbcInsert.execute(objectObjectHashMap);
        } else {
            objectObjectHashMap.put("user_id", user.id());
            if (namedParameterJdbcTemplate.update("""
                       UPDATE users  SET name=:name, email=:email, password=:password,
                       registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id 
                    """, parameterSource) == 0
                    &
                    namedParameterJdbcTemplate.update("UPDATE  user_roles SET user_id=:user_id, role=:role WHERE user_id=:user_id", objectObjectHashMap) == 0

            )
                return null;
        }

        return user;
    }


    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = getUniUsersPerRoles(jdbcTemplate.query("SELECT * FROM users u LEFT JOIN  user_roles ur ON u.id = ur.user_id WHERE id=?", ROW_MAPPER, id));
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        List<User> users = getUniUsersPerRoles(jdbcTemplate.query("SELECT * FROM users u LEFT JOIN  user_roles ur ON u.id = ur.user_id WHERE email=?", ROW_MAPPER, email));
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
        return getUniUsersPerRoles(jdbcTemplate.query("SELECT * FROM users u LEFT JOIN  user_roles ur ON u.id = ur.user_id  ORDER BY name, email", ROW_MAPPER));

    }

    private static List<User> getUniUsersPerRoles(List<User> users) {
        HashMap<Integer, Set<Role>> userRoleHashMap = new HashMap<>();
        for (User u :
                users) {
            Set<Role> roles = u.getRoles();
            if (roles == null)
                continue;

            if (userRoleHashMap.containsKey(u.id())) {
                userRoleHashMap.get(u.id()).addAll(roles);
                u.setId(-1);
            } else userRoleHashMap.put(u.id(), roles);
        }
        List<User> users1 = users.stream().filter(u -> u.id() != -1).peek(u -> u.setRoles(userRoleHashMap.get(u.id()))).toList();
        return users1;
    }

    private static final class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();

            user.setId(rs.getInt("id"));
            user.setName(rs.getString("name"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password"));
            user.setCaloriesPerDay(rs.getInt("calories_per_day"));
            user.setEnabled(rs.getBoolean("enabled"));
            user.setRegistered(rs.getTimestamp("registered"));
            String role = rs.getString("role");
            if (role != null) {
                Set<Role> roles = new HashSet<>();
                roles.add(Role.valueOf(role));
                user.setRoles(roles);
            }
            return user;
        }
    }
}
