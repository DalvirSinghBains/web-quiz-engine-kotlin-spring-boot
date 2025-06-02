package engine.model

import jakarta.persistence.*
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction

@Entity
class Quiz(
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long? = null,
    val title: String,
    val text: String,
    @ElementCollection
    val options: List<String>,
    @ElementCollection
    val answer: List<Int>,
    @ManyToOne
    @JoinColumn(nullable = false)
    var appUser: AppUser? = null,
    @OneToMany(mappedBy = "quizId", cascade = [CascadeType.ALL], orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    var attempts: MutableSet<QuizAttempt> = mutableSetOf()
)