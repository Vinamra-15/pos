package com.increff.pos.dao;

import com.increff.pos.pojo.DaySalesPojo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
@Transactional
public class DaySalesDao {

    @PersistenceContext
    private EntityManager em;
    public void insert(DaySalesPojo daySalesPojo){
        em.persist(daySalesPojo);
    }
}
