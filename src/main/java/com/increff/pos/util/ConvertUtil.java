package com.increff.pos.util;

import com.increff.pos.model.*;
import com.increff.pos.pojo.BrandCategoryPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;

public class ConvertUtil {
    public static BrandCategoryData convertBrandPojoToBrandData(BrandCategoryPojo p) {
        BrandCategoryData d = new BrandCategoryData();
        d.setCategory(p.getCategory());
        d.setBrand(p.getBrand());
        d.setId(p.getId());
        return d;
    }

    public static BrandCategoryPojo convertBrandFormToBrandPojo(BrandCategoryForm form) {
        BrandCategoryPojo p = new BrandCategoryPojo();
        p.setCategory(form.getCategory());
        p.setBrand(form.getBrand());
        return p;
    }


    public static ProductData convertProductPojoToProductData(ProductPojo productPojo, BrandCategoryPojo brandCategoryPojo) throws ApiException {
        ProductData d = new ProductData();
        d.setName(productPojo.getName());
        d.setId(productPojo.getId());
        d.setBarcode(productPojo.getBarcode());
        d.setMrp(productPojo.getMrp());
        d.setBrandName(brandCategoryPojo.getBrand());
        d.setBrandCategory(brandCategoryPojo.getCategory());
        return d;
    }

    public static ProductPojo convertProductFormToProductPojo(ProductForm productForm, BrandCategoryPojo brandCategoryPojo) throws ApiException {

        ProductPojo p = new ProductPojo();
        p.setBarcode(productForm.getBarcode());
        p.setName(productForm.getName());
        p.setMrp(productForm.getMrp());
        p.setBrandId(brandCategoryPojo.getId());
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
