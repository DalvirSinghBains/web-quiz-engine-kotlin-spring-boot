package engine.repository

import engine.model.AppUser
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AppUserRepository: JpaRepository<AppUser, Long> {
    fun findAppUserByUsername (username: String): AppUser?
}