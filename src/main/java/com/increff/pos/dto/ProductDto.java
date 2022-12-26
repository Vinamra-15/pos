package com.increff.pos.dto;

import com.increff.pos.model.ProductData;
import com.increff.pos.model.ProductForm;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.ProductService;
import com.increff.pos.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.RollbackException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class ProductDto {

    @Autowired
    private ProductService productService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private InventoryService inventoryService;


//    @Transactional(rollbackOn = ApiException.class)
    public void add(ProductForm form) throws ApiException {
        normalize(form);
        validate(form);
        ProductPojo productPojo = convert(form);
        productService.add(productPojo);

        //error induce

        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setProductId(productPojo.getId());
        inventoryPojo.setQuantity(0);
        inventoryService.add(inventoryPojo);



    }

    public ProductData get(int id) throws ApiException {
        ProductPojo productPojo = productService.get(id);
        return convert(productPojo);
    }

    public List<ProductData> getAll() throws ApiException {
        List<ProductPojo> list = productService.getAll();
        List<ProductData> list2 = new ArrayList<ProductData>();
        for (ProductPojo productPojo : list) {
            list2.add(convert(productPojo));

        }
        Collections.reverse(list2);
        return list2;

    }

    public void update(int id, ProductForm form) throws ApiException {
        normalize(form);
        validate(form);
        ProductPojo productPojo = convert(form);
        productService.update(id, productPojo);
    }



    private ProductData convert(ProductPojo p) throws ApiException {
        ProductData d = new ProductData();
        d.setName(p.getName());
        d.setId(p.getId());
        d.setBarcode(p.getBarcode());
        d.setMrp(Double.toString(p.getMrp()));

        d.setBrandName(brandService.get(p.getBrandId()).getName());
        d.setBrandCategory(brandService.get(p.getBrandId()).getCategory());
        return d;
    }

    private ProductPojo convert(ProductForm f) throws ApiException {


        ProductPojo p = new ProductPojo();
        p.setBarcode(f.getBarcode());
        p.setName(f.getName());
        p.setMrp(Double.parseDouble(f.getMrp()));
        p.setBrandId(brandService.getCheckWithBrandCategory(f.getBrandName(),f.getBrandCategory()).getId());
        return p;
    }

    private static void validate(ProductForm form) throws ApiException {
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
    protected static void normalize(ProductForm form) {
        form.setName(StringUtil.toLowerCase(form.getName()));
        form.setBrandCategory(StringUtil.toLowerCase(form.getBrandCategory()));
        form.setBrandName(StringUtil.toLowerCase(form.getBrandName()));
    }

}
