package com.johncooper.reactiveKotlin.models

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import javax.validation.constraints.NotNull

@JsonSerialize
@Table("pets")
data class Pet(
    @Id
    var id: Int? = null,
    @field:NotNull
    val name: String,
    val description: String,
    val type: String,
    val price: String,
    val breed: String)