package com.example.productmanagement.controller;

import com.example.productmanagement.model.Product;
import com.example.productmanagement.model.ProductForm;
import com.example.productmanagement.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private IProductService productService;

    @Value("${file-upload}")
    private String fileUpload;

    @GetMapping
    public String getProductList(ModelMap model) {
        List<Product> products = productService.findAll();
        model.addAttribute("products", products);
        return "index";
    }

    @GetMapping("/create")
    public String showCreateForm(ModelMap model) {
        model.addAttribute("productForm", new ProductForm());
        return "create";
    }

    @PostMapping("/save")
    public String createProduct(ProductForm productForm, RedirectAttributes redirectAttributes) {
        MultipartFile multipartFile = productForm.getImage();
        String fileName = multipartFile.getOriginalFilename();
        try {
            FileCopyUtils.copy(multipartFile.getBytes(), new File(fileUpload + fileName));
        } catch (IOException e) {
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }
        Product product = new Product(productForm.getId(), productForm.getName(), productForm.getPrice(),
                productForm.getDescription(), productForm.getManufacturer(), fileName);
        productService.create(product);
        return "redirect:/products";
    }

    @GetMapping("/{id}/view")
    public String showProductView(@PathVariable int id, ModelMap model) {
        Product product = productService.findById(id);
        model.addAttribute("product", product);
        return "view";
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
