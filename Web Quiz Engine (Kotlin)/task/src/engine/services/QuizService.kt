package engine.services

import engine.dto.AnswerSubmission
import engine.dto.QuizAttemptDTO
import engine.dto.QuizFeedback
import engine.dto.QuizResponseDTO
import engine.model.Quiz
import engine.model.QuizAttempt
import engine.repository.AppUserRepository
import engine.repository.QuizAttemptRepository
import engine.repository.QuizRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.*
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.jvm.optionals.getOrNull

@Service
class QuizService(
    @Autowired val quizRepository: QuizRepository,
    @Autowired val appUserRepository: AppUserRepository,
    @Autowired val quizAttemptRepository: QuizAttemptRepository
) {

    fun saveQuiz(quiz: Quiz, username: String): Quiz {
        quiz.appUser = appUserRepository.findAppUserByUsername(username)
        return quizRepository.save(quiz)
    }

    fun findQuizById(username: String, quizId: Long):
            ResponseEntity<Any> {
        val user = appUserRepository.findAppUserByUsername(username)
            ?: return ResponseEntity(HttpStatus.UNAUTHORIZED)
        val quiz = quizRepository.findById(quizId).getOrNull()
            ?: return notFound().build()
        return ok(quiz)
    }

    fun findAllQuizzes(username: String, page: Int): ResponseEntity<Any> {
        val user = appUserRepository.findAppUserByUsername(username)
            ?: return ResponseEntity<Any>(username, HttpStatus.UNAUTHORIZED)

        val pageRequest = PageRequest.of(page, 10)
        val quizList = quizRepository.findAll(pageRequest)
            .map { quiz -> QuizResponseDTO(quiz.id, quiz.title,quiz.text, quiz.options)}
        return ResponseEntity(quizList, HttpStatus.OK)
    }

    fun solveQuiz(
        username: String,
        submission: AnswerSubmission,
        id: Long
    ): ResponseEntity<Any> {

        val user = appUserRepository.findAppUserByUsername(username)
            ?: return ResponseEntity(HttpStatus.UNAUTHORIZED)

        val correct = QuizFeedback(true, "Congratulations, you're right!")
        val inCorrect = QuizFeedback(false, "Wrong answer! Please, try again.")

        val savedQuizWithValidId = quizRepository.findById(id).getOrNull()
            ?: return notFound().build()

        val actualAnswer = savedQuizWithValidId.answer.toString()
        val submittedAnswer = submission.answer.toString()

        return if (actualAnswer == submittedAnswer) {
            val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
            quizAttemptRepository.save(QuizAttempt(null, savedQuizWithValidId,
                formatter.format(LocalDateTime.now()), user.username))
            ok().body(correct)
        } else {
            ok().body(inCorrect)
        }
    }

    fun deleteQuiz(username: String, quizId: Long): ResponseEntity<Any> {

        val user = appUserRepository.findAppUserByUsername(username)
            ?: return ResponseEntity(HttpStatus.UNAUTHORIZED)

        val savedQuizWithValidId = quizRepository.findById(quizId).getOrNull()
            ?: return notFound().build()

        if (savedQuizWithValidId.appUser?.username != user.username) {
            return ResponseEntity(HttpStatus.FORBIDDEN)
        }

        val userCheck = quizRepository.findById(quizId).get()
        if (userCheck.appUser?.username != username) {
            return status(HttpStatus.FORBIDDEN).build()
        }

        quizRepository.deleteById(quizId)
        return noContent().build()
    }

    fun findAllCompletedQuizzes(username: String, page: Int): ResponseEntity<Any> {

        val user = appUserRepository.findAppUserByUsername(username)
            ?: return ResponseEntity(HttpStatus.UNAUTHORIZED)

        val pageRequest = PageRequest.of(page, 10,
            Sort.Direction.DESC,"completedAt")

        val quizList = quizAttemptRepository
            .findQuizAttemptsByUsername(user.username, pageRequest)
            .map {
                it.quizId.id?.let { it1 -> QuizAttemptDTO(it1, it.completedAt) }
            }
        return ok(quizList)
    }
}
