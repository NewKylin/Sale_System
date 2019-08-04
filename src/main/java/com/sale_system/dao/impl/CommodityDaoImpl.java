package com.sale_system.dao.impl;

import com.sale_system.dao.CommodityDao;
import com.sale_system.po.Commodity;
import org.springframework.stereotype.Repository;

@Repository
public class CommodityDaoImpl extends BaseDaoImpl<Commodity> implements CommodityDao {
    public CommodityDaoImpl(){
        super.setNs("com.sale_system.mapper.CommodityMapper");
    }
}
