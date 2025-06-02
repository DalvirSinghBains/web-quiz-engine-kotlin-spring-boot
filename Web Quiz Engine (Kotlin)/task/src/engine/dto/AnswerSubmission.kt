package engine.dto

import jakarta.validation.constraints.Size

data class AnswerSubmission(
    @Size(min = 0)
    val answer: List<Int>
)