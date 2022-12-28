package cn.saladday.travel.dao;

import cn.saladday.travel.domain.Seller;

public interface SellerDao {

    Seller findBySid(int sid);
}
