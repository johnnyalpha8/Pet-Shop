package com.johncooper.reactiveKotlin.repositories

import com.johncooper.reactiveKotlin.models.Pet
import com.johncooper.reactiveKotlin.models.User
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.r2dbc.repository.R2dbcRepository
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@EnableR2dbcRepositories
interface PetRepository : R2dbcRepository<Pet, Int> {
    fun findByName(name: String): Mono<Pet>

//    fun findByEmail(email: String): Mono<User>
//
//    @Query("SELECT * FROM users limit :limit offset :offset")
//    fun findAllUsers(limit: Int, offset: Int): Flux<User>
}