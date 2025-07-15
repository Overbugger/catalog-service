package com.polarbookshop.catalog_service.controller;

import com.polarbookshop.catalog_service.config.PolarProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    private final PolarProperties polarProperties;

    public HomeController(PolarProperties polarProperties) {
        this.polarProperties = polarProperties;
    }

    @GetMapping("/greeting")
    public String getGreeting() {
        return polarProperties.getGreeting();
    }
}
