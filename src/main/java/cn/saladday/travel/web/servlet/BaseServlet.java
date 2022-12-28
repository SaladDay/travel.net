package cn.saladday.travel.web.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BaseServlet extends HttpServlet {
    //此servlet不会被调用，只是作为父类提供一个公共的service函数
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取方法名
        String uri = req.getRequestURI();
        String methodName = uri.substring(uri.lastIndexOf('/')+1);
//        System.out.println(methodName);
        //获取方法对象
        try {
            Method method = this.getClass().getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
            method.invoke(this, req, resp);
        } catch (NoSuchMethodException e) {
            //说明访问了个奇怪的页面，需要给他跳到默认的页面上去
            resp.sendRedirect(req.getContextPath()+"/index.html");
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }


    }

}
