package com.demo.excel.service;

import com.demo.excel.entity.Product;
import com.demo.excel.repository.ProductRepository;
import com.demo.excel.util.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository repository;

    @Override
    public void init() {
        IntStream.range(0, 10)
                .forEach(value -> {
                    Product entityToSave = new Product
                            .Builder()
                            .name("Product " + (value+1))
                            .description("Product " + (value+1) + " description.")
                            .build();
                    repository.save(entityToSave);
                });
    }

    @Override
    public List<Product> findAllProducts() {
        return repository.findAll();
    }

    @Override
    public void removeAllProducts() {
        repository.deleteAll();;
    }

    @Override
    public void importData(MultipartFile file) {
        try {
            List<Product> products = Helper.importData(file.getInputStream());
            repository.saveAll(products);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
