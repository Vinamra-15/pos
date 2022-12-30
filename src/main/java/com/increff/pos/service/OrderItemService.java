package com.increff.pos.service;

import com.increff.pos.dao.OrderItemDao;
import com.increff.pos.pojo.OrderItemPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class OrderItemService {
    @Autowired
    private OrderItemDao orderItemDao;
    public List<OrderItemPojo> getByOrderId(Integer id){
        return orderItemDao.selectByOrderId(id);
    }

    public void add(OrderItemPojo orderItemPojo){
        orderItemDao.insert(orderItemPojo);
    }


}
