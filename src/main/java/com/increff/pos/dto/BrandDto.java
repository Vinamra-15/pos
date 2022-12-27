package com.increff.pos.dto;

import com.increff.pos.model.BrandData;
import com.increff.pos.model.BrandForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
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
public class BrandDto {

    @Autowired
    private BrandService brandService;


    public void add(BrandForm form) throws ApiException {
        normalizeBrandForm(form);
        validateBrandForm(form);
        BrandPojo p = convertBrandFormToBrandPojo(form);
		brandService.add(p);
    }

    public BrandData get(Integer id) throws ApiException {
        BrandPojo pojo = brandService.get(id);
        return convertBrandPojoToBrandData(pojo);
    }

    public List<BrandData> getAll()
    {
        List<BrandPojo> list = brandService.getAll();
        List<BrandData> list2 = new ArrayList<BrandData>();
        for (BrandPojo p : list) {
            list2.add(convertBrandPojoToBrandData(p));
        }
        Collections.reverse(list2);
        return list2;
    }

    public void update(Integer id, BrandForm form) throws ApiException {
        normalizeBrandForm(form);
        validateBrandForm(form);
        BrandPojo p = convertBrandFormToBrandPojo(form);
        brandService.update(id, p);
    }



}
