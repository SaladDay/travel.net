package cn.saladday.travel.dao;

import cn.saladday.travel.domain.Category;

import java.util.List;

public interface CategoryDao {

    /**
     * 查找所有category
     * @return
     */
    public List<Category> findAll();
}
