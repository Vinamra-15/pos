package com.increff.pos.dto;

import com.increff.pos.model.OrderData;
import com.increff.pos.model.OrderDetailsData;
import com.increff.pos.model.OrderItemData;
import com.increff.pos.model.OrderItemForm;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import sun.misc.BASE64Decoder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.*;

import static com.increff.pos.util.ConvertUtil.convert;
import static com.increff.pos.util.Normalize.normalizeForm;
import static com.increff.pos.util.Validate.validateForm;

@Component
@Transactional(rollbackFor = {ClassCastException.class, ApiException.class, Throwable.class})
public class OrderDto {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private ProductService productService;


    @Autowired
    private InventoryService inventoryService;
    public void createOrder(List<OrderItemForm> orderItemForms) throws ApiException, IOException {
        normalizeForm(orderItemForms);
        validateForm(orderItemForms);
        /*
            1. productPojo(fetch product pojo from each orderItemForm's barcode)
            2.For each productPojo since barcode existence is already checked, compare and reduce inventory quantity
                i. reduceInventoryQuantity(orderItemPojo,productPojo)
            2. create new orderPojo
            3. Save orderItemPojo from convertUtil using(orderItemForm,productPojo,orderPojo) for each orderItemForm
            4. get time and set in orderPojo
            5. save orderPojo in order table
         */
        List<ProductPojo> productPojoList = getProductsFromOrderItemFormList(orderItemForms);
        List<InventoryPojo> inventoryPojoList = getInventoryFromProducts(productPojoList);
        validateInventoryQuantity(orderItemForms,productPojoList,inventoryPojoList);
        reduceInventoryQuantity(orderItemForms,inventoryPojoList);

        OrderPojo orderPojo = new OrderPojo();
        orderPojo.setDatetime(Date.from(Instant.now()));
        orderService.add(orderPojo);

        for(OrderItemForm orderItemForm:orderItemForms){
            ProductPojo productPojo = productService.getByBarcode(orderItemForm.getBarcode());
            OrderItemPojo orderItemPojo = convert(orderItemForm, productPojo, orderPojo);
            orderItemService.add(orderItemPojo);
        }
        generatePDF(getOrderDetails(orderPojo.getId()));
    }
    private void generatePDF(OrderDetailsData orderDetailsData) throws IOException {
        String encodedPdf = getEncodedPdf(orderDetailsData);
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] decodedBytes = decoder.decodeBuffer(encodedPdf);

