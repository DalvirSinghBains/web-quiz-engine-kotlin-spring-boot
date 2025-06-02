package engine.services

import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class AuthenticationProviderImpl(
    private val userDetailsService: UserDetailsService,
    private val passwordEncoder: PasswordEncoder
) : AuthenticationProvider {

    override fun authenticate(authentication: Authentication?): Authentication {
        val username: String = authentication!!.name
        val password: String = authentication.credentials.toString()

        val userDetails = userDetailsService.loadUserByUsername(username)

        if (passwordEncoder.matches(password, userDetails.password)) {
            return UsernamePasswordAuthenticationToken(
                username,
                password,
                userDetails.authorities
            )
        } else {
            throw BadCredentialsException("Something went wrong!")
        }
    }

    override fun supports(authenticationType: Class<*>?): Boolean {
        return authenticationType
            ?.equals(UsernamePasswordAuthenticationToken::class.java)?:false
    }
}