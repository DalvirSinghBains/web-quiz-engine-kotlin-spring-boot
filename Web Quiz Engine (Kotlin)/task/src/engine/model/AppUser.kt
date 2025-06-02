package engine.model

import jakarta.persistence.*
import org.springframework.stereotype.Component

@Component
@Entity
class AppUser(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long? = null,
    @Column(unique=true)
    val username: String,
    var password: String,
    var authority: String? = "USER"
)