package cn.saladday.travel.service.impl;

import cn.saladday.travel.dao.FavouriteDao;
import cn.saladday.travel.dao.impl.FavouriteDaoImpl;
import cn.saladday.travel.domain.Favourite;
import cn.saladday.travel.domain.PageBean;
import cn.saladday.travel.domain.ResultInfo;
import cn.saladday.travel.domain.Route;
import cn.saladday.travel.service.FavouriteService;

import java.util.Date;
import java.util.List;

public class FavouriteServiceImpl implements FavouriteService {
    private FavouriteDao dao = new FavouriteDaoImpl();
    @Override
    public ResultInfo isFavourite(int uid, int rid) {
        ResultInfo info = new ResultInfo();
        //调用Dao包下的 findByUidAndRid;
        //如果能找到就返回true，如果不能就返回false
        try {
            Favourite favourite = dao.findByUidAndRid(uid, rid);
            info.setFlag(true);
            if (favourite == null) {
                info.setData(false);
            } else {
                info.setData(true);
            }
            return info;
        }catch (Exception e){
            info.setFlag(false);
            info.setErrorMsg(e.getMessage());
            return info;
        }

    }

    @Override
    public Boolean addFavourite(int uid, int rid) {
        Date date = new Date();
        int i = dao.addSingle(uid, rid, date);
        return i > 0;
    }

    @Override
    public Boolean removeFavourite(int uid, int rid) {
        int i = dao.removeSingle(uid, rid);
        return i > 0;
    }

    @Override
    public ResultInfo pageQuery(int uid,String rname, int currentPage, int pageSize) {
        ResultInfo info = new ResultInfo();
        PageBean<Route> pageBean = new PageBean<Route>();
        int totalCount = dao.countByUidAndRname(uid,rname);
        int totalPage = totalCount%pageSize==0?(totalCount/pageSize):(totalCount/pageSize+1);
        int start = (currentPage-1)*pageSize;
        List<Route> routes = dao.findByPage(uid,rname,start, pageSize);
        if(routes==null){
            info.setFlag(false);
            info.setErrorMsg("daoError");
            return info;
        }else{
            info.setFlag(true);
            pageBean.setTotalPage(totalPage);
            pageBean.setPageSize(pageSize);
            pageBean.setList(routes);
            pageBean.setTotalCount(totalCount);
            pageBean.setCurrentPage(currentPage);
            info.setData(pageBean);
            return info;
        }

    }
}
