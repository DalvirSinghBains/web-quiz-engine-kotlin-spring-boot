package engine.services

import engine.dto.AppUserAdapter
import engine.repository.AppUserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl(private val repository: AppUserRepository) : UserDetailsService {
    override fun loadUserByUsername(username: String?): UserDetails {
        val foundUser = username?.let { repository.findAppUserByUsername(it) }
            ?: throw UsernameNotFoundException("Not found")
        return AppUserAdapter(foundUser)
    }
}