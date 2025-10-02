package com.chefia.core.usecases.impl.login;

import com.chefia.core.gateway.UserGateway;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticatorService implements UserDetailsService {

    private final UserGateway userGateway;

    public AuthenticatorService(UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var userReturned = this.userGateway.findByLogin(username);
        return userReturned.orElse(null);
    }
}
