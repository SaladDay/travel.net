package cn.saladday.travel.dao.mapper;

import cn.saladday.travel.domain.Route;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RouteMapper {
    /**
     *
     * @param cid
     * @param rname
     * @return
     */
    int findCount(@Param("cid")int cid, @Param("rname")String rname);
    /**
     * 分页查询
     * @param cid 分类
     * @param start 开始页码
     * @param pageSize 一页数据量
     * @param rname rname模糊查询条件
     * @return
     */
    List<Route> findByPage(@Param("cid")int cid, @Param("start")int start, @Param("pageSize")int pageSize, @Param("rname")String rname);
    /**
     * 通过cid查询单个route数据
     * @param rid
     * @return
     */
    Route findByRid(int rid);
}
