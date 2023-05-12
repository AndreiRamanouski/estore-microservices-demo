package com.appsdev.estore.product.controller;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
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
}
