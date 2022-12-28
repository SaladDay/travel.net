package cn.saladday.travel.dao.impl;

import cn.saladday.travel.dao.RouteImgDao;
import cn.saladday.travel.domain.RouteImg;
import cn.saladday.travel.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class RouteImgDaoImpl implements RouteImgDao {
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());


    @Override
    public List<RouteImg> findByRid(int rid) {
        try{
            String sql = "SELECT * FROM tab_route_img WHERE rid = ?;";
            List<RouteImg> query = template.query(sql, new BeanPropertyRowMapper<RouteImg>(RouteImg.class), rid);
            return query;
        }catch (Exception e){
            return null;
        }
    }
}
