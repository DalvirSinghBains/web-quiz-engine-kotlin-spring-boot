package engine.dto

data class QuizResponseDTO(
    val id: Long?,
    val title: String,
    val text: String,
    val options: List<String>
)