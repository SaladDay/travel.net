package cn.saladday.travel.service.impl;

import cn.saladday.travel.dao.FavouriteDao;
import cn.saladday.travel.dao.RouteDao;
import cn.saladday.travel.dao.RouteImgDao;
import cn.saladday.travel.dao.SellerDao;
import cn.saladday.travel.dao.impl.FavouriteDaoImpl;
import cn.saladday.travel.dao.impl.RouteDaoImpl;
import cn.saladday.travel.dao.impl.RouteImgDaoImpl;
import cn.saladday.travel.dao.impl.SellerDaoImpl;
import cn.saladday.travel.domain.*;
import cn.saladday.travel.service.RouteService;

import java.util.List;

public class RouteServiceImpl implements RouteService {
    private RouteDao dao = new RouteDaoImpl();

    @Override
    public ResultInfo pageQuery(int cid, int currentPage, int pageSize, String rname) {
        ResultInfo info = new ResultInfo();
        PageBean<Route> pageBean = new PageBean<Route>();
        int totalCount = dao.findCount(cid,rname);
        if(totalCount==0){
            info.setFlag(true);
            pageBean.setCurrentPage(currentPage);
            pageBean.setPageSize(pageSize);
            pageBean.setList(null);
            pageBean.setTotalCount(totalCount);
            pageBean.setTotalPage(0);
            info.setData(pageBean);
            return info;
        }
        int totalPage = totalCount % pageSize==0?(totalCount/pageSize):(totalCount/pageSize+1);
        int start = (currentPage-1)*pageSize;
        List<Route> routes = dao.findByPage(cid, start, pageSize,rname);
        if(routes==null){
            //有数据，但是没有返回数据
            info.setFlag(false);
            info.setErrorMsg("dao错误");
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
        return info;

    }

    @Override
    public ResultInfo findDetail(int rid) {
        ResultInfo info = new ResultInfo();
        //调用RouteDao下的Route route = findByRid(int rid)
        Route route = dao.findByRid(rid);
        if(route==null){
            info.setFlag(false);
            info.setErrorMsg("route查询错误");
            return info;
        }
        //调用RouteImgDao下的RouteImg = findByRid(int rid)
        RouteImgDao imgDao = new RouteImgDaoImpl();
        List<RouteImg> routeImgs = imgDao.findByRid(rid);
        if(routeImgs==null){
            info.setFlag(false);
            info.setErrorMsg("routeImg查询错误");
            return info;
        }
        //调用SellerDao下的Seller = findBySid(int sid)
        SellerDao sellerDao = new SellerDaoImpl();
        Seller seller = sellerDao.findBySid(route.getSid());
        if(seller==null){
            info.setFlag(false);
            info.setErrorMsg("seller查询错误");
            return info;
        }
        //调用FavouriteDao下的 int countByRid(int rid)
        FavouriteDao favouriteDao = new FavouriteDaoImpl();
        int count = favouriteDao.countByRid(rid);
        route.setCount(count);
        route.setRouteImgList(routeImgs);
        route.setSeller(seller);

        info.setData(route);
        info.setFlag(true);
        return info;

    }
}
