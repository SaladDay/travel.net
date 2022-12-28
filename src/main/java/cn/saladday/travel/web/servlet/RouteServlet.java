package cn.saladday.travel.web.servlet;


import cn.saladday.travel.domain.ResultInfo;
import cn.saladday.travel.domain.Route;
import cn.saladday.travel.domain.User;
import cn.saladday.travel.service.FavouriteService;
import cn.saladday.travel.service.RouteService;
import cn.saladday.travel.service.UserService;
import cn.saladday.travel.service.impl.FavouriteServiceImpl;
import cn.saladday.travel.service.impl.RouteServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import sun.tools.jconsole.JConsole;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@WebServlet("/route/*")
public class RouteServlet extends BaseServlet{
     private RouteService routeService = new RouteServiceImpl();
     private ObjectMapper mapper = new ObjectMapper();
     private boolean exist(String string){
          return string != null && !"".equals(string) && !"null".equalsIgnoreCase(string);
     }

     /**
      * 分页展示
      * @param req
      * @param resp
      */
     public void pageQuery(HttpServletRequest req, HttpServletResponse resp) throws IOException {
          //获取参数
          String cidStr = req.getParameter("cid");
          String currentPageStr = req.getParameter("currentPage");
          String pageSizeStr = req.getParameter("pageSize");
          String rname = req.getParameter("rname");
          if(rname!=null) {
               rname = new String(rname.getBytes("iso-8859-1"), "utf-8");
          }



          //处理数据--设置默认值
          int cid = 0;
          if(cidStr!=null&&!"".equals(cidStr)&&!"null".equalsIgnoreCase(cidStr)){
               cid = Integer.parseInt(cidStr);
               if(cid<=0)cid=1;
          }else{
               //在cid不存在的同时rname也不存在，才给其一个默认值1
               if(rname==null||"".equals(rname)) {
                    cid = 1;
               }
          }

          int currentPage = 0;
          if(currentPageStr!=null&&!"".equals(currentPageStr)){
               currentPage = Integer.parseInt(currentPageStr);
               if(currentPage<=0)currentPage=1;
          }else{
               currentPage = 1;
          }

          int pageSize = 0;
          if(pageSizeStr!=null&&!"".equals(pageSizeStr)){
               pageSize = Integer.parseInt(pageSizeStr);
               if(pageSize<=0)pageSize=5;
          }else{
               pageSize = 5;
          }

          //如果rname不存在就给他一个通配符
          if(rname==null||"".equals(rname)||"null".equalsIgnoreCase(rname)){
               //此处应该给他一个通配符
               rname = "%";
          }
          //调用Service，ResultInfo pageQuery(int cid,int currentPage,int pageSize,String rname)
          ResultInfo info = routeService.pageQuery(cid, currentPage, pageSize,rname);
          if(info.isFlag()) {
               //查询成功
               //将info.data返回前台
               try {
                    String json = mapper.writeValueAsString(info.getData());
                    resp.getWriter().write(json);
               } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
               }
          }else{
               //查询失败
               System.out.println(info.getErrorMsg());
          }

     }

     public void favouritePageQuery(HttpServletRequest req, HttpServletResponse resp) throws IOException {
          ResultInfo info = new ResultInfo();
          String rname = req.getParameter("rname");
          if(rname!=null) {
               rname = new String(rname.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
          }
          resp.setContentType("application/json;charset=utf-8");
          //获取参数,并初始化
          String currentPageStr = req.getParameter("currentPage");
          String pageSizeStr = req.getParameter("pageSize");
          User user = (User)req.getSession().getAttribute("user");
          if(user==null){
               info.setFlag(true);
               info.setErrorMsg("notLogged");
               String json = mapper.writeValueAsString(info);
               resp.getWriter().write(json);
               return;
          }
          if(!exist(rname)){
               rname = "%";
          }
          int currentPage = 0;
          if(exist(currentPageStr)){
               currentPage = Integer.parseInt(currentPageStr);
          }else {
               currentPage = 1;
          }
          int pageSize = 0;
          if(exist(pageSizeStr)){
               pageSize = Integer.parseInt(pageSizeStr);
          }else {
               pageSize = 5;
          }

          //调用FavouriteService 下的 resultInfo pageQuery(int uid,int currentPage,int PageSize)
          FavouriteService favouriteService = new FavouriteServiceImpl();
          info = favouriteService.pageQuery(user.getUid(),rname, currentPage, pageSize);
          if(info.isFlag()){
               String json = mapper.writeValueAsString(info);
               resp.getWriter().write(json);
               return;

          }else{
               System.out.println(info.getErrorMsg());
          }

     }

     /**
      * 通过rid查询一个route的详情
      */
     public void findDetail(HttpServletRequest req, HttpServletResponse resp) throws IOException {
          //获取参数
          String ridStr = req.getParameter("rid");
          int rid;
          if(ridStr!=null && !"".equals(ridStr)&&!"null".equalsIgnoreCase(ridStr)){
               rid = Integer.parseInt(ridStr);
          }else{
               return;
          }
          ResultInfo info  = routeService.findDetail(rid);
          if(info.isFlag()){

               String json = mapper.writeValueAsString(info.getData());
               resp.setContentType("application/json;charset=utf-8");
               resp.getWriter().write(json);
          }else{
               System.out.println(info.getErrorMsg());
               return;
          }
     }

     public void isFavourite(HttpServletRequest req, HttpServletResponse resp) throws IOException {
          String ridStr = req.getParameter("rid");
          int rid;
          if(ridStr!=null && !"".equals(ridStr)&&!"null".equalsIgnoreCase(ridStr)){
               rid = Integer.parseInt(ridStr);
          }else{
               return;
          }
          User user = (User)req.getSession().getAttribute("user");
          int uid;
          if(user==null){
               uid = 0;
          }else {
               uid=user.getUid();
          }

          //调用favouriteService 中的ResultInfo isFavourite();
          FavouriteService favouriteService = new FavouriteServiceImpl();
          ResultInfo favourite = favouriteService.isFavourite(uid, rid);
          if(favourite.isFlag()){
               String json = mapper.writeValueAsString((Boolean) favourite.getData());
               resp.setContentType("application/json;charset=utf-8");
               resp.getWriter().write(json);

          }else {
               System.out.println(favourite.getErrorMsg());
               return;

          }
     }
     public void likeIt(HttpServletRequest req, HttpServletResponse resp) throws IOException {
          String ridStr = req.getParameter("rid");
          resp.setContentType("application/json;charset=utf-8");
          int rid;
          if(ridStr!=null && !"".equals(ridStr)&&!"null".equalsIgnoreCase(ridStr)){
               rid = Integer.parseInt(ridStr);
          }else{
               String json = "{\"errCode\":2}";
               resp.getWriter().write(json);
               return;
          }
          User user = (User)req.getSession().getAttribute("user");
          if(user==null){
               String json = "{\"errCode\":1}";
               resp.getWriter().write(json);
               return;
          }
          //调用FavouriteService 中的 addFavourite(int rid,int uid);
          FavouriteService favouriteService = new FavouriteServiceImpl();
          Boolean flag = favouriteService.addFavourite(user.getUid(),rid);
          if(!flag){
               String json = "{\"errCode\":2}";
               resp.getWriter().write(json);
          }else {
               String json = "{\"errCode\":0}";
               resp.getWriter().write(json);
          }

     }

     public void dislikeIt(HttpServletRequest req, HttpServletResponse resp) throws IOException {
          String ridStr = req.getParameter("rid");
          resp.setContentType("application/json;charset=utf-8");
          int rid;
          if(ridStr!=null && !"".equals(ridStr)&&!"null".equalsIgnoreCase(ridStr)){
               rid = Integer.parseInt(ridStr);
          }else{
               String json = "{\"errCode\":2}";
               resp.getWriter().write(json);
               return;
          }
          User user = (User)req.getSession().getAttribute("user");
          if(user==null){
               String json = "{\"errCode\":1}";
               resp.getWriter().write(json);
               return;
          }
          //调用FavouriteService 中的 addFavourite(int rid,int uid);
          FavouriteService favouriteService = new FavouriteServiceImpl();
          Boolean flag = favouriteService.removeFavourite(user.getUid(),rid);
          if(!flag){
               String json = "{\"errCode\":2}";
               resp.getWriter().write(json);
          }else {
               String json = "{\"errCode\":0}";
               resp.getWriter().write(json);
          }

     }



}
