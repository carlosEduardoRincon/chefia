package com.chefia.core.service;

import com.chefia.core.port.output.user.UserRepositoryOutputPort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticatorService implements UserDetailsService {

    private final UserRepositoryOutputPort userRepositoryOutputPort;

    public AuthenticatorService(UserRepositoryOutputPort userRepositoryOutputPort) {
        this.userRepositoryOutputPort = userRepositoryOutputPort;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var userReturned = this.userRepositoryOutputPort.findByLogin(username);
        return userReturned.orElse(null);
    }
}
