package engine.repository

import engine.model.QuizAttempt
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface QuizAttemptRepository: JpaRepository<QuizAttempt, Long> {
    fun findQuizAttemptsByUsername(username: String, pageable: Pageable): Page<QuizAttempt>
}