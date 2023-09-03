package com.demo.excel.controller;

import com.demo.excel.entity.Product;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductApis {
    ResponseEntity<List<Product>> getAllProducts();

    void removeAllProducts();

    ResponseEntity<String> populate();

    ResponseEntity<String> importData(@RequestParam("file") MultipartFile file);
}
