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

    public OrderItemPojo getByOrderIdProductId(Integer orderId, Integer productId) throws ApiException {
        OrderItemPojo orderItemPojo = orderItemDao.selectByOrderIdProductId(orderId,productId);
        if(orderItemPojo==null){
            throw new ApiException("No order item for concerned orderId and productId exists!");
        }
        return orderItemPojo;
    }

    public void add(OrderItemPojo orderItemPojo){
        orderItemDao.insert(orderItemPojo);
    }


}
