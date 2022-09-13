package com.onulstore.service;

import com.onulstore.domain.product.Product;
import com.onulstore.domain.product.ProductRepository;
import com.onulstore.web.dto.ProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    public ProductDto.ProductResponse register(ProductDto.ProductRequest registration) {
        if(productRepository.existsByProductName(registration.getProductName())) {
            throw new RuntimeException("이미 존재하는 상품입니다.");
        }
        Product product = registration.toProduct();
        return ProductDto.ProductResponse.of(productRepository.save(product));
    }

    @Transactional
    public ProductDto.ProductResponse modify(ProductDto.ProductRequest modification, Long productId) {

        Product product = productRepository.findById(productId).get();
        product.changeProductData(modification.getProductName(),
                modification.getContent(),
                modification.getLargeCategoryCode(),
                modification.getSmallCategoryCode(),
                modification.getPrice(),
                modification.getQuantity(),
                modification.getPurchaseCount(),
                modification.getProductImg(),
                modification.getProductStatus());

        return ProductDto.ProductResponse.of(productRepository.save(product));
    }

    @Transactional
    public boolean delete(Long productId) {
        Optional<Product> deleteProductNumber = productRepository.findById(productId);
        if(deleteProductNumber.isPresent()){
            productRepository.deleteById(productId);
            return true;
        }
        else{
            return false;
        }
    }

    @Transactional
    public ProductDto.ProductResponse detailInquiry(Long productId){
        Product product = productRepository.findById(productId).get();
        return ProductDto.ProductResponse.of(product);
    }

    @Transactional
    public Page entireProductList(Pageable pageable){
        Page<ProductDto.ProductResponse> pages = productRepository.findAll(pageable).map(product -> ProductDto.ProductResponse.of(product));
        return pages;
    }
}
