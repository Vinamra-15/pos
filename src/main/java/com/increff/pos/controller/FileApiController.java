package com.increff.pos.controller;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.increff.pos.service.ApiException;
import io.swagger.annotations.Api;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.sql.rowset.serial.SerialBlob;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Blob;
import java.sql.SQLException;

@Api
@RestController
public class FileApiController {
    @RequestMapping(path = "/download/invoice/{invoicePath}", method = RequestMethod.GET)
    public ResponseEntity<Resource> download(@PathVariable String invoicePath) throws ApiException {
        try {
            String billDirPath = System.getProperty("user.dir") + File.separator + "bills" + File.separator + invoicePath + ".pdf";
            File file = new File(billDirPath);
//            System.out.println(billDirPath);
            HttpHeaders header = new HttpHeaders();
            header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=invoice.pdf");
            header.add("Cache-Control", "no-cache, no-store, must-revalidate");
            header.add("Pragma", "no-cache");
            header.add("Expires", "0");

            Path path = Paths.get(file.getAbsolutePath());
            ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));
//            System.out.println(resource.contentLength());

            return ResponseEntity.ok()
                    .headers(header)
                    .contentLength(file.length())
                    .contentType(MediaType.parseMediaType("application/pdf"))
                    .body(resource);

        }
        catch (IOException exception){
            throw new ApiException("Pdf being generated!");
        }
    }
//@GetMapping(
//        produces = MediaType.APPLICATION_PDF_VALUE
//)
//@RequestMapping(path = "/download/invoice/{invoicePath}", method = RequestMethod.GET)
//public @ResponseBody byte[] download(@PathVariable String invoicePath) throws ApiException {
//    try {
//
//        String billDirPath = System.getProperty("user.dir") + File.separator + "bills" + File.separator + invoicePath + ".pdf";
//        File file = new File(billDirPath);
//        FileInputStream fl = new FileInputStream(file);
//        byte[] arr = new byte[(int)file.length()];
//        fl.read(arr);
//        fl.close();
//        return arr;
//
////
////        System.out.println("mai upar se print hota h");
////        File file = new File(billDirPath);
////        return file;
////        System.out.println("maine bhi print hona h");
////        System.out.println(billDirPath);
////        HttpHeaders header = new HttpHeaders();
////        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=invoice.pdf");
////        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
////        header.add("Pragma", "no-cache");
////        header.add("Expires", "0");
////
////        Path path = Paths.get(file.getAbsolutePath());
////        byte[] resource= Files.readAllBytes(path);
////        System.out.println("Maine prinr hona haldsfjsoiaj;mdpi");
//////        System.out.println(resource.contentLength());
////
////        try {
////            return new SerialBlob(resource);
////        } catch (SQLException e) {
////            throw new ApiException("difficult converting resource to blob");
////        }
////
//    }
//    catch (Exception exception){
//        System.out.println(exception);
//        throw new ApiException("Pdf being generated");
//    }
//}
}
