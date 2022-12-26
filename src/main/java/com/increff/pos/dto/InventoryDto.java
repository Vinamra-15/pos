package com.increff.pos.dto;


import com.increff.pos.model.InventoryForm;
import com.increff.pos.model.InventoryData;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.ProductService;
import com.increff.pos.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class InventoryDto {
    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private ProductService productService;


    public void add(InventoryForm form) throws ApiException{
        validate(form);
        InventoryPojo pojo = convert(form);
        inventoryService.add(pojo);
    }

    public InventoryData get(String barcode) throws ApiException {
        ProductPojo productPojo = productService.getByBarcode(barcode);
        InventoryPojo inventoryPojo = inventoryService.get(productPojo.getId());
        return convert(inventoryPojo);
    }

    public List<InventoryData> getAll() throws ApiException {
        List<InventoryPojo> list = inventoryService.getAll();
        List<InventoryData> list2 = new ArrayList<InventoryData>();
        for (InventoryPojo p : list) {
            list2.add(convert(p));

        }

        Collections.reverse(list2);

        return list2;
    }

    public void update(String barcode, InventoryForm form) throws ApiException {
        try{
            validate(form);
            InventoryPojo inventoryPojo = convert(form);
            ProductPojo productPojo = productService.getByBarcode(barcode);
            inventoryService.update(productPojo.getId(), inventoryPojo);

        }
        catch (Exception e)
        {
            throw new ApiException(e.toString());
        }

    }

    private static void validate(InventoryForm form) throws ApiException {


        if(StringUtil.isEmpty(form.getBarcode())||form.getQuantity()==null) {
            throw new ApiException("Field(s) cannot be empty");
        }

        if(form.getQuantity()<0)
        {
            throw new ApiException("Quantity must be a non-negative number(integer)");
        }
    }


    private InventoryData convert(InventoryPojo inventoryPojo) throws ApiException {
        InventoryData inventoryData = new InventoryData();
        ProductPojo productPojo = productService.get(inventoryPojo.getProductId());
        inventoryData.setBarcode(productPojo.getBarcode());
        inventoryData.setProductName(productPojo.getName());
        inventoryData.setQuantity(inventoryPojo.getQuantity());
        return inventoryData;

    }



    private InventoryPojo convert(InventoryForm inventoryForm) throws ApiException {
        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setQuantity(inventoryForm.getQuantity());
        inventoryPojo.setProductId(productService.getByBarcode(inventoryForm.getBarcode()).getId());
        return inventoryPojo;
    }


}
