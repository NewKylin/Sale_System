package com.sale_system.service.impl;

import com.sale_system.dao.CommodityDao;
import com.sale_system.po.Commodity;
import com.sale_system.service.CommodityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CommodityServiceImpl implements CommodityService {

    @Autowired
    private CommodityDao commodityDao;

    @Override
    public Commodity selectById(int id) {
        return commodityDao.selectById(id);
    }

    @Override
    public List<Commodity> selectAll(Map map) {
        return commodityDao.selectAll(map);
    }

    @Override
    public void insert(Commodity commodties) {
        commodityDao.insert(commodties);
    }

    @Override
    public void update(Commodity commodties) {
        commodityDao.update(commodties);
    }

    @Override
    public void deleteById(int id) {
        commodityDao.deleteById(id);
    }
}
