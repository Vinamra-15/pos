package com.increff.pos.util;

import com.increff.pos.model.*;
import com.increff.pos.pojo.*;
import com.increff.pos.service.ApiException;

import java.util.List;

public class ConvertUtil {
    public static BrandCategoryData convert(BrandCategoryPojo brandCategoryPojo) {
        BrandCategoryData brandCategoryData = new BrandCategoryData();
        brandCategoryData.setCategory(brandCategoryPojo.getCategory());
        brandCategoryData.setBrand(brandCategoryPojo.getBrand());
        brandCategoryData.setId(brandCategoryPojo.getId());
        return brandCategoryData;
    }
    public static BrandCategoryPojo convert(BrandCategoryForm brandCategoryForm) {
        BrandCategoryPojo brandCategoryPojo = new BrandCategoryPojo();
        brandCategoryPojo.setCategory(brandCategoryForm.getCategory());
        brandCategoryPojo.setBrand(brandCategoryForm.getBrand());
        return brandCategoryPojo;
    }
    public static ProductData convert(ProductPojo productPojo, BrandCategoryPojo brandCategoryPojo) throws ApiException {
        ProductData productData = new ProductData();
        productData.setName(productPojo.getName());
        productData.setId(productPojo.getId());
        productData.setBarcode(productPojo.getBarcode());
        productData.setMrp(productPojo.getMrp());
        productData.setBrand(brandCategoryPojo.getBrand());
        productData.setCategory(brandCategoryPojo.getCategory());
        return productData;
    }
    public static ProductPojo convert(ProductForm productForm, BrandCategoryPojo brandCategoryPojo) throws ApiException {

        ProductPojo productPojo = new ProductPojo();
        productPojo.setBarcode(productForm.getBarcode());
        productPojo.setName(productForm.getName());
        productPojo.setMrp(productForm.getMrp());
        productPojo.setBrandId(brandCategoryPojo.getId());
        return productPojo;
    }

    public static InventoryData convert(InventoryPojo inventoryPojo, ProductPojo productPojo) {
        InventoryData inventoryData = new InventoryData();
        inventoryData.setProductName(productPojo.getName());
        inventoryData.setBarcode(productPojo.getBarcode());
        inventoryData.setQuantity(inventoryPojo.getQuantity());
        return inventoryData;
    }

    public static InventoryPojo convert(InventoryForm inventoryForm, ProductPojo productPojo) throws ApiException {
        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setQuantity(inventoryForm.getQuantity());
        inventoryPojo.setProductId(productPojo.getId());
        return inventoryPojo;
    }

    public static OrderItemData convert(OrderItemPojo orderItemPojo,ProductPojo productPojo){
        OrderItemData orderItemData = new OrderItemData();
        orderItemData.setId(orderItemPojo.getId());
        orderItemData.setQuantity(orderItemPojo.getQuantity());
        orderItemData.setName(productPojo.getName());
        orderItemData.setBarcode(productPojo.getBarcode());
        orderItemData.setSellingPrice(orderItemPojo.getSellingPrice());
        return orderItemData;
    }

    public static OrderItemPojo convert(OrderItemForm orderItemForm, ProductPojo productPojo, OrderPojo orderPojo){
        OrderItemPojo orderItemPojo = new OrderItemPojo();
        orderItemPojo.setQuantity(orderItemForm.getQuantity());
        orderItemPojo.setProductId(productPojo.getId());
        orderItemPojo.setOrderId(orderPojo.getId());
        orderItemPojo.setSellingPrice(orderItemForm.getSellingPrice());
        return orderItemPojo;
    }



    public static OrderData convert(OrderPojo orderPojo){
        OrderData orderData = new OrderData();
        orderData.setId(orderPojo.getId());
        orderData.setDatetime(orderPojo.getDatetime());
        return orderData;
    }
}
