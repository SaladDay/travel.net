package cn.saladday.travel.service.impl;

import cn.saladday.travel.dao.CategoryDao;
import cn.saladday.travel.dao.impl.CategoryDaoImpl;
import cn.saladday.travel.domain.Category;
import cn.saladday.travel.domain.ResultInfo;
import cn.saladday.travel.service.CategoryService;

import java.util.List;

public class CategoryServiceImpl implements CategoryService {
    private CategoryDao dao = new CategoryDaoImpl();

    @Override
    public ResultInfo findAll() {
        ResultInfo info = new ResultInfo();

        List<Category> categories = dao.findAll();
        if(categories==null){
            info.setFlag(false);
            info.setErrorMsg("dao查询错误");
        }else {
            info.setFlag(true);
            info.setData(categories);
        }
        return info;
    }
}
