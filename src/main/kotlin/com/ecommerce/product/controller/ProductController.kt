package com.ecommerce.product.controller

import com.ecommerce.common.dto.ProductDTO
import com.ecommerce.product.service.ProductService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/products")
class ProductController(private val productService: ProductService) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @GetMapping
    fun getAllProducts(@RequestParam(required = false) category: String?,
                      @RequestParam(required = false) search: String?): List<ProductDTO> {
        logger.info("GET /products - category={}, search={}", category, search)
        return when {
            category != null -> productService.getProductsByCategory(category)
            search != null   -> productService.searchProducts(search)
            else             -> productService.getAllProducts()
        }
    }

    @GetMapping("/{id}")
    fun getProductById(@PathVariable id: Long): ProductDTO {
        logger.info("GET /products/{}", id)
        return productService.getProductById(id)
    }

    @PostMapping
    fun createProduct(@RequestBody dto: ProductDTO): ResponseEntity<ProductDTO> {
        logger.info("POST /products - name={}", dto.name)
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(dto))
    }

    @PutMapping("/{id}")
    fun updateProduct(@PathVariable id: Long, @RequestBody dto: ProductDTO): ProductDTO {
        logger.info("PUT /products/{}", id)
        return productService.updateProduct(id, dto)
    }

    @DeleteMapping("/{id}")
    fun deleteProduct(@PathVariable id: Long): ResponseEntity<Void> {
        logger.info("DELETE /products/{}", id)
        productService.deleteProduct(id)
        return ResponseEntity.noContent().build()
    }
}
