package com.increff.pos.util;

import com.increff.pos.model.BrandCategoryForm;
import com.increff.pos.model.ProductForm;

public class Normalize {
    public static void normalizeForm(BrandCategoryForm form) {
        form.setBrand(StringUtil.toLowerCase(form.getBrand()));
        form.setCategory(StringUtil.toLowerCase(form.getCategory()));
    }

    public static void normalizeForm(ProductForm form) {
        form.setName(StringUtil.toLowerCase(form.getName()));
        form.setCategory(StringUtil.toLowerCase(form.getCategory()));
        form.setBrand(StringUtil.toLowerCase(form.getBrand()));
    }
}
