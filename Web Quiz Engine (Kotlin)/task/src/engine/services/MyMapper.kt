package engine.services

import engine.dto.CreatedQuizRequestDTO
import engine.dto.QuizResponseDTO
import engine.dto.RegistrationRequest
import engine.model.AppUser
import engine.model.Quiz
import org.springframework.stereotype.Component

@Component
class MyMapper {

    fun quizEntityToResponseDTO(quiz: Quiz): QuizResponseDTO {
        return QuizResponseDTO(
            id = quiz.id, title = quiz.title,
            text = quiz.text, options = quiz.options
        )
    }

    fun requestDTOToQuizEntity(dto: CreatedQuizRequestDTO): Quiz {
        return Quiz(
            title = dto.title, text = dto.text,
            options = dto.options, answer = dto.answer
        )
    }

    fun registrationToUserEntity(dto: RegistrationRequest): AppUser {
        return AppUser(username = dto.email, password = dto.password)
    }
}