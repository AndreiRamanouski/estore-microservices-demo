package com.appsdev.estore.product.controller;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("products")
public class ProductController {


    @GetMapping
    public List<String> getProducts(){
        log.info("getProducts");

        return List.of("hi", "hi2");
    }

    @PostMapping
    public String createProduct(){
        log.info("createProduct");


        return "Create product";
    }

    @PutMapping
    public String updateProduct(){
        log.info("updateProduct");


        return "Update product";
    }

    @DeleteMapping
    public String deleteProduct(){
        log.info("deleteProduct");


        return "Delete product";
    }
}
