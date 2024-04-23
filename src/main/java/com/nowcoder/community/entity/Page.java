package com.nowcoder.community.entity;

public class Page {

    private int current = 1;
    private int limit = 10; //每页限制
    private int rows; //一共多少页

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        if (current >= 1) {
            this.current = current;
        }
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        if (limit >= 1 && limit < 100) {
            this.limit = limit;
        }
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        if (rows >= 0) {
            this.rows = rows;
        }
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    private String path;

    //获取当前页的起始行
    public int getOffset(){
        System.out.println("current"+current);
        return (current - 1) * limit;
    }
    //获取总页数
    public int getTotal() {
        if (rows % limit == 0) {
            return rows / limit;
        } else {
            return rows / limit + 1;
        }
    }

    //显示的起始页码
    public int getFrom(){
            int from = current - 2;
            if (from < 1){
                return 1;
            }else{
                return from;
            }
        }
    //结束页码
    public int getTo(){
            int to = current + 2;
            int total = getTotal();
            return to > total ? total : to;

        }

    }





