package com.increff.pos.util;

import com.increff.pos.model.BrandCategoryForm;
import com.increff.pos.model.OrderItemForm;
import com.increff.pos.model.ProductForm;

import java.util.ArrayList;
import java.util.List;

public class Normalize {
    public static void normalizeForm(BrandCategoryForm form) {
        form.setBrand(StringUtil.toLowerCase(form.getBrand()));
        form.setCategory(StringUtil.toLowerCase(form.getCategory()));
    }

    public static void normalizeForm(ProductForm form) {
        form.setName(StringUtil.toLowerCase(form.getName()));
        form.setCategory(StringUtil.toLowerCase(form.getCategory()));
        form.setBrand(StringUtil.toLowerCase(form.getBrand()));
    }

    public static void normalizeForm(List<OrderItemForm> orderItemFormList){
        for(int i=0;i<orderItemFormList.size();i++){
            OrderItemForm orderItemForm = orderItemFormList.get(i);
            orderItemForm.setBarcode(StringUtil.toLowerCase(orderItemForm.getBarcode()));
            orderItemFormList.set(i,orderItemForm);
        }
    }
}
