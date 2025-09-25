//package com.chefia.core.service;
//
//import com.chefia.core.port.output.user.UserRepository;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//@Service
//public class AuthenticatorService implements UserDetailsService {
//
//    private final UserRepository userRepository;
//
//    public AuthenticatorService(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        return this.userRepository.findByLogin(username);
//    }
//}
