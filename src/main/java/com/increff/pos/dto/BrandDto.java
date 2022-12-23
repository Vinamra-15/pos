package com.increff.pos.dto;

import com.increff.pos.model.BrandData;
import com.increff.pos.model.BrandForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BrandDto {

    @Autowired
    private BrandService brandService;


    public void add(BrandForm form) throws ApiException {
        normalize(form);
        validate(form);
        BrandPojo p = convert(form);
		brandService.add(p);
    }

    public BrandData get(int id) throws ApiException {
        BrandPojo pojo = brandService.get(id);
        return convert(pojo);
    }

    public List<BrandData> getAll()
    {
        List<BrandPojo> list = brandService.getAll();
        List<BrandData> list2 = new ArrayList<BrandData>();
        for (BrandPojo p : list) {
            list2.add(convert(p));
        }
        return list2;
    }

    public void update(int id, BrandForm form) throws ApiException {
        normalize(form);
        validate(form);
        BrandPojo p = convert(form);
        brandService.update(id, p);
    }




    private static BrandData convert(BrandPojo p) {
        BrandData d = new BrandData();
        d.setCategory(p.getCategory());
        d.setName(p.getName());
        d.setId(p.getId());
        return d;
    }

    private static BrandPojo convert(BrandForm form) {
        BrandPojo p = new BrandPojo();
        p.setCategory(form.getCategory());
        p.setName(form.getName());
        return p;
    }
    private static void validate(BrandForm form) throws ApiException {
        if(StringUtil.isEmpty(form.getName())||StringUtil.isEmpty(form.getCategory())) {
            throw new ApiException("Field(s) cannot be empty");
        }
    }
    protected static void normalize(BrandForm form) {
        form.setName(StringUtil.toLowerCase(form.getName()));
        form.setCategory(StringUtil.toLowerCase(form.getCategory()));
    }
}
