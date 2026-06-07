package kr.ac.hansung.controller;

import jakarta.validation.Valid;
import kr.ac.hansung.dto.ProductDto;
import kr.ac.hansung.entity.Product;
import kr.ac.hansung.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public String list(@RequestParam(required = false) String keyword,
                       @PageableDefault(size = 10, sort = "id") Pageable pageable, Model model) {
        Page<Product> productPage = productService.searchProducts(keyword, pageable);
        model.addAttribute("products", productPage); // Page 객체 전체 전달
        model.addAttribute("keyword", keyword);
        return "products/list";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("productDto", new ProductDto());
        return "products/add";
    }

    @PostMapping("/add")
    public String add(@Valid @ModelAttribute("productDto") ProductDto dto,
                      BindingResult br) {
        if (br.hasErrors()) {
            return "products/add";
        }
        productService.saveProduct(dto);
        return "redirect:/products";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("productDto", productService.findById(id));
        model.addAttribute("productId", id);
        return "products/edit";
    }

    @PostMapping("/{id}/edit")
    public String edit(@PathVariable Long id,
                       @Valid @ModelAttribute("productDto") ProductDto dto,
                       BindingResult br,
                       Model model) {
        if (br.hasErrors()) {
            model.addAttribute("productId", id);
            return "products/edit";
        }
        productService.updateProduct(id, dto);
        return "redirect:/products";
    }
}