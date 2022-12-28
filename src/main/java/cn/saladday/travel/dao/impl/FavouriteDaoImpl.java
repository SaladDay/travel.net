package cn.saladday.travel.dao.impl;

import cn.saladday.travel.dao.FavouriteDao;
import cn.saladday.travel.domain.Favourite;
import cn.saladday.travel.domain.ResultInfo;
import cn.saladday.travel.domain.Route;
import cn.saladday.travel.service.FavouriteService;
import cn.saladday.travel.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Date;
import java.util.List;

public class FavouriteDaoImpl implements FavouriteDao {
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());


    @Override
    public Favourite findByUidAndRid(int uid, int rid) {
        try{
            String sql = "select * from tab_favorite where uid = ? and rid = ?";
            return template.queryForObject(sql,new BeanPropertyRowMapper<Favourite>(Favourite.class),uid,rid);
        }catch (Exception e){
            return null;
        }

    }

    @Override
    public int countByRid(int rid) {
        try{
            String sql = "SELECT COUNT(*) FROM tab_favorite WHERE rid = ? ;";
            return template.queryForObject(sql,Integer.class,rid);
        }catch (Exception e){
            return 0;
        }
    }

    @Override
    public int countByUid(int uid) {
        try{
            String sql = "SELECT COUNT(*) FROM tab_favorite WHERE uid = ? ;";
            return template.queryForObject(sql,Integer.class,uid);
        }catch (Exception e){
            return 0;
        }
    }

    /**
     * rname必须要传递，如果没有rname就走countByUid
     * @param uid
     * @param rname
     * @return
     */
    @Override
    public int countByUidAndRname(int uid, String rname) {
        try{
            rname = "%"+rname+"%";
            String sql = "SELECT COUNT(*) FROM tab_favorite,tab_route WHERE uid = ? AND rname LIKE ? AND  tab_favorite.`rid`=tab_route.`rid`;";
            return template.queryForObject(sql,Integer.class,uid,rname);
        }catch (Exception e){
            return 0;
        }
    }

    @Override
    public List<Route> findByPage(int uid,String rname, int start, int pageSize) {
        try{
            rname = "%"+rname+"%";
            String sql = "SELECT tab_route.* FROM tab_favorite,tab_route WHERE uid = ? AND rname LIKE ? AND tab_favorite.`rid`=tab_route.`rid` limit ?,?;";
            return template.query(sql,new BeanPropertyRowMapper<Route>(Route.class),uid,rname,start,pageSize);
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public int addSingle(int uid, int rid, Date date) {
        try{
            String sql = "INSERT INTO tab_favorite VALUES(?,?,?);";
            return template.update(sql,rid,date,uid);
        }catch (Exception e){
            return 0;
        }
    }

    @Override
    public int removeSingle(int uid, int rid) {
        try{
            String sql = "delete from tab_favorite where uid = ? and rid = ?";
            return template.update(sql,uid,rid);
        }catch (Exception e){
            return 0;
        }
    }
}
