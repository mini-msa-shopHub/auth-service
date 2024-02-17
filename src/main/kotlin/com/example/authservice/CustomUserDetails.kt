package com.example.authservice

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails

class CustomUserDetails(
    username: String?,
    password: String?,
    enabled: Boolean,
    accountNonExpired: Boolean,
    credentialsNonExpired: Boolean,
    accountNonLocked: Boolean,
    authorities: MutableCollection<out GrantedAuthority>?,
    id: Long?
) : User(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities) {


}

