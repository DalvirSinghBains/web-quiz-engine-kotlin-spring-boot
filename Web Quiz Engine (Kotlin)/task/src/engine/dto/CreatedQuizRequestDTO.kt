package engine.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.jetbrains.annotations.NotNull

data class CreatedQuizRequestDTO(
    @NotNull
    @NotBlank(message = "Name is required")
    val title: String,
    @NotNull
    @NotBlank(message = "Name is required")
    val text: String,
    @Size(min = 2)
    val options: List<String>,
    @Size(min = 0)
    val answer: List<Int> = listOf()
)