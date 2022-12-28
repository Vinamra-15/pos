package com.increff.pos.dao;

import com.increff.pos.pojo.OrderItemPojo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
@Transactional
public class OrderItemDao extends AbstractDao{
    private static String select_id = "select p from OrderItemPojo p where id=:id";
    private static String select_orderId = "select p from OrderItemPojo p where orderId=:orderId";
    private static String select_all = "select p from OrderItemPojo p";

    @PersistenceContext
    private EntityManager em;
    public void insert(OrderItemPojo orderItemPojo) {
        em.persist(orderItemPojo);
    }

    public OrderItemPojo select(Integer id) {
        TypedQuery<OrderItemPojo> query = getQuery(select_id, OrderItemPojo.class);
        query.setParameter("id", id);
        return getSingle(query);
    }

    public List<OrderItemPojo> selectByOrderId(Integer orderId){
        TypedQuery<OrderItemPojo> query = getQuery(select_orderId, OrderItemPojo.class);
        query.setParameter("orderId", orderId);
        return query.getResultList();
    }

    public List<OrderItemPojo> selectAll() {
        TypedQuery<OrderItemPojo> query = getQuery(select_all, OrderItemPojo.class);
        return query.getResultList();
    }


    public void update(OrderItemPojo p) {
        em.merge(p);
    }


}
