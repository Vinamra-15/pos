package com.increff.pos.dto;

import com.increff.pos.model.BrandCategoryData;
import com.increff.pos.model.BrandCategoryForm;
import com.increff.pos.pojo.BrandCategoryPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.increff.pos.util.ConvertUtil.convertBrandFormToBrandPojo;
import static com.increff.pos.util.ConvertUtil.convertBrandPojoToBrandData;
import static com.increff.pos.util.Normalize.normalizeBrandForm;
import static com.increff.pos.util.Validate.validateBrandForm;

@Component
public class BrandCategoryDto {

    @Autowired
    private BrandCategoryService brandCategoryService;


    public void add(BrandCategoryForm brandCategoryForm) throws ApiException {
        normalizeBrandForm(brandCategoryForm);
        validateBrandForm(brandCategoryForm);
        BrandCategoryPojo p = convertBrandFormToBrandPojo(brandCategoryForm);
		brandCategoryService.add(p);
    }

    public BrandCategoryData get(Integer id) throws ApiException {
        BrandCategoryPojo pojo = brandCategoryService.get(id);
        return convertBrandPojoToBrandData(pojo);
    }

    public List<BrandCategoryData> getAll()
    {
        List<BrandCategoryPojo> list = brandCategoryService.getAll();
        List<BrandCategoryData> list2 = new ArrayList<BrandCategoryData>();
        for (BrandCategoryPojo p : list) {
            list2.add(convertBrandPojoToBrandData(p));
        }
        Collections.reverse(list2);
        return list2;
    }

    public void update(Integer id, BrandCategoryForm form) throws ApiException {
        normalizeBrandForm(form);
        validateBrandForm(form);
        BrandCategoryPojo p = convertBrandFormToBrandPojo(form);
        brandCategoryService.update(id, p);
    }



}
