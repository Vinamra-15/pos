package com.increff.pos.util;

import com.increff.pos.model.BrandForm;
import com.increff.pos.model.ProductForm;

public class Normalize {
    public static void normalizeBrandForm(BrandForm form) {
        form.setName(StringUtil.toLowerCase(form.getName()));
        form.setCategory(StringUtil.toLowerCase(form.getCategory()));
    }

    public static void normalizeProductForm(ProductForm form) {
        form.setName(StringUtil.toLowerCase(form.getName()));
        form.setBrandCategory(StringUtil.toLowerCase(form.getBrandCategory()));
        form.setBrandName(StringUtil.toLowerCase(form.getBrandName()));
    }
}
