package com.sale_system.dao.impl;

import com.sale_system.dao.BaseDao;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

public abstract class BaseDaoImpl<T> extends SqlSessionDaoSupport implements BaseDao<T> {
    @Autowired
    public void setSqlSessionFactory(SqlSessionFactory sessionFactory){
        super.setSqlSessionFactory(sessionFactory);
    }
    private String ns;
    public String getNs(){
        return ns;
    }
    public void setNs(String ns){
        this.ns = ns;
    }

    @Override
    public T selectById(int id) {
        return this.getSqlSession().selectOne(ns + ".selectById", id);
    }

    @Override
    public List selectAll(Map map) {
        List<T> oList = this.getSqlSession().selectList(ns + ".selectAll", map);
        return oList;
    }

    @Override
    public void insert(T entity) {
        this.getSqlSession().insert(ns + ".insert", entity);
    }

    @Override
    public void update(T entity) {
        this.getSqlSession().update(ns + ".update", entity);
    }

    @Override
    public void deleteById(int id) {
        this.getSqlSession().delete(ns + ".deleteById", id);
    }
}
