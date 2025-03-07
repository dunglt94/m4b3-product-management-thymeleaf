package com.example.productmanagement.service;

import com.example.productmanagement.model.Product;

import java.util.List;

public interface IProductService {
    List<Product> findAll();

    Product findById(int id);

    List<Product> findByName(String name);

    void create(Product product);

    void update(Product product);

    void delete(int id);
}
