package cn.saladday.travel.dao.impl;

import cn.saladday.travel.dao.CategoryDao;
import cn.saladday.travel.domain.Category;
import cn.saladday.travel.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class CategoryDaoImpl implements CategoryDao {
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    @Override
    public List<Category> findAll() {
        try{
            String sql = "SELECT * FROM tab_category;";
            List<Category> categoryList = template.query(sql, new BeanPropertyRowMapper<Category>(Category.class));
            return categoryList;
        }catch (Exception e){
            return null;
        }
    }
}
