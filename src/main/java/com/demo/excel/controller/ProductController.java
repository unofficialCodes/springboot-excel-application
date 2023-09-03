package com.demo.excel.controller;

import com.demo.excel.entity.Product;
import com.demo.excel.service.ProductService;
import com.demo.excel.util.Helper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController implements ProductApis {

    @Autowired
    private ProductService service;

    @Override
    @GetMapping(path = "/all", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.of(Optional.of(service.findAllProducts()));
    }

    @GetMapping(path = "/all/export")
    public void exportToExcel(HttpServletResponse servletResponse) {
        servletResponse
                .setContentType("application/octet-stream");

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=products.xlsx";
        servletResponse.setHeader(headerKey, headerValue);

        List<Product> products = service.findAllProducts();
        Helper helper = new Helper(products);

        helper.export(servletResponse);

    }

    @PostMapping("/upload")
    public ResponseEntity<String> importData(@RequestParam("file") MultipartFile file) {
        service.importData(file);
        return ResponseEntity.ok("Imported data successfully.");
    }

    @Override
    @DeleteMapping(path = "/removeAll")
    @ResponseStatus(HttpStatus.OK)
    public void removeAllProducts() {
        service.removeAllProducts();
    }

    @Override
    @GetMapping("/populate")
    public ResponseEntity<String> populate() {
        service.init();
        return ResponseEntity.ok("Populated data successfully.");
    }

}
