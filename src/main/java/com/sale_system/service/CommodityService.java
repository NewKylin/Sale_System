package com.sale_system.service;

import com.sale_system.po.Commodity;

import java.util.List;
import java.util.Map;

public interface CommodityService {
    public Commodity selectById(int id);
    public List<Commodity> selectAll(Map map);//根据条件查询多个结果
    public void insert(Commodity commodties);//插入，用实体作为参数
    public void update(Commodity commodties);//修改，用实体作为参数
    public void deleteById(int id);//按id删除，删除一条
}
