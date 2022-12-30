package com.increff.pos.util;

import com.increff.pos.model.BrandCategoryForm;
import com.increff.pos.model.InventoryForm;
import com.increff.pos.model.OrderItemForm;
import com.increff.pos.model.ProductForm;
import com.increff.pos.service.ApiException;

import java.util.List;

public class Validate {
    public static void validateForm(BrandCategoryForm brandCategoryForm) throws ApiException {
        if(StringUtil.isEmpty(brandCategoryForm.getBrand())||StringUtil.isEmpty(brandCategoryForm.getCategory())) {
            throw new ApiException("Field(s) cannot be empty");
        }
    }
    public static void validateForm(InventoryForm inventoryForm) throws ApiException {

        if(StringUtil.isEmpty(inventoryForm.getBarcode())|| inventoryForm.getQuantity()==null) {
            throw new ApiException("Field(s) cannot be empty");
        }

        if(inventoryForm.getQuantity()<0)
        {
            throw new ApiException("Quantity must be a non-negative number(integer)");
        }
    }

    public static void validateForm(ProductForm productForm) throws ApiException {

        if(StringUtil.isEmpty(productForm.getName())||StringUtil.isEmpty(productForm.getBarcode())||StringUtil.isEmpty(productForm.getBrand())||StringUtil.isEmpty(productForm.getCategory())||productForm.getMrp()==null) {
            throw new ApiException("Field(s) cannot be empty");
        }


        if(productForm.getMrp()<0)
        {
            throw new ApiException("MRP must be a non-negative number!");
        }
    }

    public static void validateForm(List<OrderItemForm> orderItemForms) throws ApiException {
        for(OrderItemForm orderItemForm:orderItemForms)
            validateForm(orderItemForm);
    }

    public static void validateForm(OrderItemForm orderItemForm) throws ApiException {
        if(StringUtil.isEmpty(orderItemForm.getBarcode())||orderItemForm.getQuantity()==null) {
            throw new ApiException("Field(s) cannot be empty");
        }
        if(orderItemForm.getQuantity()<0){
            throw new ApiException("Quantity must be a non-negative number!");
        }
    }
}
