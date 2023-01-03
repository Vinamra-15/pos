function getBaseUrl(){
    var baseUrl = $("meta[name=baseUrl]").attr("content")
    return baseUrl
}
function getOrderUrl(){
	return getBaseUrl() + "/api/orders/";
}
function getProductUrl(){
	return getBaseUrl() + "/api/products";
}


function getOrderList() {
  var url = getOrderUrl();
  $.ajax({
    url: url,
    type: 'GET',
    success: function (data) {
      displayOrderList(data);
    },
    error: handleAjaxError,
  });
}

function getProductByBarcode(barcode, onSuccess) {
  const url = getProductUrl() + "?barcode=" + barcode;
  $.ajax({
    url: url,
    type: 'GET',
    success: function (data) {
      onSuccess(data);
    },
    error: handleAjaxError,
  });
}

//UI DISPLAY METHODS
let orderItems = [];

function getCurrentOrderItem(typeOfOperation) {
   if(typeOfOperation==='add')
  {
    return {
        barcode: $('#inputAddModalBarcode').val(),
        quantity: Number.parseInt($('#inputAddModalQuantity').val()),
        sellingPrice:Number.parseFloat($('#inputAddModalSellingPrice').val())
    };
  }
  else
  {
    return {
        barcode: $('#inputEditModalBarcode').val(),
        quantity: Number.parseInt($('#inputEditModalQuantity').val()),
        sellingPrice:Number.parseFloat($('#inputEditModalSellingPrice').val())
      };
  }
}

function addItem(item) {
  if(item.barcode==null){
    alert("Please enter barcode!")
    return
  }
  const index = orderItems.findIndex((it) => it.barcode === item.barcode);
  if (index == -1) {
    orderItems.push(item);
  } else {
    orderItems[index].quantity += item.quantity;
  }
}

function addOrderItem(typeOfOperation) {
  const item = getCurrentOrderItem(typeOfOperation);
//  console.log(item)
  if(typeOfOperation==='add')
  {
    getProductByBarcode(item.barcode, (product) => {
        addItem({
          barcode: product.barcode,
          name: product.name,
          sellingPrice: item.sellingPrice,
          quantity: item.quantity,
        })
         displayCreateOrderItems(orderItems);
         resetAddItemForm();
        }

        )
    }
    else
    {
        getProductByBarcode(item.barcode, (product) => {
            addItem({
              barcode: product.barcode,
              name: product.name,
              sellingPrice: item.sellingPrice,
              quantity: item.quantity,
            })
            displayEditOrderItems(orderItems)
            resetEditItemForm();
            });
    }

}

function onQuantityChanged(barcode) {
  const index = orderItems.findIndex((it) => it.barcode === barcode);
  if (index == -1) return;

  const newQuantity = $(`#order-item-${barcode}`).val();
  orderItems[index].quantity = Number.parseInt(newQuantity);
}

function displayCreateOrderItems(data) {
  const $tbody = $('#create-order-table').find('tbody');
  $tbody.empty();


  console.log("from displayCreateOrderItems." )
  console.log(data)



  for (let i in data) {
    const item = data[i];
    const row = `
      <tr class="text-center">
        <td class="barcodeData">${item.barcode}</td>
        <td>${item.name}</td>
        <td >${item.sellingPrice}</td>
        <td>
          <input
            id="order-item-${item.barcode}"
            type="number"
            class="form-control"
            value="${item.quantity}"
            onchange="onQuantityChanged('${item.barcode}')"
            style="width:70%" min="1">
        </td>
        <td>
          <button onclick="deleteOrderItem('${item.barcode}','add')" class="btn btn-outline-danger">Delete</button>
        </td>
      </tr>
    `;

    $tbody.append(row);
  }
}

function deleteOrderItem(barcode,typeOfOperation) {
  const index = orderItems.findIndex((it) => it.barcode === barcode);
  if (index == -1) return;
  orderItems.splice(index, 1);
  if(typeOfOperation==='edit')
  {
    displayEditOrderItems(orderItems);
    displayEditOrderModal();
  }
  else
  displayCreateOrderItems(orderItems)
}

function resetAddItemForm() {
  $('#inputAddModalBarcode').val('');
  $('#inputAddModalQuantity').val('');
  $('#inputAddModalSellingPrice').val('');
}

function resetEditItemForm() {
  $('#inputEditModalBarcode').val('');
  $('#inputEditModalQuantity').val('');
  $('#inputEditModalSellingPrice').val('');
}

function resetCreateModal() {
  resetAddItemForm();
  orderItems = [];
  displayCreateOrderItems(orderItems);
}

function getBillAmount(id){
    const url = getOrderUrl + id;
    $.ajax({
        url: url,
        type: 'GET',
        success: function (data) {
          // sum data items
          let amount = 0;
          for(let i in data.items){
            amount = amount + i.sellingPrice
          }
          return amount
        },
        error: handleAjaxError,
      });

}


