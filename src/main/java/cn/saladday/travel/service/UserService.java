package cn.saladday.travel.service;

import cn.saladday.travel.domain.ResultInfo;
import cn.saladday.travel.domain.User;

public interface UserService {

    /**
     * 用户注册功能，返回true/false,并返回失败原因
     */
    public ResultInfo register(User user);

    /**
     * 激活用户，返回true/false,并返回失败原因
     * @param code
     * @return
     */
    public ResultInfo active(String code);

    /**
     * 登入功能
     * @param loginUser，仅包含账号密码的user类
     * @return
     */
    public ResultInfo login(User loginUser);


}
