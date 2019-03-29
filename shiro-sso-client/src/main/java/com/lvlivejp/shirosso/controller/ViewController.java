package com.lvlivejp.shirosso.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller

public class ViewController {


    @GetMapping("/indexview")
    public String view(){
        return "index";
    }

}
