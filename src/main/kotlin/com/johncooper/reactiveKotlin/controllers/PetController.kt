package com.johncooper.reactiveKotlin.controllers

import com.johncooper.reactiveKotlin.models.Pet
import com.johncooper.reactiveKotlin.repositories.PetRepository
import com.johncooper.reactiveKotlin.requests.PetCreateRequest
import com.johncooper.reactiveKotlin.responses.PetCreateResponse
import io.swagger.v3.oas.annotations.Operation
import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/catalog", produces = [MediaType.APPLICATION_JSON_VALUE])
class PetController {

    @Autowired
    lateinit var petRepository: PetRepository

    @Operation(operationId = "createPet", summary = "Create pet")
    @PostMapping("/pets")
    suspend fun createPet(
        @RequestBody request: PetCreateRequest
    ): PetCreateResponse {
        val existingPet = petRepository.findByName(request.name).awaitFirstOrNull()
        if(existingPet != null){
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Duplicate pet")
        }
        val pet = Pet(
            id = null,
            name = request.name,
            breed = request.breed,
            description = request.description,
            type = request.type,
            price = request.price
        )
        val createdPet = try {
            petRepository.save(pet).awaitFirstOrNull()
        } catch (e: Exception){
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Unable to create pet", e)
        }
        createdPet?.id ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing id for created pet")

        return PetCreateResponse(
            id = createdPet.id!!,
            name = createdPet.name,
            breed = createdPet.breed,
            description = createdPet.description,
            type = createdPet.type,
            price = createdPet.price
        )
    }

    @GetMapping(value = ["/pets"])
    @Operation(operationId = "findPetByName", summary = "Find a pet by name")
    suspend fun searchPetByName(
        @RequestParam(required = false)
        petName: String,
    ): ResponseEntity<Pet>? {
        val pet = petRepository.findByName(petName).awaitFirst()
      return ResponseEntity.ok(pet)
    }
}
