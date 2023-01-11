package com.increff.pos.dto;

import com.increff.pos.model.InventoryReportData;
import com.increff.pos.model.SalesReportData;
import com.increff.pos.model.SalesReportForm;
import com.increff.pos.pojo.*;
import com.increff.pos.service.*;
import com.increff.pos.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.increff.pos.util.Validate.validate;

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

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private SalesReportService salesReportService;


    public List<SalesReportData> getSalesReport(SalesReportForm salesReportForm) throws ApiException {
        validate(salesReportForm);
        Date startDate = salesReportForm.getStartDate();
        Date endDate = salesReportForm.getEndDate();
        String brand = salesReportForm.getBrand();
        String category = salesReportForm.getCategory();

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

    public List<InventoryReportData> getInventoryReport() throws ApiException {

        Map<Integer,InventoryReportData> brandIdToInventoryReportDataMap = new HashMap<Integer,InventoryReportData>();
        List<ProductPojo> productPojos = productService.getAll();
        for(ProductPojo productPojo:productPojos){
            InventoryPojo inventoryPojo = inventoryService.get(productPojo.getId());
            Integer brandId = productPojo.getBrandId();
            BrandCategoryPojo brandCategoryPojo = brandCategoryService.get(brandId);
            if(brandIdToInventoryReportDataMap.containsKey(brandId)==false){
                InventoryReportData inventoryReportData = new InventoryReportData();
                inventoryReportData.setBrand(brandCategoryPojo.getBrand());
                inventoryReportData.setCategory(brandCategoryPojo.getCategory());
                inventoryReportData.setQuantity(inventoryPojo.getQuantity());
                brandIdToInventoryReportDataMap.put(brandId,inventoryReportData);
            }
            else
            {
                InventoryReportData inventoryReportData = brandIdToInventoryReportDataMap.get(brandId);
                Integer quant = inventoryReportData.getQuantity();
                quant+=inventoryPojo.getQuantity();
                inventoryReportData.setQuantity(quant);
                brandIdToInventoryReportDataMap.put(brandId,inventoryReportData);

            }
        }

        List<InventoryReportData> inventoryReportDataList = new ArrayList<InventoryReportData>();
        for(Map.Entry m:brandIdToInventoryReportDataMap.entrySet()){
            inventoryReportDataList.add((InventoryReportData) m.getValue());
        }
        return inventoryReportDataList;
    }

    @Scheduled(cron = "5 * * ? * *")
    public void generateDailySalesReport() throws ApiException {
        DaySalesPojo daySalesPojo = new DaySalesPojo();
        daySalesPojo.setDate(new Date());

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,-1);
        Date yesterday = TimeUtil.getStartOfDay(calendar.getTime(),calendar);
        System.out.println("From: " + yesterday.toString() + "\nTo: " + new Date().toString());

        List<OrderPojo> orderPojoList = orderService.getByStartDateEndDate(yesterday, new Date());
        List<OrderItemPojo> orderItemPojoList = new ArrayList<OrderItemPojo>();
        for(OrderPojo orderPojo : orderPojoList) {
            List<OrderItemPojo> orderItemPojo = orderItemService.getByOrderId(orderPojo.getId());
            orderItemPojoList.addAll(orderItemPojo);
        }

        Double totalRevenue = 0d;
        for (OrderItemPojo orderItem : orderItemPojoList) {
            totalRevenue += orderItem.getSellingPrice() * orderItem.getQuantity();
        }

        daySalesPojo.setTotal_revenue(totalRevenue);
        daySalesPojo.setInvoiced_orders_count(orderPojoList.size());
        daySalesPojo.setInvoiced_items_count(orderItemPojoList.size());

        salesReportService.add(daySalesPojo);
    }

}
