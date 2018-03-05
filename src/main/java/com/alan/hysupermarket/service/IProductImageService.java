package com.alan.hysupermarket.service;

import com.alan.hysupermarket.pojo.ProductImage;

import java.util.List;

public interface IProductImageService {

    String type_single = "type_single";
    String type_detail = "type_detail";

    void add(ProductImage productImage);

    void delete(int id);

    void update(ProductImage productImage);

    ProductImage get(int id);

    List<ProductImage> list(int pid, String type);

}
