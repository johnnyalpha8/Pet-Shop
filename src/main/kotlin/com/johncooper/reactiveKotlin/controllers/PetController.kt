package com.johncooper.reactiveKotlin.controllers

import com.johncooper.reactiveKotlin.models.Pet
import com.johncooper.reactiveKotlin.models.User
import com.johncooper.reactiveKotlin.repositories.PetRepository
import com.johncooper.reactiveKotlin.requests.PetCreateRequest
import com.johncooper.reactiveKotlin.requests.UserUpdateRequest
import com.johncooper.reactiveKotlin.responses.PagingResponse
import com.johncooper.reactiveKotlin.responses.PetCreateResponse
import com.johncooper.reactiveKotlin.responses.UserUpdateResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.coroutines.reactive.awaitFirstOrElse
import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import javax.validation.Valid
import io.swagger.v3.oas.annotations.media.Content as MediaContent
import io.swagger.v3.oas.annotations.media.Schema as MediaSchema

@RestController
@RequestMapping("/catalog", produces = [MediaType.APPLICATION_JSON_VALUE])
internal class PetController {

    @Autowired
    lateinit var petRepository: PetRepository

    @Operation(operationId = "createPet", summary = "Create pet")
    @PostMapping("/search") //TODO choose proper path name
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
        val id = createdPet?.id ?:
        throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing id for created pet")

        return PetCreateResponse(
            id = createdPet.id!!,
            name = createdPet.name,
            breed = createdPet.breed,
            description = createdPet.description,
            type = createdPet.type,
            price = createdPet.price
        )
    }

    @GetMapping(value = ["/search"])//TODO update api.json
    @Operation(operationId = "findPetByName", summary = "Find a pet by name")
    suspend fun searchPetByName(
        @RequestParam(required = false)
//        @Size(min = 1, message = "SEARCH_BUSINESS_BUSINESS_NAME_LENGTH_VALIDATION_ERROR_MESSAGE")
        petName: String,
    ): ResponseEntity<Pet>? { //TODO will be a list of pets
        val pet = petRepository.findByName(petName).awaitFirst()
      return ResponseEntity.ok(pet)

    }//TODO controller advice


    //TODO list
//    @Operation(operationId = "listUsers", summary = "List users")
//    @GetMapping("")
//    suspend fun listUsers(
//        @RequestParam pageNo: Int = 1,
//        @RequestParam pageSize: Int = 10
//    ): PagingResponse<User> {
//        val limit = pageSize
//        val offset = (limit * pageNo) - limit
//        val list = userRepository.findAllUsers(limit, offset).collectList().awaitFirst()
//        val total = userRepository.count().awaitFirst()
//        return PagingResponse(total, list)
//    }

//TODO could use @ApiModel and @ApiParameter and @ApiProperties?? blag from CJ
//like this
  //@ApiModel("Add Business Product Request")
  //data class AddBusinessProductRequest(
  //    @ApiModelProperty(value = "External system", position = 1, example = "FMS", required = true)
  //    @field:NotNull
  //    val externalSystem: BusinessProductExternalSystem?,
  //
  //    @ApiModelProperty(
  //        value = "Product reference in external system",
  //        position = 3,
  //        example = "47de05dc-14e6-4b89-b90e-65332fa273f7"
  //    )
  //    @field:Size(min = 1, max = 36)
  //    val externalSystemProductRef: String? = null,
//    @Operation(operationId = "updateUser", summary = "List users")
//    @PatchMapping("/{userId}")
//    @ApiResponse(responseCode = "404", description = "Book not found",
//        content = [MediaContent(
//            mediaType = "application/json",
//            schema = MediaSchema(implementation = Pet::class)
//        )]
//    )
//    suspend fun updateUser(
//        @PathVariable userId: Int,
//        @RequestBody @Valid userUpdateRequest: UserUpdateRequest
//    ): UserUpdateResponse {
//        val existingDBUser = userRepository.findById(userId).awaitFirstOrElse {
//            throw ResponseStatusException(HttpStatus.NOT_FOUND, "User #$userId doesn't exist")
//        }
//
//        val duplicateUser = userRepository.findByEmail(userUpdateRequest.email).awaitFirstOrNull()
//        if (duplicateUser != null && duplicateUser.id != userId) {
//            throw ResponseStatusException(
//                HttpStatus.BAD_REQUEST,
//                "Duplicate user: user with email ${userUpdateRequest.email} already exists"
//            )
//        }
//
//        val updatedUser = try {
//            existingDBUser.email = userUpdateRequest.email
//            existingDBUser.firstName = userUpdateRequest.firstName ?: existingDBUser.firstName
//            existingDBUser.lastName = userUpdateRequest.lastName ?: existingDBUser.lastName
//            userRepository.save(existingDBUser).awaitFirst()
//        }catch (e: Exception){
//            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to update user", e)
//        }
//
//        return UserUpdateResponse(
//            id = updatedUser.id,
//            email = updatedUser.email,
//            firstName = updatedUser.firstName,
//            lastName = updatedUser.lastName
//        )
//    }

    //TODO refactor to take Pet
//    @Operation(operationId = "deleteUser", summary = "Delete user")
//    @DeleteMapping("/{userId}")
//    suspend fun deleteUser(
//        @PathVariable userId: Int
//    ) {
//        val existingUser = userRepository.findById(userId).awaitFirstOrElse {
//            throw ResponseStatusException(HttpStatus.NOT_FOUND, "User #$userId not found")
//        }
//        userRepository.delete(existingUser).subscribe()
//
//    }

}



//TODO what does  @field:NotNull do??
//
//As mentioned above @NotNull does nothing on its own. A good way of using @NotNull would be using it with Objects.requireNonNull
//
//public class Foo {
//    private final Bar bar;
//
//    public Foo(@NotNull Bar bar) {
//        this.bar = Objects.requireNonNull(bar, "bar must not be null");
//    }
//}
//val pet = Pet(name =  null, description = "kj", type = "jhj", price = "jhjh", breed = "jhh")