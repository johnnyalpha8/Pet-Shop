package com.johncooper.reactiveKotlin.responses

import org.springframework.data.annotation.Id
import java.math.BigDecimal
import javax.validation.constraints.NotNull

data class PetCreateResponse(
    var id: Int,
    val name: String,
    val description: String,
    val type: String,
    val price: String,
    val breed: String)