function displayOrderList(orders) {
  var $tbody = $('#order-table').find('tbody');
  $tbody.empty();

  orders.forEach((order) => {
    //billAmount = getBillAmount(order.id)
    var row = `
        <tr class="text-center">
            <td>${order.id}</td>
            <td>${order.datetime}</td>
            <td>
                <button type="button" class="btn btn-outline-secondary" onclick="fetchOrderDetails(${order.id},'add')">
                  Details
                </button>

                <button type="button" class="btn btn-outline-secondary" onclick="editOrderDetails(${order.id})">
                                  Edit
                                </button>
            </td>
        </tr>
    `;
    $tbody.append(row);
  });
}


function fetchOrderDetails(id,typeOfOperation) {
  var url = getOrderUrl() + id;
  $.ajax({
    url: url,
    type: 'GET',
    success: function (data) {
        orderItems = data.items;
       if(typeOfOperation==='add')
       displayOrderDetails(data);
       else
       {
            displayEditOrderItems(orderItems);
            displayEditOrderModal()
       }
    },
    error: handleAjaxError,
  });
}
function displayEditOrderModal(){
    $('#edit-order-modal').modal({ backdrop: 'static', keyboard: false }, 'show');
}
function displayEditOrderItems(data){
      //displayEditOrderModal();
      const $tbody = $('#edit-order-table').find('tbody');
      $tbody.empty();
      const items = data;
      console.log(items)

        for (let i in items) {
          const item = items[i];
          console.log(item)

          const row = `
            <tr class="text-center">
              <td class="barcodeData">${item.barcode}</td>
              <td>${item.name}</td>
              <td >${item.sellingPrice}</td>
              <td>
                <input
                    id="order-item-${item.barcode}"
                    type="number"
                    class="form-control"
                    value="${item.quantity}"
                    onchange="onQuantityChanged('${item.barcode}')"
                    style="width:70%" min="1">
              </td>
              <td>
                   <button onclick="deleteOrderItem('${item.barcode}','edit')" class="btn btn-outline-danger">Delete</button>
              </td>

            </tr>
          `;

          $tbody.append(row);
        }
}

function resetUploadDialog() {
  //Reset file name
  var $file = $('#orderFile');
  $file.val('');
  $('#orderFileName').html('Choose File');
  //Reset various counts
  processCount = 0;
  fileData = [];
  errorData = [];
  //Update counts
  updateUploadDialog();
}

function updateUploadDialog() {
  $('#rowCount').html('' + fileData.length);
  $('#processCount').html('' + processCount);
  $('#errorCount').html('' + errorData.length);
}

function updateFileName() {
  var $file = $('#orderFile');
  var fileName = $file.val();
  $('#orderFileName').html(fileName);
}

function displayUploadData() {
  resetUploadDialog();
  $('#upload-order-modal').modal('toggle');
}

function displayOrderDetails(data) {
  const datetime = data.datetime
  displayOrderDetailsModal();
  const $time = $('#date-time')
  $time.text("Order placed on: " + datetime)
  const $orderId = $('#order-id')
  $orderId.text("Order Id: " + data.orderId)
  const $tbody = $('#order-details-table').find('tbody');
  $tbody.empty();
  const items = data.items;

    for (let i in items) {
      const item = items[i];
      const row = `
        <tr class="text-center">
          <td>${item.barcode}</td>
          <td>${item.name}</td>
          <td >${item.sellingPrice}</td>
          <td>${item.quantity}</td>
        </tr>
      `;

      $tbody.append(row);
    }
}

function displayOrderDetailsModal(){
    $('#order-details-modal').modal({ backdrop: 'static', keyboard: false }, 'show');
}

function hideOrderDetailsModal() {
  $('#order-details-modal').modal('toggle');
  getOrderList();
}


function displayCreationModal() {
  resetCreateModal();
  $('#create-order-modal').modal({ backdrop: 'static', keyboard: false }, 'show');
}

function hideCreationModal() {
  $('#create-order-modal').modal('toggle');
  getOrderList();
}

function editOrderDetails(id){
    fetchOrderDetails(id,'edit');
}

//INITIALIZATION CODE
function init() {
  $('#add-order-item').click(addOrderItem);
  $('#create-order').click(displayCreationModal);
  $('#refresh-data').click(getOrderList);
  $('#upload-data').click(displayUploadData);
  $('#place-order-btn').click(placeNewOrder);

}

$(document).ready(init);
$(document).ready(getOrderList);

// Place Order
function placeNewOrder() {
  const data = orderItems.map((it) => {
    return {
      barcode: it.barcode,
      quantity: it.quantity,
      sellingPrice:it.sellingPrice
    };
  });

  const json = JSON.stringify(data);
  placeOrder(json, hideCreationModal);
}

//BUTTON ACTIONS
function placeOrder(json, onSuccess) {
  //Set the values to update
  const url = getOrderUrl();

  $.ajax({
    url: url,
    type: 'POST',
    data: json,
    headers: {
      'Content-Type': 'application/json',
    },
    success: onSuccess,
    error: handleAjaxError,
  });

  return false;
}

