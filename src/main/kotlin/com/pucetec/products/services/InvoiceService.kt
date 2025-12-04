package com.pucetec.products.services

import com.pucetec.products.mappers.InvoiceMapper
import com.pucetec.products.models.entities.InvoiceDetail
import com.pucetec.products.models.requests.InvoiceRequest
import com.pucetec.products.models.responses.InvoiceResponse
import com.pucetec.products.repositories.InvoiceDetailRepository
import com.pucetec.products.repositories.InvoiceRepository
import com.pucetec.products.repositories.ProductRepository
import org.springframework.stereotype.Service
import java.util.stream.Collectors

@Service
open class InvoiceService(
    private val invoiceRepository: InvoiceRepository,
    private val invoiceDetailRepository: InvoiceDetailRepository,
    private val productRepository: ProductRepository,
    private val invoiceMapper: InvoiceMapper
) {

    open fun save(request: InvoiceRequest): InvoiceResponse {
        // 1. Crear la cabecera de la factura
        val invoiceEntity = invoiceMapper.toEntity(request)

        // 2. Procesar los detalles (productos)
        request.details.forEach { detailRequest ->
            // Buscar el producto en la BD
            val product = productRepository.findById(detailRequest.productId)
                .orElseThrow { RuntimeException("Producto no encontrado id: ${detailRequest.productId}") }

            val detailEntity = InvoiceDetail(
                totalPrice = detailRequest.totalPrice,
                product = product,
                invoice = invoiceEntity
            )

            // Agregar a la lista de la factura
            invoiceEntity.invoiceDetails.add(detailEntity)
        }

        // 3. Guardar todo en cascada
        val savedInvoice = invoiceRepository.save(invoiceEntity)

        return invoiceMapper.toResponse(savedInvoice)
    }

    open fun findAll(): List<InvoiceResponse> {
        val invoices = invoiceRepository.findAll()
        for (i in invoices) {
            // Aquí usamos el mismo nombre que declaramos arriba (invoiceDetailRepository)
            i.invoiceDetails = invoiceDetailRepository
                .findAllByInvoiceId(i.id).toMutableList()
        }
        return invoices.map { invoiceMapper.toResponse(it) }
    }
}