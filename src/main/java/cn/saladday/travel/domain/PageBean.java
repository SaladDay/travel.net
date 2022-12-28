package cn.saladday.travel.domain;

import java.util.List;

public class PageBean<T> {

    private int totalCount;
    private int totalPage;
    private int currentPage;
    private int pageSize;

    private List<T> list;




    public PageBean() {
    }

    public PageBean(int totalCount, int totalPage, int currentPage, int pageSize, List<T> list) {
        this.totalCount = totalCount;
        this.totalPage = totalPage;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.list = list;
    }

    /**
     * 获取
     * @return totalCount
     */
    public int getTotalCount() {
        return totalCount;
    }

    /**
     * 设置
     * @param totalCount
     */
    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    /**
     * 获取
     * @return totalPage
     */
    public int getTotalPage() {
        return totalPage;
    }

    /**
     * 设置
     * @param totalPage
     */
    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    /**
     * 获取
     * @return currentPage
     */
    public int getCurrentPage() {
        return currentPage;
    }

    /**
     * 设置
     * @param currentPage
     */
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    /**
     * 获取
     * @return pageSize
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * 设置
     * @param pageSize
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * 获取
     * @return list
     */
    public List<T> getList() {
        return list;
    }

    /**
     * 设置
     * @param list
     */
    public void setList(List<T> list) {
        this.list = list;
    }

    public String toString() {
        return "PageBean{totalCount = " + totalCount + ", totalPage = " + totalPage + ", currentPage = " + currentPage + ", pageSize = " + pageSize + ", list = " + list + "}";
    }
}
