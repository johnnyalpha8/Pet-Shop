package com.johncooper.reactiveKotlin.requests

import java.math.BigDecimal
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty

data class PetCreateRequest(
    @field:NotEmpty
    val name: String,
    @field:NotEmpty
    val description: String,
    @field:NotEmpty
    val type: String,
    val price: String,
    @field:NotEmpty
    val breed: String)

