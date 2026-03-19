package com.ecommerce.product.repository

import com.ecommerce.product.entity.Product
import org.springframework.data.jpa.repository.JpaRepository

interface ProductRepository : JpaRepository<Product, Long> {
    fun findByCategory(category: String): List<Product>
    fun findByNameContainingIgnoreCase(name: String): List<Product>
}
