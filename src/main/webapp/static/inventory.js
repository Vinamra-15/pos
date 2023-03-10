function getInventoryUrl(){
   var baseUrl = $("meta[name=baseUrl]").attr("content")
   return baseUrl + "/api/inventory";
}

function getRole(){
    var role = $("meta[name=role]").attr("content")
    return role;
}


function updateInventory(event){
   $('#edit-inventory-modal').modal('toggle');
   //Get the ID
   var barcode = $("#inventory-edit-form input[name=barcode]").val();
   var url = getInventoryUrl() +"/" + barcode;
   //Set the values to update
   var $form = $("#inventory-edit-form");
   var json = toJson($form);
   $.ajax({
      url: url,
      type: 'PUT',
      data: json,
      headers: {
           'Content-Type': 'application/json'
       },
      success: function(response) {
             getInventoryList();
             $.notify("Inventory update successful for product: " + JSON.parse(json).barcode,"success");
      },
      error: handleAjaxError
   });

   return false;
}


function getInventoryList(){
   var url = getInventoryUrl();
   $.ajax({
      url: url,
      type: 'GET',
      success: function(data) {
             displayInventoryList(data);
             //console.log(data);
      },
      error: handleAjaxError
   });
}


// FILE UPLOAD METHODS
var fileData = [];
var errorData = [];
var processCount = 0;


function processData(){
   var file = $('#employeeFile')[0].files[0];
   readFileData(file, readFileDataCallback);
}

function readFileDataCallback(results){
   fileData = results.data;
   uploadRows();
}

function uploadRows(){
   //Update progress
   updateUploadDialog();
   //If everything processed then return
   if(processCount==fileData.length){
      return;
   }

   //Process next row
   var row = fileData[processCount];
   processCount++;

   var json = JSON.stringify(row);
   var url = getInventoryUrl();

   //Make ajax call
   $.ajax({
      url: url,
      type: 'POST',
      data: json,
      headers: {
           'Content-Type': 'application/json'
       },
      success: function(response) {
             uploadRows();
      },
      error: function(response){
             row.error=response.responseText
             errorData.push(row);
             uploadRows();
      }
   });

}

function downloadErrors(){
   writeFileData(errorData);
}

//UI DISPLAY METHODS

function displayInventoryList(data){
   var $tbody = $('#inventory-table').find('tbody');
   $tbody.empty();
   for(var i in data){
      var e = data[i];
      if(getRole()==="supervisor")
      {
          var buttonHtml = ' <button type="button" class="btn btn-outline-secondary" onclick="displayEditInventory(\'' + e.barcode + '\')">Edit</button>'
          var row = '<tr class="text-center">'
          + '<td>' + e.barcode + '</td>'
          + '<td>' + e.productName + '</td>'
          + '<td>'  + e.quantity + '</td>'
          + '<td>' + buttonHtml + '</td>'
          + '</tr>';
            $tbody.append(row);
      }
      else
      {
        var row = '<tr class="text-center">'
                  + '<td>' + e.barcode + '</td>'
                  + '<td>' + e.productName + '</td>'
                  + '<td>'  + e.quantity + '</td>'
                  + '</tr>';
                    $tbody.append(row);
      }

   }
}

function displayEditInventory(barcode){
   var url = getInventoryUrl() + "/" + barcode;
   $.ajax({
      url: url,
      type: 'GET',
      success: function(data) {
             displayInventory(data);
      },
      error: handleAjaxError
   });
}

function resetUploadDialog(){
   //Reset file name
   var $file = $('#employeeFile');
   $file.val('');
   $('#employeeFileName').html("Choose File");
   //Reset various counts
   processCount = 0;
   fileData = [];
   errorData = [];
   //Update counts
   updateUploadDialog();
}

function updateUploadDialog(){
   $('#rowCount').html("" + fileData.length);
   $('#processCount').html("" + processCount);
   $('#errorCount').html("" + errorData.length);
}

function updateFileName(){
   var $file = $('#employeeFile');
   var fileName = $file.val();
   $('#employeeFileName').html(fileName);
}

function displayUploadData(){
   resetUploadDialog();
   $('#upload-employee-modal').modal('toggle');
}

function displayInventory(data){
   $("#inventory-edit-form input[name=barcode]").val(data.barcode);
   $("#inventory-edit-form input[name=quantity]").val(data.quantity);
   $('#edit-inventory-modal').modal('toggle');
}


//INITIALIZATION CODE
function init(){
   $('#update-inventory').click(updateInventory);
   $('#refresh-data').click(getInventoryList);
   $('#upload-data').click(displayUploadData);
   $('#process-data').click(processData);
   $('#download-errors').click(downloadErrors);
    $('#employeeFile').on('change', updateFileName)
}

$(document).ready(init);
$(document).ready(getInventoryList);