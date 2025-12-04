package com.pucetec.products.models.requests

import com.fasterxml.jackson.annotation.JsonProperty

data class InvoiceRequest(
    @JsonProperty(value = "client_ci")
    val clientCi: String,

    @JsonProperty(value = "client_name")
    val clientName: String,

    @JsonProperty(value = "client_address")
    val clientAddress: String,

    @JsonProperty(value = "total_before_taxes")
    val totalBeforeTaxes: Float? = null,

    val taxes: Float? = null,

    @JsonProperty(value = "total_after_taxes")
    val totalAfterTaxes: Float? = null,

    // --- NUEVO CAMPO AGREGADO ---
    // Esta lista permitirá recibir los productos dentro de la factura
    val details: List<InvoiceDetailRequest> = emptyList()
)
/**
 * {
 * "client_ci": "1738383883"
 * }
 */