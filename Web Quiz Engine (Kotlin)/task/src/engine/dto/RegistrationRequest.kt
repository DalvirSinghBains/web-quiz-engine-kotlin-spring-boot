package engine.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class RegistrationRequest(
    @field:NotNull
    @field:NotBlank
    @field:Pattern(regexp = "[a-z]+[0-9]*@[a-z]+\\.[a-z]{3}")
    val email: String,

    @field:NotNull
    @field:NotBlank
    @field:Size(min = 5)
    val password: String
)
