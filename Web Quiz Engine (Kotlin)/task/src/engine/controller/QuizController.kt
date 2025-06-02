package engine.controller

import engine.dto.AnswerSubmission
import engine.dto.CreatedQuizRequestDTO
import engine.dto.QuizResponseDTO
import engine.model.Quiz
import engine.services.MyMapper
import engine.services.QuizService
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
class QuizController @Autowired constructor(private val service: QuizService,
                                            private val myMapper: MyMapper) {

    @PostMapping("/api/quizzes")
    fun createQuiz(
        @Valid @RequestBody quiz: CreatedQuizRequestDTO,
        auth: Authentication
    ): ResponseEntity<QuizResponseDTO> {
        val username = auth.principal as String

        val postedQuiz = myMapper.requestDTOToQuizEntity(quiz)
        val quizFromDB = service.saveQuiz(postedQuiz, username)

        val responseDTO = myMapper.quizEntityToResponseDTO(quizFromDB)
        return ok().body(responseDTO)
    }

    @GetMapping("/api/quizzes/{id}")
    fun getQuizById(
        auth: Authentication,
        @PathVariable("id") quizId: Long
    ): ResponseEntity<Any> {
        val username = auth.principal as String

        val response = service.findQuizById(username, quizId)
        if (response.body is Quiz) {
            val responseDTO = myMapper.quizEntityToResponseDTO(response.body as Quiz)
            return ok().body(responseDTO)
        } else {
            return response
        }
    }

    @GetMapping("/api/quizzes")
    fun getAllQuizzes(auth: Authentication,
                      @RequestParam(defaultValue = "0") page: Int): ResponseEntity<Any> {
        val username = auth.principal as String

        val response = service.findAllQuizzes(username, page)
        if (response.body is MutableList<*>) {
            return ok().body(response.body as MutableList<*>)
        }else if (response.body is Page<*>) {
            return response
        } else {
            return response
        }
    }

    @GetMapping("/api/quizzes/completed")
    fun getAllCompletedQuizzes(auth: Authentication,
                               @RequestParam(defaultValue = "0") page: Int): ResponseEntity<Any> {
        val username = auth.principal as String
        val response = service.findAllCompletedQuizzes(username, page)
        return response
    }

    @PostMapping("/api/quizzes/{id}/solve")
    fun solveQuizById(
        auth: Authentication,
        @RequestBody submission: AnswerSubmission,
        @PathVariable("id") quizId: Long
    ): ResponseEntity<Any> {
        val username = auth.principal as String

        return service.solveQuiz(username, submission, quizId)
    }

    @DeleteMapping("/api/quizzes/{id}")
    fun deleteQuiz(
        auth: Authentication,
        @PathVariable("id") quizId: Long
    ): ResponseEntity<Any> {
        val username = auth.principal as String
        return service.deleteQuiz(username, quizId)
    }
}
