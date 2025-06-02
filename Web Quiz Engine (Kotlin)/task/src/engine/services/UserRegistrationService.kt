package engine.services

import engine.dto.RegistrationResponse
import engine.model.AppUser
import engine.repository.AppUserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.badRequest
import org.springframework.http.ResponseEntity.ok
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserRegistrationService(
    @Autowired val userRepository: AppUserRepository,
    @Autowired private val passwordEncoder: PasswordEncoder
) {
    fun registerUser(appUser: AppUser): ResponseEntity<RegistrationResponse> {

        val success = RegistrationResponse(true, "success")
        val failure = RegistrationResponse(false, "user already exists")

        if (userRepository.findAppUserByUsername(appUser.username) != null) {
            return badRequest().body(failure)
        } else {
            appUser.password = passwordEncoder.encode(appUser.password)
            userRepository.save(appUser)
            return ok().body(success)
        }
    }
}