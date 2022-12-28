package com.increff.pos.dao;

import com.increff.pos.pojo.OrderPojo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
@Transactional
public class OrderDao extends AbstractDao{

    private static String select_id = "select p from OrderPojo p where id=:id";
    private static String select_all = "select p from OrderPojo p";
    @PersistenceContext
    private EntityManager em;
    public void insert(OrderPojo orderPojo) {
        em.persist(orderPojo);
    }

    public OrderPojo select(Integer id) {
        TypedQuery<OrderPojo> query = getQuery(select_id, OrderPojo.class);
        query.setParameter("id", id);
        return getSingle(query);
    }

    public List<OrderPojo> selectAll() {
        TypedQuery<OrderPojo> query = getQuery(select_all, OrderPojo.class);
        return query.getResultList();
    }


    public void update(OrderPojo p) {
        em.merge(p);
    }
}
