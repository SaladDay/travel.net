package cn.saladday.travel.dao;

import cn.saladday.travel.domain.Favourite;
import cn.saladday.travel.domain.Route;

import java.util.Date;
import java.util.List;

public interface FavouriteDao {

    Favourite findByUidAndRid(int uid,int rid);

    /**
     * 查找rid被收藏的次数
     * @param rid
     * @return
     */
    int countByRid(int rid);

    int countByUid(int uid);

    int countByUidAndRname(int uid,String rname);

    List<Route> findByPage(int uid,String rname, int start, int pageSize);
    /**
     * 传入uid,rid,date创建一个收藏数据
     * @param uid
     * @param rid
     * @param date
     * @return
     */
    int addSingle(int uid, int rid, Date date);

    int removeSingle(int uid, int rid);
}
