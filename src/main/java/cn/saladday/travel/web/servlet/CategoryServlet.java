package cn.saladday.travel.web.servlet;

import cn.saladday.travel.domain.Category;
import cn.saladday.travel.domain.ResultInfo;
import cn.saladday.travel.service.CategoryService;
import cn.saladday.travel.service.impl.CategoryServiceImpl;
import cn.saladday.travel.util.JedisUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@WebServlet("/category/*")
public class CategoryServlet extends BaseServlet{
    private CategoryService  categoryService = new CategoryServiceImpl();
    private ObjectMapper mapper = new ObjectMapper();

    /**
     * 获取所有categories信息
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void findAll(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException{
        //由于category内容不经常改变，因此利用jedis来优化
        Jedis jedis = JedisUtil.getJedis();
        //使用sortedSet数据结构来存储数据以实现有序序列
        Set<Tuple> categories_redis = jedis.zrangeWithScores("categories", 0, -1);


        if(categories_redis==null||categories_redis.size()==0){
            //redis中没有数据，需要去mysql中调取
            ResultInfo info = categoryService.findAll();
            if(info.isFlag()){
                //正常提取到数据了
                //设置返回值为json
                List<Category> categoryList = (List<Category>) info.getData();
                categoryList.sort(new Comparator<Category>() {
                    @Override
                    public int compare(Category o1, Category o2) {
                        return o1.getCid()-o2.getCid();
                    }
                });
//                System.out.println("从sql中取出的数据");
//                System.out.println(categoryList);
                for (Category category : categoryList) {
                    jedis.zadd("categories",category.getCid(),category.getCname());
                }
                resp.setContentType("application/json;charset=utf-8");
                String categoryJson = mapper.writeValueAsString(categoryList);
                resp.getWriter().write(categoryJson);
                return;
                //返回数据
            }else {
                //service层没有正常取得数据
                System.out.println(info.getErrorMsg());
                throw new RuntimeException();
            }

        }else {
            List<Category> categoryList = new ArrayList<Category>();


            for (Tuple item : categories_redis) {
                Category category = new Category();
                category.setCid((int) item.getScore());
                category.setCname(item.getElement());
                categoryList.add(category);
            }
//            System.out.println("从redis中取得的数据");
//            System.out.println(categoryList);
            resp.setContentType("application/json;charset=utf-8");
            String categoryJson = mapper.writeValueAsString(categoryList);
            resp.getWriter().write(categoryJson);
            return;
        }



    }

}
