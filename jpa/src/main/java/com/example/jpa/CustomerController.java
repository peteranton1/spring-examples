package com.example.jpa;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CustomerController {

    private final CustomerRepository customerRepository;

    public CustomerController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @GetMapping("/customers.view")
    public String customers(Model model){
        model.addAttribute("customers",
                this.customerRepository.findAll());
        return "customers";
    }
}
