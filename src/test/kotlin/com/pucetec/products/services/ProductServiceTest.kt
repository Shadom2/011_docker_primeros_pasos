package com.pucetec.products.services

import com.pucetec.products.exceptions.ProductAlreadyExistsException
import com.pucetec.products.exceptions.ProductNotFoundException
import com.pucetec.products.exceptions.StockOutOfRangeException
import com.pucetec.products.mappers.ProductMapper
import com.pucetec.products.models.entities.Product
import com.pucetec.products.models.requests.ProductRequest
import com.pucetec.products.repositories.ProductRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import java.util.Optional

class ProductServiceTest {

    private lateinit var productRepositoryMock: ProductRepository
    private lateinit var productMapper: ProductMapper
    private lateinit var productService: ProductService

    @BeforeEach
    fun init() {
        productRepositoryMock = mock(ProductRepository::class.java)
        productMapper = ProductMapper() // El profe usa el mapper real, no un mock

        productService = ProductService(
            productRepository = productRepositoryMock,
            productMapper = productMapper
        )
    }

    @Test
    fun `SHOULD save a product GIVEN a valid product request`() {
        val request = ProductRequest(
            name = "telefono",
            price = 0.5,
            stock = 9
        )

        val productToSave = Product(
            name = "telefono",
            price = 0.5,
            stock = 9,
        )

        val savedProduct = Product(
            name = "telefono",
            price = 0.5,
            stock = 9,
        ).apply { id = 1L }

        `when`(productRepositoryMock.findByName("telefono")).thenReturn(null)

        `when`(productRepositoryMock.save(any())).thenReturn(savedProduct)

        val response = productService.save(request)

        assertEquals(savedProduct.id, response.id)
    }

    @Test
    fun `SHOULD NOT save a product GIVEN an existing product name`() {
        val request = ProductRequest(
            name = "telefono",
            price = 0.5,
            stock = 9
        )

        val savedProduct = Product(
            name = "telefono",
            price = 0.5,
            stock = 9,
        ).apply { id = 1L }

        `when`(productRepositoryMock.findByName("telefono")).thenReturn(savedProduct)

        assertThrows(ProductAlreadyExistsException::class.java) {
            productService.save(request)
        }
    }

    @Test
    fun `SHOULD NOT save a product GIVEN a stock equal or bigger than 10`() {
        val request = ProductRequest(
            name = "telefono",
            price = 0.5,
            stock = 11
        )

        assertThrows(StockOutOfRangeException::class.java) {
            productService.save(request)
        }
    }

    @Test
    fun `SHOULD return a product response GIVEN a valid product id`() {
        val productId = 23L

        val mockProduct = Product(
            name = "telefono",
            price = 0.5,
            stock = 10,
        ).apply {
            id = productId
        }

        `when`(productRepositoryMock.findById(productId)).thenReturn(Optional.of(mockProduct))

        val response = productService.findById(productId)

        assertEquals(mockProduct.name, response.name)
        assertEquals(mockProduct.id, response.id)
        assertEquals(mockProduct.price, response.price)
        assertEquals(mockProduct.stock, response.stock)
    }

    @Test
    fun `SHOULD return a ProductNotFoundException GIVEN a non existing product id`() {
        `when`(productRepositoryMock.findById(88L)).thenReturn(Optional.empty())

        assertThrows(ProductNotFoundException::class.java) {
            productService.findById(88L)
        }
    }
}