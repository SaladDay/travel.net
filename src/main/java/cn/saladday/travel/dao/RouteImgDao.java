package cn.saladday.travel.dao;

import cn.saladday.travel.domain.RouteImg;

import java.util.List;

public interface RouteImgDao {
    /**
     * 一个route可以对应多个routeImg
     * @param rid
     * @return
     */
    List<RouteImg> findByRid(int rid);
}
