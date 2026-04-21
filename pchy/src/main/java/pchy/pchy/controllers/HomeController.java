package pchy.pchy.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    
    @GetMapping
    public String home() {
        return "index";
    }

     @GetMapping("/temporal")
    public String temporal() {
        return "/temporal"; 
    }
}
