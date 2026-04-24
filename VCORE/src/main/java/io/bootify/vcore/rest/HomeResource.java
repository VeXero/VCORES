package io.bootify.vcore.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeResource {

    @GetMapping("/api/hello")
    public String hello() {
        return "Hello Dear Commissioner!";
    }
}