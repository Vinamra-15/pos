package com.increff.pos.dto;

import com.increff.pos.model.SalesReportData;
import com.increff.pos.model.SalesReportForm;
import com.increff.pos.pojo.BrandCategoryPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ReportDto {
    @Autowired
    private BrandCategoryService brandCategoryService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private ProductService productService;


    public List<SalesReportData> getSalesReport(SalesReportForm salesReportForm) throws ApiException {
        Date startDate = salesReportForm.getStartDate();
        Date endDate = salesReportForm.getEndDate();
        String brand = salesReportForm.getBrand();
        String category = salesReportForm.getCategory();

        if(startDate==null)
            startDate = new GregorianCalendar(2020, Calendar.JANUARY,1).getTime();

        if(endDate==null)
            endDate = new Date();

        List<OrderPojo> orderPojos = orderService.getByStartDateEndDate(startDate,endDate);
        List<OrderItemPojo> orderItemPojos = new ArrayList<OrderItemPojo>();
        for(OrderPojo orderPojo:orderPojos){
            List<OrderItemPojo> orderItemPojoList = orderItemService.getByOrderId(orderPojo.getId());
            orderItemPojos.addAll(orderItemPojoList);
        }


        List<SalesReportData> salesReportDataList = new ArrayList<SalesReportData>();
        if(brand.equals("")&&category.equals("")){
            List<BrandCategoryPojo> brandCategoryPojos = brandCategoryService.getAll();
            for(BrandCategoryPojo brandCategoryPojo:brandCategoryPojos){
                SalesReportData salesReportData = new SalesReportData();
                salesReportData.setBrand(brandCategoryPojo.getBrand());
                salesReportData.setCategory(brandCategoryPojo.getCategory());
                Integer quantity = 0;
                Double revenue = 0.0;
                for(OrderItemPojo orderItemPojo:orderItemPojos){
                    ProductPojo productPojo = productService.get(orderItemPojo.getProductId());
                    if(productPojo.getBrandId()==brandCategoryPojo.getId()){
                        quantity += orderItemPojo.getQuantity();
                        revenue += (orderItemPojo.getQuantity())*(orderItemPojo.getSellingPrice());
                    }
                }
                salesReportData.setQuantity(quantity);
                salesReportData.setRevenue(revenue);
                salesReportDataList.add(salesReportData);

            }
        }
        else if(brand.equals("")){
            List<BrandCategoryPojo> brandCategoryPojos = brandCategoryService.getByCategory(category);
            for(BrandCategoryPojo brandCategoryPojo:brandCategoryPojos){
                SalesReportData salesReportData = new SalesReportData();
                salesReportData.setCategory(brandCategoryPojo.getCategory());
                salesReportData.setBrand(brandCategoryPojo.getBrand());
                Integer quantity = 0;
                Double revenue = 0.0;
                for(OrderItemPojo orderItemPojo:orderItemPojos){
                    ProductPojo productPojo = productService.get(orderItemPojo.getProductId());
                    if(productPojo.getBrandId()==brandCategoryPojo.getId()){
                        quantity += orderItemPojo.getQuantity();
                        revenue += (orderItemPojo.getQuantity())*(orderItemPojo.getSellingPrice());
                    }
                }
                salesReportData.setQuantity(quantity);
                salesReportData.setRevenue(revenue);
                salesReportDataList.add(salesReportData);

            }

        }
        else if(category.equals("")){
            List<BrandCategoryPojo> brandCategoryPojos = brandCategoryService.getByBrand(brand);
            for(BrandCategoryPojo brandCategoryPojo:brandCategoryPojos){
                SalesReportData salesReportData = new SalesReportData();
                salesReportData.setCategory(brandCategoryPojo.getCategory());
                salesReportData.setBrand(brandCategoryPojo.getBrand());
                Integer quantity = 0;
                Double revenue = 0.0;
                for(OrderItemPojo orderItemPojo:orderItemPojos){
                    ProductPojo productPojo = productService.get(orderItemPojo.getProductId());
                    if(productPojo.getBrandId()==brandCategoryPojo.getId()){
                        quantity += orderItemPojo.getQuantity();
                        revenue += (orderItemPojo.getQuantity())*(orderItemPojo.getSellingPrice());
                    }
                }
                salesReportData.setQuantity(quantity);
                salesReportData.setRevenue(revenue);
                salesReportDataList.add(salesReportData);

            }
        }
        else{
            BrandCategoryPojo brandCategoryPojo = brandCategoryService.getByBrandCategory(brand,category);
            SalesReportData salesReportData = new SalesReportData();
            salesReportData.setCategory(brandCategoryPojo.getCategory());
            salesReportData.setBrand(brandCategoryPojo.getBrand());
            Integer quantity = 0;
            Double revenue = 0.0;
            for(OrderItemPojo orderItemPojo:orderItemPojos){
                ProductPojo productPojo = productService.get(orderItemPojo.getProductId());
                if(productPojo.getBrandId()==brandCategoryPojo.getId()){
                    quantity += orderItemPojo.getQuantity();
                    revenue += (orderItemPojo.getQuantity())*(orderItemPojo.getSellingPrice());
                }
            }
            salesReportData.setQuantity(quantity);
            salesReportData.setRevenue(revenue);
            salesReportDataList.add(salesReportData);



        }




        return salesReportDataList;
    }

}
