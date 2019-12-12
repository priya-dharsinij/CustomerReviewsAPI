package com.udacity.course3.reviews.controller;

import com.udacity.course3.reviews.exception.ResourceNotFoundException;
import com.udacity.course3.reviews.model.Product;
import com.udacity.course3.reviews.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Spring REST controller for working with product entity.
 */
@RestController
@RequestMapping("/products")
public class ProductsController {

    // TODO: Wire JPA repositories here
    @Autowired
    private ProductRepository productRepository;

    /**
     * Creates a product.
     *
     * 1. Accept product as argument. Use {@link RequestBody} annotation.
     * 2. Save product.
     */
    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public Product createProduct(@Valid @RequestBody Product product) {
        System.out.println("Enetere create product"+product);
       return productRepository.save(product);
    }

    /**
     * Finds a product by id.
     *
     * @param id The id of the product.
     * @return The product if found, or a 404 not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Product> findById(@PathVariable("id") Long id) {
        Optional<Product> optional = productRepository.findById(id);
        Product product = optional.orElseThrow(() -> new ResourceNotFoundException("ProductID " + id + " not found"));
        return ResponseEntity.ok(product);
    }

    /**
     * Lists all products.
     *
     * @return The list of products.
     */
    @GetMapping("/")
    public List<Product> listProducts() {
        Iterable<Product> all = productRepository.findAll();
        return StreamSupport.stream(all.spliterator(), false).collect(Collectors.toList());
    }
}