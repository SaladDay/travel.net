package cn.saladday.travel.dao.mapper;

import cn.saladday.travel.domain.Category;

import java.util.List;

public interface CategoryMapper {

    /**
     * 查找所有category
     * @return
     */
    List<Category> findAll();
}
