package com.increff.pos.util;

import com.increff.pos.model.BrandForm;
import com.increff.pos.model.InventoryForm;
import com.increff.pos.model.ProductForm;
import com.increff.pos.service.ApiException;

public class Validate {
    public static void validateBrandForm(BrandForm form) throws ApiException {
        if(StringUtil.isEmpty(form.getName())||StringUtil.isEmpty(form.getCategory())) {
            throw new ApiException("Field(s) cannot be empty");
        }
    }
    public static void validateInventoryForm(InventoryForm inventoryForm) throws ApiException {

        if(StringUtil.isEmpty(inventoryForm.getBarcode())|| inventoryForm.getQuantity()==null) {
            throw new ApiException("Field(s) cannot be empty");
        }

        if(inventoryForm.getQuantity()<0)
        {
            throw new ApiException("Quantity must be a non-negative number(integer)");
        }
    }

    public static void validateProductForm(ProductForm form) throws ApiException {
        double mrp = 0;
        try{
            mrp = Double.parseDouble(form.getMrp());
        }
        catch (Exception exception)
        {
            throw new ApiException("MRP must be a number(double)");
        }



        if(StringUtil.isEmpty(form.getName())||StringUtil.isEmpty(form.getBarcode())||StringUtil.isEmpty(form.getBrandName())||StringUtil.isEmpty(form.getBrandCategory())) {
            throw new ApiException("Field(s) cannot be empty");
        }

        if(mrp<0)
        {
            throw new ApiException("MRP must be a non-negative number(double)");
        }
    }
}
