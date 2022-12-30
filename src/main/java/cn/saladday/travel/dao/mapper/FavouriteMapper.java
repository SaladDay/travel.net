package cn.saladday.travel.dao.mapper;

import cn.saladday.travel.domain.Favourite;
import cn.saladday.travel.domain.Route;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface FavouriteMapper {

    Favourite findByUidAndRid(@Param("uid") int uid, @Param("rid") int rid);

    /**
     * 查找rid被收藏的次数
     * @param rid
     * @return
     */
    int countByRid(int rid);

    int countByUid(int uid);

    int countByUidAndRname(@Param("uid")int uid, @Param("rname")String rname);

    List<Route> findByPage(@Param("uid")int uid, @Param("rname")String rname, @Param("start")int start, @Param("pageSize")int pageSize);
    /**
     * 传入uid,rid,date创建一个收藏数据
     * @param uid
     * @param rid
     * @param date
     * @return
     */
    int addSingle(@Param("uid")int uid, @Param("rid")int rid, @Param("date")Date date);

    int removeSingle(@Param("uid")int uid, @Param("rid")int rid);
}
