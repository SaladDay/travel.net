package cn.saladday.travel.dao.mapper;

import cn.saladday.travel.domain.RouteImg;

import java.util.List;

public interface RouteImgMapper {

    /**
     * 一个route可以对应多个routeImg
     * @param rid
     * @return
     */
    List<RouteImg> findByRid(int rid);
}
