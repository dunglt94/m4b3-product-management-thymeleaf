package com.example.productmanagement.controller;

import com.example.productmanagement.model.Product;
import com.example.productmanagement.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private IProductService productService;

    @GetMapping
    public String getProductList(ModelMap model) {
        List<Product> products = productService.findAll();
        model.addAttribute("products", products);
        return "index";
    }

    @GetMapping("/create")
    public String showCreateForm(ModelMap model) {
        model.addAttribute("product", new Product());
        return "create";
    }

    @PostMapping("/save")
    public String createProduct(Product product) {
        productService.create(product);
        return "redirect:/products";
    }

    @GetMapping("/{id}/edit")
    public String showUpdateForm(@PathVariable int id, ModelMap model) {
        Product product = productService.findById(id);
        model.addAttribute("product", product);
        return "update";
    }

    @PostMapping("/update")
    public String updateProduct(Product product, RedirectAttributes redirectAttributes) {
        productService.update(product);
        redirectAttributes.addFlashAttribute("success", "Product updated successfully");
        return "redirect:/products";
    }

    @GetMapping("/{id}/delete")
    public String deleteProduct(@PathVariable int id, RedirectAttributes redirectAttributes) {
        productService.delete(id);
        redirectAttributes.addFlashAttribute("success", "Product deleted successfully");
        return "redirect:/products";
    }

    @GetMapping("/search")
    public String showSearchForm(@RequestParam String name ,ModelMap model) {
        List<Product> products = productService.findByName(name);
        model.addAttribute("products", products);
        if (products.isEmpty()) {
            model.addAttribute("message", "Product(s) not found");
        }
        model.addAttribute("searchQuery", name);

        return "search";
    }
}
