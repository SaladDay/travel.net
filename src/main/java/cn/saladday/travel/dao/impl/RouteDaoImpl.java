package cn.saladday.travel.dao.impl;

import cn.saladday.travel.dao.RouteDao;
import cn.saladday.travel.domain.Route;
import cn.saladday.travel.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RouteDaoImpl implements RouteDao {
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());
    @Override
    public int findCount(int cid, String rname) {
        try{
            String sqlBase = "select count(rid) from tab_route where rname like ? ";
            StringBuilder sb = new StringBuilder(sqlBase);
            List<Object> parameter = new ArrayList<Object>();
            if(cid==0){
                parameter.add("%"+rname+"%");
            }else{
                sb.append("and cid = ? ");
                parameter.add("%"+rname+"%");
                parameter.add(cid);
            }
            String sql = sb.toString();
//            String sql = "select count(rid) FROM tab_route WHERE cid = ? and rname like ?";
            Integer count = template.queryForObject(sql, Integer.class, parameter.toArray());
            return count;
        }catch (Exception e){
            return 0;
        }

    }

    @Override
    public List<Route> findByPage(int cid, int start, int pageSize, String rname) {
        try{
            String sqlBase = "select * from tab_route where 1=1 ";
            StringBuilder sb = new StringBuilder(sqlBase);
            List<Object> parameter = new ArrayList<Object>();
            if(cid==0){
                sb.append("and rname like ? limit ?,?");
                parameter.add("%"+rname+"%");
            }else{
                sb.append("and rname like ? and cid = ? limit ?,?");
                parameter.add("%"+rname+"%");
                parameter.add(cid);
            }
            parameter.add(start);
            parameter.add(pageSize);
            String sql = sb.toString();
            List<Route> list = template.query(sql, new BeanPropertyRowMapper<Route>(Route.class), parameter.toArray());
            return list;
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public Route findByRid(int rid) {
        try{
            String sql = "SELECT * FROM tab_route WHERE rid = ?;";
            return template.queryForObject(sql, new BeanPropertyRowMapper<Route>(Route.class), rid);
        }catch (Exception e){
            return null;
        }
    }
}
