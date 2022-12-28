package cn.saladday.travel.dao.impl;

import cn.saladday.travel.dao.UserDao;
import cn.saladday.travel.domain.User;
import cn.saladday.travel.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

public class UserDaoImpl implements UserDao {
    private DataSource ds = JDBCUtils.getDataSource();
    private JdbcTemplate template = new JdbcTemplate(ds);


    @Override
    public User findByUsername(String username) {
        String sql = "select * from tab_user where username = ?";
        try {
            User user = template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), username);
            return user;
        } catch (Exception e) {
            return null;
        }

    }

    @Override
    public User findByCode(String code) {
        try {
            String sql = "select * from tab_user where code = ?";
            User user = template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), code);
            return user;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public User findByEmail(String email) {
        try {
            String sql = "select * from tab_user where email = ?";
            User user = template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), email);
            return user;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public User findByUsernameAndPassword(User loginUser) {
        try {
            String sql = "select * from tab_user where username = ? and password = ?";
            User user = template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), loginUser.getUsername(),loginUser.getPassword());
            return user;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean save(User user) {
        String sql = "insert into tab_user(username, password, name, birthday, sex, telephone, email,status,code)" +
                " values (?,?,?,?,?,?,?,?,?)";
        try {
            template.update(sql, user.getUsername(),
                    user.getPassword(),
                    user.getName(),
                    user.getBirthday(),
                    user.getSex(),
                    user.getTelephone(),
                    user.getEmail(),
                    user.getStatus(),
                    user.getCode());
            return true;
        } catch (Exception e) {
            return false;
        }


    }

    @Override
    public boolean updateStatus(User user) {
        String sql = "update tab_user set status = 'Y' where code = ?;";
        try {
            template.update(sql, user.getCode());
            return true;

        } catch (Exception e) {
            return false;
        }

    }
}
