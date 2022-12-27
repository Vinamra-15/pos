package com.increff.pos.service;

import com.increff.pos.dao.InventoryDao;
import com.increff.pos.pojo.InventoryPojo;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class InventoryService {
    @Autowired
    private InventoryDao dao;


//    @Transactional(rollbackOn = ApiException.class)
    public void add(InventoryPojo inventoryPojo) throws ApiException {
        dao.insert(inventoryPojo);
    }


    @Transactional(rollbackOn = ApiException.class)
    public InventoryPojo get(Integer productId) throws ApiException {
        return getCheck(productId);
    }

    @Transactional
    public List<InventoryPojo> getAll() {
        return dao.selectAll();
    }



    @Transactional(rollbackOn  = ApiException.class)
    public void update(Integer productId, InventoryPojo inventoryPojo) throws ApiException {

        InventoryPojo ex = getCheck(productId);
        ex.setProductId(inventoryPojo.getProductId());
        ex.setQuantity(inventoryPojo.getQuantity());
        dao.update(ex);
    }

    @Transactional
    public InventoryPojo getCheck(Integer productId) throws ApiException {
        InventoryPojo p = dao.select(productId);
        if (p == null) {
            throw new ApiException("Product with given barcode does not exist");
        }
        return p;
    }
}
