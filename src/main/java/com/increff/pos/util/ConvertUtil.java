package com.increff.pos.util;

import com.increff.pos.model.*;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;

public class ConvertUtil {
    public static BrandData convertBrandPojoToBrandData(BrandPojo p) {
        BrandData d = new BrandData();
        d.setCategory(p.getCategory());
        d.setName(p.getName());
        d.setId(p.getId());
        return d;
    }

    public static BrandPojo convertBrandFormToBrandPojo(BrandForm form) {
        BrandPojo p = new BrandPojo();
        p.setCategory(form.getCategory());
        p.setName(form.getName());
        return p;
    }


    public static ProductData convertProductPojoToProductData(ProductPojo productPojo, BrandPojo brandPojo) throws ApiException {
        ProductData d = new ProductData();
        d.setName(productPojo.getName());
        d.setId(productPojo.getId());
        d.setBarcode(productPojo.getBarcode());
        d.setMrp(Double.toString(productPojo.getMrp()));
        d.setBrandName(brandPojo.getName());
        d.setBrandCategory(brandPojo.getCategory());
        return d;
    }

    public static ProductPojo convertProductFormToProductPojo(ProductForm productForm, BrandPojo brandPojo) throws ApiException {

        ProductPojo p = new ProductPojo();
        p.setBarcode(productForm.getBarcode());
        p.setName(productForm.getName());
        p.setMrp(Double.parseDouble(productForm.getMrp()));
        p.setBrandId(brandPojo.getId());
        return p;
    }


    public static InventoryData convertInventoryPojoToInventoryData(InventoryPojo inventoryPojo, ProductPojo productPojo) {
        InventoryData inventoryData = new InventoryData();
        inventoryData.setProductName(productPojo.getName());
        inventoryData.setBarcode(productPojo.getBarcode());
        inventoryData.setQuantity(inventoryPojo.getQuantity());
        return inventoryData;
    }

    public static InventoryPojo convertInventoryFormToInventoryPojo(InventoryForm inventoryForm, ProductPojo productPojo) throws ApiException {
        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setQuantity(inventoryForm.getQuantity());
        inventoryPojo.setProductId(productPojo.getId());
        return inventoryPojo;
    }


}
