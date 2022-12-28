package cn.saladday.travel.dao.impl;

import cn.saladday.travel.dao.SellerDao;
import cn.saladday.travel.domain.Seller;
import cn.saladday.travel.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

public class SellerDaoImpl implements SellerDao {
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    @Override
    public Seller findBySid(int sid) {
        try{
            String sql = "SELECT * FROM tab_seller WHERE sid = ?;";
            return template.queryForObject(sql,new BeanPropertyRowMapper<Seller>(Seller.class),sid);
        }catch (Exception e){
            return null;
        }
    }
}
