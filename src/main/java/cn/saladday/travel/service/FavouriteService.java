package cn.saladday.travel.service;

import cn.saladday.travel.domain.ResultInfo;

public interface FavouriteService {

    ResultInfo isFavourite(int uid,int rid);
    Boolean addFavourite(int uid,int rid);

    Boolean removeFavourite(int uid, int rid);
    ResultInfo pageQuery(int uid,String rname, int currentPage,int pageSize);
}

