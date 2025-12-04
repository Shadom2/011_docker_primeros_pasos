package com.pucetec.products.models.requests

import com.fasterxml.jackson.annotation.JsonProperty

data class InvoiceDetailRequest(
    @JsonProperty(value = "product_id")
    val productId: Long,

    // Lo hacemos opcional (?) y null por defecto,
    // porque al crear una factura nueva, ¡aún no tenemos ID de factura!
    @JsonProperty(value = "invoice_id")
    val invoiceId: Long? = null,

    // Agregamos esto porque tu Entidad InvoiceDetail tiene un campo totalPrice
    // y necesitamos recibirlo desde Postman.
    @JsonProperty(value = "total_price")
    val totalPrice: Float
)