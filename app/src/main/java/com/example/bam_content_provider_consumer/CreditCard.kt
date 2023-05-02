package com.example.bam_content_provider_consumer

data class CreditCard(
    val id: Long,
    val userOwnerId: Long,
    val uniqueNumber: String
)
