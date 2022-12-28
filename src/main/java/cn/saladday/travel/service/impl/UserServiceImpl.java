package cn.saladday.travel.service.impl;

import cn.saladday.travel.dao.UserDao;
import cn.saladday.travel.dao.impl.UserDaoImpl;
import cn.saladday.travel.domain.ResultInfo;
import cn.saladday.travel.domain.User;
import cn.saladday.travel.service.UserService;
import cn.saladday.travel.util.MailUtils;
import cn.saladday.travel.util.UuidUtil;
//import com.sun.tools.javac.comp.Todo;

public class UserServiceImpl implements UserService {
    private UserDao dao = new UserDaoImpl();


    @Override
    public ResultInfo register(User user) {
        ResultInfo info = new ResultInfo();

        //判断用户是否已经存在->调用Dao User findByUsername(String username)
        User username_user = dao.findByUsername(user.getUsername());
        //判断邮箱是否已经存在->调用Dao User findByEmail(String email)
        User email_user = dao.findByEmail(user.getEmail());
        if (username_user != null) {
            //若存在直接返回false
            info.setFlag(false);
            info.setErrorMsg("用户名重复");
            return info;

        }else if(email_user!=null){
            info.setFlag(false);
            info.setErrorMsg("邮箱重复");
            return info;

        } else {
            //不存在则-->调用Dao Boolean save(User)-->return save函数返回值
            //设置用户的唯一标识
            user.setCode(UuidUtil.getUuid());
            //设置用户的激活状态，新注册的用户激活状态均为N
            user.setStatus("N");
            //发送验证码邮件
            //todo 加入验证码失效功能
            //todo 设置多线程发送邮件功能
            String content = "<a href='http://localhost/travel/user/active?code=" + user.getCode() + "'>旅游网，请点击激活</a>";
            boolean b = MailUtils.sendMail(user.getEmail(), content, "旅游网激活邮件");

            if (b && dao.save(user)) {
                info.setFlag(true);
                return info;

            } else{
                //1.发送邮件错误2.dao错误
                info.setFlag(false);
                info.setErrorMsg("激活邮件发送失败，请重试");
                return info;
            }

        }

    }

    @Override
    public ResultInfo active(String code) {
        ResultInfo info = new ResultInfo();
        //查询code是否为null，如果是，视为恶意攻击，警告
        if (code == null) {
            info.setFlag(false);
            info.setErrorMsg("用户不存在，请勿对网站进行违法操作,违规者将承担法律责任");
            return info;
        }
        //调用Dao下的User findByCode(String code)
        User _user = dao.findByCode(code);
        // 查询code是否有改用户，如果没有，视为恶意攻击，警告
        if (_user == null) {
            info.setFlag(false);
            info.setErrorMsg("用户不存在，请勿对网站进行违法操作，违规者将承担法律责任");
            return info;
        }
        //如果有，调用Dao下的Boolean updateStatus(User user)
        boolean b = dao.updateStatus(_user);
        if (b) {
            info.setFlag(true);
        } else {
            info.setFlag(false);
            info.setErrorMsg("未知错误，请联系管理员");
        }
        return info;

    }

    @Override
    public ResultInfo login(User loginUser) {
        ResultInfo info = new ResultInfo();

        //调用dao下的User findByUsernameAndPassword(User loginUser)
        User user = dao.findByUsernameAndPassword(loginUser);

        //判断是否存在该用户
        if(user == null){
            info.setFlag(false);
            info.setErrorMsg("用户名或密码错误");
            return info;
        }
        //判断是否激活
        assert user != null;
        if("N".equals(user.getStatus())){
            info.setFlag(false);
            info.setErrorMsg("账户未激活");
            return info;
        }

        //均满足则返回完整的User对象
        info.setFlag(true);
        info.setData(user);
        return info;

    }
}
