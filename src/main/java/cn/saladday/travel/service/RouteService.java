package cn.saladday.travel.service;

import cn.saladday.travel.domain.ResultInfo;
import cn.saladday.travel.domain.Route;

public interface RouteService {

    /**
     * 通过cid和rname，进行分页查询
     *
     * @param cid
     * @param currentPage
     * @param pageSize
     * @param rname
     * @return
     */
    ResultInfo pageQuery(int cid, int currentPage, int pageSize, String rname);

    /**
     * 通过cid
     * @param rid
     * @return
     */
    ResultInfo findDetail(int rid);
}
