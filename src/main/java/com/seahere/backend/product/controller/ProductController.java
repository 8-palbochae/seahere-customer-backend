package com.seahere.backend.product.controller;

import com.seahere.backend.auth.login.CustomUserDetails;
import com.seahere.backend.product.controller.response.IncomingSearchResponse;
import com.seahere.backend.product.dto.ProductDto;
import com.seahere.backend.product.entity.ProductEntity;
import com.seahere.backend.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/product-search-incoming")
    public ResponseEntity<List<IncomingSearchResponse>> getProducts() {
        List<ProductEntity> products = productService.getAllProducts();
        List<IncomingSearchResponse> productResponses = products.stream().map(ProductDto::from).map(IncomingSearchResponse::from).collect(Collectors.toList());
        return ResponseEntity.ok(productResponses);
    }

    @GetMapping("/product-qr/{productId}")public ResponseEntity<ProductDto> getProductQr(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable Long productId) {
        Optional<ProductEntity> productOptional = productService.getProduct(productId);
        if (productOptional.isPresent()) {
            ProductDto response= ProductDto.from(productOptional.get());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

}