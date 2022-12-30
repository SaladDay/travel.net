package cn.saladday.travel.service.impl;

import cn.saladday.travel.dao.mapper.FavouriteMapper;
import cn.saladday.travel.domain.Favourite;
import cn.saladday.travel.domain.PageBean;
import cn.saladday.travel.domain.ResultInfo;
import cn.saladday.travel.domain.Route;
import cn.saladday.travel.service.FavouriteService;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

public class FavouriteServiceImpl implements FavouriteService {
//    private FavouriteDao dao = new FavouriteDaoImpl();
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
    public ResultInfo isFavourite(int uid, int rid) {
        ResultInfo info = new ResultInfo();
        SqlSession sqlSession = factory.openSession(true);
        FavouriteMapper favouriteMapper = sqlSession.getMapper(FavouriteMapper.class);

        //调用Dao包下的 findByUidAndRid;
        //如果能找到就返回true，如果不能就返回false
        try {
//            Favourite favourite = dao.findByUidAndRid(uid, rid);
            Favourite favourite = favouriteMapper.findByUidAndRid(uid, rid);
            info.setFlag(true);
            if (favourite == null) {
                info.setData(false);
            } else {
                info.setData(true);
            }
            sqlSession.close();
            return info;
        }catch (Exception e){
            info.setFlag(false);
            info.setErrorMsg(e.getMessage());
            sqlSession.close();
            return info;
        }

    }

    @Override
    public Boolean addFavourite(int uid, int rid) {
        SqlSession sqlSession = factory.openSession(true);
        FavouriteMapper favouriteMapper = sqlSession.getMapper(FavouriteMapper.class);

        Date date = new Date();
//        int i = dao.addSingle(uid, rid, date);
        int i = favouriteMapper.addSingle(uid, rid, date);
        sqlSession.close();
        return i > 0;
    }

    @Override
    public Boolean removeFavourite(int uid, int rid) {
        SqlSession sqlSession = factory.openSession(true);
        FavouriteMapper favouriteMapper = sqlSession.getMapper(FavouriteMapper.class);
        int i = favouriteMapper.removeSingle(uid, rid);
        sqlSession.close();
        return i > 0;
    }

    @Override
    public ResultInfo pageQuery(int uid,String rname, int currentPage, int pageSize) {
        SqlSession sqlSession = factory.openSession(true);
        FavouriteMapper favouriteMapper = sqlSession.getMapper(FavouriteMapper.class);
        ResultInfo info = new ResultInfo();
        PageBean<Route> pageBean = new PageBean<Route>();
        int totalCount = favouriteMapper.countByUidAndRname(uid,rname);
        int totalPage = totalCount%pageSize==0?(totalCount/pageSize):(totalCount/pageSize+1);
        int start = (currentPage-1)*pageSize;
        List<Route> routes = favouriteMapper.findByPage(uid,rname,start, pageSize);
        if(routes==null){
            info.setFlag(false);
            info.setErrorMsg("daoError");
            sqlSession.close();
            return info;
        }else{
            info.setFlag(true);
            pageBean.setTotalPage(totalPage);
            pageBean.setPageSize(pageSize);
            pageBean.setList(routes);
            pageBean.setTotalCount(totalCount);
            pageBean.setCurrentPage(currentPage);
            info.setData(pageBean);
            sqlSession.close();
            return info;
        }

    }
}
