package com.sale_system.dao;

import java.util.List;
import java.util.Map;

public interface BaseDao<T> {
    public T selectById(int id);//只查询一个，常用于修改

    public List<T> selectAll(Map map);//根据条件查询多个结果

    public void insert(T entity);//插入，用实体作为参数

    public void update(T entity);//修改，用实体作为参数

    public void deleteById(int id);//按id删除，删除一条
}
