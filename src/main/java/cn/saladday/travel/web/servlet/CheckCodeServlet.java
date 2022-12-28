package cn.saladday.travel.web.servlet;

import cn.saladday.travel.util.CaptchaUtils;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

/**
 * 验证码
 */
@WebServlet("/checkCode")
public class CheckCodeServlet extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		
		//服务器通知浏览器不要缓存
		//如果缓存了会切换不了图片
		response.setHeader("pragma","no-cache");
		response.setHeader("cache-control","no-cache");
		response.setHeader("expires","0");

		String checkCode = CaptchaUtils.getSecurityCode();

		//将验证码放入HttpSession中
		request.getSession().setAttribute("CAPTCHA_SERVER",checkCode);

		BufferedImage image = CaptchaUtils.createImage(checkCode);
		//将内存中的图片输出到浏览器
		//参数一：图片对象
		//参数二：图片的格式，如PNG,JPG,GIF
		//参数三：图片输出到哪里去
		ImageIO.write(image,"PNG",response.getOutputStream());
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request,response);
	}
}



