package com.chefia.infra.security;

import com.chefia.core.port.output.user.UserRepositoryOutputPort;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UserRepositoryOutputPort userRepositoryOutputPort;

    public SecurityFilter(TokenService tokenService,
                          UserRepositoryOutputPort userRepositoryOutputPort
    ) {
        this.tokenService = tokenService;
        this.userRepositoryOutputPort = userRepositoryOutputPort;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var tokenJWT = getTokenFromRequest(request);

        if (tokenJWT != null) {
            var login = this.tokenService.verifyJWT(tokenJWT);
            var user = this.userRepositoryOutputPort.findByLogin(login);

            if (user.isPresent()) {
                var authentication = new UsernamePasswordAuthenticationToken(user, null, user.get().getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        var authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null) {
            return authorizationHeader.replace("Bearer ", "");
        }
        return null;
    }
}