        File file = new File("bills"+"/"+orderDetailsData.getId().toString() +".pdf");
        FileOutputStream fop = new FileOutputStream(file);
        fop.write(decodedBytes);
        fop.flush();
        fop.close();
    }

    private String getEncodedPdf(OrderDetailsData orderDetailsData){
        RestTemplate restTemplate = new RestTemplate();
        String invoiceAppUrl = "http://localhost:8000/invoice";
        String response = restTemplate.postForObject(invoiceAppUrl + "/api/generate",orderDetailsData, String.class);
        return response;
    }


    private void reduceInventoryQuantity(List<OrderItemForm> orderItemForms, List<InventoryPojo> inventoryPojoList) throws ApiException {
        for(int i=0;i<orderItemForms.size();i++){
            InventoryPojo inventoryPojo = inventoryPojoList.get(i);
            inventoryService.update(inventoryPojo.getProductId(),inventoryPojo);
        }
    }

    private void validateInventoryQuantity(List<OrderItemForm> orderItemForms, List<ProductPojo> productPojoList, List<InventoryPojo> inventoryPojoList) throws ApiException {
        for(int i=0;i<orderItemForms.size();i++){
            InventoryPojo inventoryPojo = inventoryPojoList.get(i);
            OrderItemForm orderItemForm = orderItemForms.get(i);
            if(orderItemForm.getQuantity()>inventoryPojo.getQuantity()){
                throw new ApiException("Insufficient inventory for Product: "+productPojoList.get(i).getBarcode());
            }

            Integer newQuantity = inventoryPojo.getQuantity() - orderItemForm.getQuantity();
            inventoryPojo.setQuantity(newQuantity);
            inventoryPojoList.set(i,inventoryPojo);
        }
    }

    private List<InventoryPojo> getInventoryFromProducts(List<ProductPojo> productPojoList) throws ApiException {
        List<InventoryPojo> inventoryPojoList = new ArrayList<InventoryPojo>();
        for(ProductPojo productPojo:productPojoList){
            InventoryPojo inventoryPojo = inventoryService.get(productPojo.getId());
            inventoryPojoList.add(inventoryPojo);
        }
        return inventoryPojoList;
    }

    public OrderDetailsData getOrderDetails(Integer id) throws ApiException {
        OrderDetailsData orderDetailsData = new OrderDetailsData();
        OrderPojo orderPojo = orderService.get(id);
        List<OrderItemPojo> orderItems = orderItemService.getByOrderId(id);
        List<ProductPojo> productPojos = getProductsFromOrderItemPojoList(orderItems);
        List<OrderItemData> orderItemDataList = getOrderItemDetailsList(orderItems,productPojos);

        orderDetailsData.setId(orderPojo.getId());
        orderDetailsData.setDatetime(orderPojo.getDatetime());
        orderDetailsData.setItems(orderItemDataList);
        return orderDetailsData;
    }

    private List<OrderItemData> getOrderItemDetailsList(List<OrderItemPojo> orderItems, List<ProductPojo> productPojos) {
        List<OrderItemData> orderItemDataList = new ArrayList<OrderItemData>();
        for(int i=0;i<orderItems.size();i++){
            ProductPojo productPojo = productPojos.get(i);
            OrderItemPojo orderItemPojo = orderItems.get(i);
            OrderItemData orderItemData = convert(orderItemPojo,productPojo);
            orderItemDataList.add(orderItemData);
        }
        return orderItemDataList;
    }

    private List<ProductPojo> getProductsFromOrderItemPojoList(List<OrderItemPojo> orderItems) throws ApiException {
        List<ProductPojo> productPojos = new ArrayList<ProductPojo>();
        for(OrderItemPojo orderItemPojo:orderItems){
            ProductPojo productPojo = productService.get(orderItemPojo.getProductId());
            productPojos.add(productPojo);
        }
        return productPojos;
    }

    private List<ProductPojo> getProductsFromOrderItemFormList(List<OrderItemForm> orderItemFormList) throws ApiException {
        List<ProductPojo> productPojos = new ArrayList<ProductPojo>();
        for(OrderItemForm orderItemForm:orderItemFormList){
            ProductPojo productPojo = productService.getByBarcode(orderItemForm.getBarcode());
            productPojos.add(productPojo);
        }
        return productPojos;
    }


    public List<OrderData> getAll() {
        List<OrderPojo> list = orderService.getAll();
        List<OrderData> list2 = new ArrayList<OrderData>();
        for(OrderPojo orderPojo:list){
            list2.add(convert(orderPojo));
        }
        Collections.reverse(list2);
        return list2;
    }

    private boolean contains(List<OrderItemPojo> prevOrderItemPojos,OrderItemForm orderItemForm) throws ApiException {
        for(OrderItemPojo prevPojo:prevOrderItemPojos){
            ProductPojo productPojo = productService.get(prevPojo.getProductId());
            if(Objects.equals(orderItemForm.getBarcode(), productPojo.getBarcode()))
                return true;
        }
        return false;
    }
    public void update(Integer id, List<OrderItemForm> curOrderItemForms) throws ApiException, IOException {
        normalizeForm(curOrderItemForms);
        validateForm(curOrderItemForms);
        List<OrderItemPojo> prevOrderItemPojos = orderItemService.getByOrderId(id);

//        HashSet<OrderItemPojo> prevOrderItemPojoSet = new HashSet<OrderItemPojo>();
//        for(OrderItemPojo orderItemPojo:prevOrderItemPojos){
//            prevOrderItemPojoSet.add(orderItemPojo);
//        }



        for(OrderItemForm orderItemForm:curOrderItemForms){
            if(contains(prevOrderItemPojos,orderItemForm)==false){
                validateAndReduceInventoryQuantity(orderItemForm);
                ProductPojo productPojo = productService.getByBarcode(orderItemForm.getBarcode());
                OrderPojo orderPojo = orderService.get(id);
                orderItemService.add(convert(orderItemForm,productPojo,orderPojo));

            }
            else
            {
                ProductPojo productPojo = productService.getByBarcode(orderItemForm.getBarcode());
                OrderItemPojo prevOrderItemPojo = orderItemService.getByOrderIdProductId(id,productPojo.getId());
                InventoryPojo inventoryPojo = inventoryService.get(productPojo.getId());

                Integer newQuantity = inventoryPojo.getQuantity() + prevOrderItemPojo.getQuantity() - orderItemForm.getQuantity();
                if(newQuantity<0){
                    throw new ApiException("Insufficient Inventory for product with barcode: " + productPojo.getBarcode());
                }
                prevOrderItemPojo.setQuantity(orderItemForm.getQuantity());
                prevOrderItemPojo.setSellingPrice(orderItemForm.getSellingPrice());
                inventoryPojo.setQuantity(newQuantity);
                inventoryService.update(productPojo.getId(),inventoryPojo);
                orderItemService.update(prevOrderItemPojo.getId(),prevOrderItemPojo);
                prevOrderItemPojos.remove(prevOrderItemPojo);
            }
        }
        for(OrderItemPojo orderItemPojo:prevOrderItemPojos){
            InventoryPojo inventoryPojo = inventoryService.get(orderItemPojo.getProductId());
            Integer newQuantity = inventoryPojo.getQuantity() +  orderItemPojo.getQuantity();
            inventoryPojo.setQuantity(newQuantity);
            inventoryService.update(orderItemPojo.getProductId(),inventoryPojo);
            orderItemService.delete(orderItemPojo.getId());
        }
        OrderPojo orderPojo = orderService.get(id);
        generatePDF(getOrderDetails(orderPojo.getId()));

    }

    private void validateAndReduceInventoryQuantity(OrderItemForm orderItemForm) throws ApiException {
        ProductPojo productPojo = productService.getByBarcode(orderItemForm.getBarcode());
        InventoryPojo inventoryPojo = inventoryService.get(productPojo.getId());

        if(orderItemForm.getQuantity()>inventoryPojo.getQuantity())
        {
            throw new ApiException("Insufficient Inventory for product with barcode: " + productPojo.getBarcode());
        }
        Integer newQuantity = inventoryPojo.getQuantity() - orderItemForm.getQuantity();
        inventoryPojo.setQuantity(newQuantity);
        inventoryService.update(productPojo.getId(),inventoryPojo);
    }

    private List<OrderItemPojo> convertOrderItemFormsToOrderItemPojo(List<OrderItemForm> orderItemForms,Integer orderId) throws ApiException {
        List<OrderItemPojo> list = new ArrayList<OrderItemPojo>();
        OrderPojo orderPojo = orderService.get(orderId);
        for(OrderItemForm orderItemForm:orderItemForms){
            ProductPojo productPojo = productService.getByBarcode(orderItemForm.getBarcode());
            OrderItemPojo orderItemPojo = convert(orderItemForm,productPojo,orderPojo);
            list.add(orderItemPojo);
        }
        return list;
    }

}
