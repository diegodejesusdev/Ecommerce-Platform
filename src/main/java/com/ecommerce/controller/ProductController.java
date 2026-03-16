package com.ecommerce.controller;

import com.ecommerce.entity.Category;
import com.ecommerce.entity.Product;
import com.ecommerce.repository.CategoryRepository;
import com.ecommerce.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/products")
public class ProductController {

    private static final int DEFAULT_PAGE_SIZE = 12;

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductController(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping
    public String list(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            Model model
    ) {
        Pageable pageable = PageRequest.of(page, DEFAULT_PAGE_SIZE, Sort.by("name"));
        Page<Product> productPage;

        if (search != null && !search.isBlank()) {
            productPage = productRepository.findByNameContainingIgnoreCase(search.trim(), pageable);
        } else if (categoryId != null) {
            productPage = productRepository.findByCategoryId(categoryId, pageable);
        } else {
            productPage = productRepository.findAll(pageable);
        }

        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("productPage", productPage);
        model.addAttribute("categories", categories);
        model.addAttribute("selectedCategoryId", categoryId);
        model.addAttribute("search", search != null ? search : "");
        return "products/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isEmpty()) {
            return "redirect:/products";
        }
        model.addAttribute("product", product.get());
        return "products/detail";
    }
}
