package cn.saladday.travel.service.impl;

import cn.saladday.travel.dao.mapper.CategoryMapper;
import cn.saladday.travel.domain.Category;
import cn.saladday.travel.domain.ResultInfo;
import cn.saladday.travel.service.CategoryService;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class CategoryServiceImpl implements CategoryService {
    private static SqlSessionFactory factory;
    static {
        try {
            InputStream is = Resources.getResourceAsStream("mybatis-config.xml");
            factory = new SqlSessionFactoryBuilder().build(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public ResultInfo findAll() {

            SqlSession sqlSession = factory.openSession();
            CategoryMapper mapper = sqlSession.getMapper(CategoryMapper.class);
            List<Category> categories = mapper.findAll();
            ResultInfo info = new ResultInfo();

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
