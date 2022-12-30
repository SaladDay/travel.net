package cn.saladday.travel.dao.mapper;

import cn.saladday.travel.domain.User;

public interface UserMapper {

    public User findByUsername(String username);

    public User findByCode(String code);

    public User findByEmail(String email);

    public User findByUsernameAndPassword(User loginUser);

    public boolean save(User user);

    public boolean updateStatus(User user);


}
