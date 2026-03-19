package com.ecommerce.product.service

import com.ecommerce.common.dto.ProductDTO
import com.ecommerce.common.exception.ResourceNotFoundException
import com.ecommerce.product.entity.Product
import com.ecommerce.product.repository.ProductRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProductService(private val productRepository: ProductRepository) {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun getAllProducts(): List<ProductDTO> {
        val products = productRepository.findAll().map { it.toDTO() }
        logger.debug("getAllProducts - found {} products", products.size)
        return products
    }

    fun getProductById(id: Long): ProductDTO {
        logger.debug("getProductById - id={}", id)
        return productRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Product not found with id: $id") }
            .toDTO()
    }

    fun getProductsByCategory(category: String): List<ProductDTO> {
        val products = productRepository.findByCategory(category).map { it.toDTO() }
        logger.debug("getProductsByCategory - category={}, found={}", category, products.size)
        return products
    }

    fun searchProducts(name: String): List<ProductDTO> {
        val products = productRepository.findByNameContainingIgnoreCase(name).map { it.toDTO() }
        logger.debug("searchProducts - query={}, found={}", name, products.size)
        return products
    }

    @Transactional
    fun createProduct(dto: ProductDTO): ProductDTO {
        val product = Product(
            name = dto.name,
            description = dto.description,
            price = dto.price,
            category = dto.category,
            imageUrl = dto.imageUrl
        )
        val saved = productRepository.save(product).toDTO()
        logger.info("Product created - id={}, name={}", saved.id, saved.name)
        return saved
    }

    @Transactional
    fun updateProduct(id: Long, dto: ProductDTO): ProductDTO {
        val product = productRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Product not found with id: $id") }
        product.name = dto.name
        product.description = dto.description
        product.price = dto.price
        product.category = dto.category
        product.imageUrl = dto.imageUrl
        val saved = productRepository.save(product).toDTO()
        logger.info("Product updated - id={}", id)
        return saved
    }

    @Transactional
    fun deleteProduct(id: Long) {
        if (!productRepository.existsById(id)) {
            throw ResourceNotFoundException("Product not found with id: $id")
        }
        productRepository.deleteById(id)
        logger.info("Product deleted - id={}", id)
    }

    private fun Product.toDTO() = ProductDTO(
        id = id, name = name, description = description,
        price = price, category = category, imageUrl = imageUrl
    )
}
