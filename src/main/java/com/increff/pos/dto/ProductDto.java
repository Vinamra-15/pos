package com.increff.pos.dto;

import com.increff.pos.model.ProductData;
import com.increff.pos.model.ProductForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.ProductService;
import com.increff.pos.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static com.increff.pos.util.ConvertUtil.convertProductFormToProductPojo;
import static com.increff.pos.util.ConvertUtil.convertProductPojoToProductData;
import static com.increff.pos.util.Normalize.normalizeProductForm;
import static com.increff.pos.util.Validate.validateProductForm;

@Component
public class ProductDto {

    @Autowired
    private ProductService productService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private InventoryService inventoryService;


    public void add(ProductForm productForm) throws ApiException {
        normalizeProductForm(productForm);
        validateProductForm(productForm);
        BrandPojo brandPojo = brandService.getCheckWithBrandCategory(productForm.getBrandName(),productForm.getBrandCategory());
        ProductPojo productPojo = convertProductFormToProductPojo(productForm,brandPojo);
        productService.add(productPojo);

        //For every new added product, initialize its quantity in Inventory to Zero.

        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setProductId(productPojo.getId());
        inventoryPojo.setQuantity(0);
        inventoryService.add(inventoryPojo);

    }

    public ProductData get(int id) throws ApiException {
        ProductPojo productPojo = productService.get(id);
        BrandPojo brandPojo = brandService.get(productPojo.getBrandId());
        return convertProductPojoToProductData(productPojo,brandPojo);
    }

    public List<ProductData> getAll() throws ApiException {
        List<ProductPojo> list = productService.getAll();
        List<ProductData> list2 = new ArrayList<ProductData>();
        for (ProductPojo productPojo : list) {
            BrandPojo brandPojo = brandService.get(productPojo.getBrandId());
            list2.add(convertProductPojoToProductData(productPojo,brandPojo));
        }
        Collections.reverse(list2);
        return list2;

    }

    public void update(int id, ProductForm productForm) throws ApiException {
        normalizeProductForm(productForm);
        validateProductForm(productForm);

        BrandPojo brandPojo = brandService.getCheckWithBrandCategory(productForm.getBrandName(),productForm.getBrandCategory());
        ProductPojo productPojo = convertProductFormToProductPojo(productForm,brandPojo);
        productService.update(id, productPojo);
    }



}
