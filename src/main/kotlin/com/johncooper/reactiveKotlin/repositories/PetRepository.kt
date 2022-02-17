package com.johncooper.reactiveKotlin.repositories

import com.johncooper.reactiveKotlin.models.Pet
import org.springframework.data.r2dbc.repository.R2dbcRepository
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import reactor.core.publisher.Mono

@EnableR2dbcRepositories
interface PetRepository : R2dbcRepository<Pet, Int> {
    fun findByName(name: String): Mono<Pet>
}