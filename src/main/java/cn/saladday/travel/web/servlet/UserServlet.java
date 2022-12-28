package cn.saladday.travel.web.servlet;

import cn.saladday.travel.domain.ResultInfo;
import cn.saladday.travel.domain.User;
import cn.saladday.travel.service.UserService;
import cn.saladday.travel.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet("/user/*")
public class UserServlet extends BaseServlet {

    private UserService userService = new UserServiceImpl();

    /**
     * 用户登入
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void login(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ResultInfo info = new ResultInfo();
        ObjectMapper mapper = new ObjectMapper();
        resp.setContentType("application/json;charset=utf-8");

        //校验验证码是否正确，错误直接返回
        String check = req.getParameter("check");
        HttpSession session = req.getSession();
        String captcha = (String) session.getAttribute("CAPTCHA_SERVER");

        if (captcha == null) {
            info.setFlag(false);
            info.setErrorMsg("验证码过期，请点击刷新验证码");
            String json = mapper.writeValueAsString(info);
            resp.getWriter().write(json);
            return;

        } else if (!captcha.equalsIgnoreCase(check)) {
            info.setFlag(false);
            info.setErrorMsg("验证码错误");
            String json = mapper.writeValueAsString(info);
            resp.getWriter().write(json);
            return;
        }
        session.removeAttribute("CAPTCHA_SERVER");
        //获取用户数据，并封装为User类
        Map<String, String[]> map = req.getParameterMap();
        User loginUser = new User();
        try {
            BeanUtils.populate(loginUser, map);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        //调用service:ResultInfo login(User loginUser);
        //设置返回类型为Json
        //1.账号不存在:返回ErrorMsg:
        //2.账号未激活:返回ErrorMsg
        //3.登录成功:返回true && user，并且传入session中

        //这里的_info是service层传入的info，之前的info是验证码的info
        info = userService.login(loginUser);
        if (info.isFlag()) {
            //登入成功
            session.setAttribute("user", info.getData());
            //不可以让用户数据到前台
            info.setData(null);
        }
        String json = mapper.writeValueAsString(info);
        resp.getWriter().write(json);

    }

    /**
     * 用户激活
     *
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void active(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取code码
        String code = req.getParameter("code");
        //调用service中的Boolean active(String code)方法
        ResultInfo info = userService.active(code);
//        ObjectMapper mapper = new ObjectMapper();
//        String json = mapper.writeValueAsString(info);
//        resp.setContentType("application/json;charset=utf-8");

        //todo 改为JSON返回，美化UI
        String msg;
        if (info.isFlag()) {
            //激活成功
            msg = "激活成功，请<a href='http://localhost/travel/login.html'>登入</a>";
        } else {
            //激活失败
            msg = info.getErrorMsg();
        }
        resp.getWriter().write(msg);
    }

    /**
     * 用户退出
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void exit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //删除数据
        request.getSession().invalidate();
        //重定向到index.html
        response.sendRedirect(request.getContextPath() + "/index.html");
    }

    /**
     * 从session中拿出UserName
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void findUserName(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //从session中拿出数据
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user != null) {
            response.getWriter().write("欢迎您," + user.getName());
        } else {
            response.getWriter().write("暂未登录，请登录");
        }
        //返回user.name回去，因为返回的是text，所以直接返回String即可
    }

    /**
     * 注册
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void register(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ResultInfo info = new ResultInfo();
        ObjectMapper mapper = new ObjectMapper();

        //获取数据
        Map<String, String[]> map = request.getParameterMap();

        //检验验证码是否正确
        String check = map.get("check")[0];
        HttpSession session = request.getSession();
        String captcha = (String) session.getAttribute("CAPTCHA_SERVER");

        if (captcha == null) {
            info.setFlag(false);
            info.setErrorMsg("验证码过时，请刷新");
            String json = mapper.writeValueAsString(info);
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(json);
            return;
        }

        if (!check.equalsIgnoreCase(captcha)) {
            info.setFlag(false);
            info.setErrorMsg("验证码错误");
            String json = mapper.writeValueAsString(info);
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(json);
            return;
        }
        //验证码只能从服务器中取出一次，用完就销毁
        session.removeAttribute("CAPTCHA_SERVER");

        //封装对象(其中还有个数据在map中 String[] check)
        User registerUser = new User();
        try {
            BeanUtils.populate(registerUser, map);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        //调用service完成注册
        //此时获得了service层的info，需要和外部的info（验证码相关信息进行拼接）
        ResultInfo _info = userService.register(registerUser);
        if (_info.isFlag()) {
            //完成注册
            info.setFlag(true);
        } else {
            //注册失败
            info.setFlag(false);
            info.setErrorMsg(_info.getErrorMsg());
        }
        //返回数据
        //在Filter内设置返回的MIME类型为TEXT，但是这里返回的是json数据,因此需要设置编码

        String json = mapper.writeValueAsString(info);
        response.setContentType("Application/Json;charset=utf-8");
        response.getWriter().write(json);

    }


}
