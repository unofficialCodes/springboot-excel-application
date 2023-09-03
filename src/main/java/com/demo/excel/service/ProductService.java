package com.demo.excel.service;

import com.demo.excel.entity.Product;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    List<Product> findAllProducts();
    void init();
    void removeAllProducts();
    void importData(MultipartFile file);
}
