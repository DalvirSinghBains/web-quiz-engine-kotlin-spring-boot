package engine.controller

import engine.dto.RegistrationRequest
import engine.dto.RegistrationResponse
import engine.services.MyMapper
import engine.services.UserRegistrationService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(
    private val myMapper: MyMapper,
    private val userRegistrationService: UserRegistrationService
) {
    @PostMapping("/api/register")
    fun register(@Valid @RequestBody registration: RegistrationRequest): ResponseEntity<RegistrationResponse> {
        val appUser = myMapper.registrationToUserEntity(registration)
        return userRegistrationService.registerUser(appUser)
    }
}