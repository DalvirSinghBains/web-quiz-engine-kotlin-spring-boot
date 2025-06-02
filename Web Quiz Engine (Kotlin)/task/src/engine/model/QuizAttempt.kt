package engine.model

import jakarta.persistence.*

@Entity
class QuizAttempt(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long? = null,
    @ManyToOne
    @JoinColumn(nullable = false)
    val quizId: Quiz,
    val completedAt: String,
    var username: String
)