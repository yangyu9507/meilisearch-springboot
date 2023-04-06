package com.yaron.meilisearch.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Yaron
 * @version 1.0
 * @date 2023/03/31
 * @description
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping
    public String index(){
        return "Hello Meilisearch~";
    }
}
