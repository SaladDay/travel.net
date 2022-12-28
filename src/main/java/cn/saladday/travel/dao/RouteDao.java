package cn.saladday.travel.dao;

import cn.saladday.travel.domain.Route;

import java.util.List;

public interface RouteDao {


    int findCount(int cid, String rname);

    /**
     * 分页查询
     * @param cid 分类
     * @param start 开始页码
     * @param pageSize 一页数据量
     * @param rname rname模糊查询条件
     * @return
     */
    List<Route> findByPage(int cid, int start, int pageSize, String rname);

    /**
     * 通过cid查询单个route数据
     * @param rid
     * @return
     */
    Route findByRid(int rid);
}
