package kr.ac.hansung.controller;

import jakarta.validation.Valid;
import kr.ac.hansung.dto.ProductDto;
import kr.ac.hansung.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public String list(@RequestParam(required = false) String keyword,
                       @PageableDefault(size = 5, sort = "id") Pageable pageable, Model model) {
        model.addAttribute("productPage", productService.searchProducts(keyword, pageable));
        model.addAttribute("keyword", keyword);
        return "products/list";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("productDto", productService.findById(id));
        model.addAttribute("productId", id);
        return "products/edit";
    }

    @PostMapping("/{id}/edit")
    public String edit(@PathVariable Long id, @Valid @ModelAttribute ProductDto dto,
                       BindingResult br, RedirectAttributes ra) {
        if (br.hasErrors()) return "products/edit";
        productService.updateProduct(id, dto);
        ra.addFlashAttribute("msg", "수정 완료");
        return "redirect:/products";
    }
}