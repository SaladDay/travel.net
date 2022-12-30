package cn.saladday.travel.service.impl;


import cn.saladday.travel.dao.mapper.FavouriteMapper;
import cn.saladday.travel.dao.mapper.RouteImgMapper;
import cn.saladday.travel.dao.mapper.RouteMapper;
import cn.saladday.travel.dao.mapper.SellerMapper;
import cn.saladday.travel.domain.*;
import cn.saladday.travel.service.RouteService;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class RouteServiceImpl implements RouteService {
//    private RouteDao dao = new RouteDaoImpl();
    private static SqlSessionFactory factory;
    static {
        try {
            InputStream is = Resources.getResourceAsStream("mybatis-config.xml");
            factory = new SqlSessionFactoryBuilder().build(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResultInfo pageQuery(int cid, int currentPage, int pageSize, String rname) {
        SqlSession sqlSession = factory.openSession(true);
        RouteMapper routeMapper = sqlSession.getMapper(RouteMapper.class);
        ResultInfo info = new ResultInfo();
        PageBean<Route> pageBean = new PageBean<Route>();
//        int totalCount = dao.findCount(cid,rname);
        int totalCount = routeMapper.findCount(cid, rname);
        if(totalCount==0){
            info.setFlag(true);
            pageBean.setCurrentPage(currentPage);
            pageBean.setPageSize(pageSize);
            pageBean.setList(null);
            pageBean.setTotalCount(totalCount);
            pageBean.setTotalPage(0);
            info.setData(pageBean);
            sqlSession.close();
            return info;
        }
        int totalPage = totalCount % pageSize==0?(totalCount/pageSize):(totalCount/pageSize+1);
        int start = (currentPage-1)*pageSize;
//        List<Route> routes = dao.findByPage(cid, start, pageSize,rname);
        List<Route> routes = routeMapper.findByPage(cid, start, pageSize, rname);
        if(routes==null){
            //有数据，但是没有返回数据
            info.setFlag(false);
            info.setErrorMsg("dao错误");
            sqlSession.close();
            return info;
        }
        //说明此时routes下有数据
        pageBean.setCurrentPage(currentPage);
        pageBean.setPageSize(pageSize);
        pageBean.setList(routes);
        pageBean.setTotalCount(totalCount);
        pageBean.setTotalPage(totalPage);

        info.setFlag(true);
        info.setData(pageBean);
        sqlSession.close();
        return info;

    }

    @Override
    public ResultInfo findDetail(int rid) {
        SqlSession sqlSession = factory.openSession();
        RouteMapper routeMapper = sqlSession.getMapper(RouteMapper.class);

        ResultInfo info = new ResultInfo();
        //调用RouteDao下的Route route = findByRid(int rid)
//        Route route = dao.findByRid(rid);
        Route route = routeMapper.findByRid(rid);

        if(route==null){
            info.setFlag(false);
            info.setErrorMsg("route查询错误");
            sqlSession.close();
            return info;
        }
        //调用RouteImgDao下的RouteImg = findByRid(int rid)
//        RouteImgDao imgDao = new RouteImgDaoImpl();
        RouteImgMapper routeImgMapper = sqlSession.getMapper(RouteImgMapper.class);
        List<RouteImg> routeImgs = routeImgMapper.findByRid(rid);
        if(routeImgs==null){
            info.setFlag(false);
            info.setErrorMsg("routeImg查询错误");
            sqlSession.close();
            return info;
        }
        //调用SellerDao下的Seller = findBySid(int sid)
//        SellerDao sellerDao = new SellerDaoImpl();
        SellerMapper sellerMapper = sqlSession.getMapper(SellerMapper.class);
        Seller seller = sellerMapper.findBySid(route.getSid());
        if(seller==null){
            info.setFlag(false);
            info.setErrorMsg("seller查询错误");
            sqlSession.close();
            return info;
        }
        //调用FavouriteDao下的 int countByRid(int rid)
//        FavouriteDao favouriteDao = new FavouriteDaoImpl();
        FavouriteMapper favouriteMapper = sqlSession.getMapper(FavouriteMapper.class);

//        int count = favouriteDao.countByRid(rid);
        int count = favouriteMapper.countByRid(rid);

        route.setCount(count);
        route.setRouteImgList(routeImgs);
        route.setSeller(seller);

        info.setData(route);
        info.setFlag(true);
        sqlSession.close();
        return info;

    }
}
