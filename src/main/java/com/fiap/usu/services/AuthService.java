package com.fiap.usu.services;

import com.fiap.usu.dtos.auth.AuthResponseDto;
import com.fiap.usu.dtos.auth.LoginRequestDto;
import com.fiap.usu.entities.User;
import com.fiap.usu.entities.UserType;
import com.fiap.usu.enums.EnumUserType;
import com.fiap.usu.exceptions.InvalidCredentialsException;
import com.fiap.usu.exceptions.InvalidCurrentPasswordException;
import com.fiap.usu.security.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthService(UserService userService, JwtUtil jwtUtil, BCryptPasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponseDto login(LoginRequestDto req) {
        try {
            final User user = userService.getUserByLoginOrEmail(req.identifier());

            if (!passwordEncoder.matches(req.password(), user.getPassword())) {
                throw new InvalidCurrentPasswordException();
            }

            final Set<EnumUserType> roles = user.getTypes().stream().map(UserType::getType).collect(Collectors.toSet());

            String token = jwtUtil.generateToken(String.valueOf(user.getId()), roles);
            return new AuthResponseDto(token);
        } catch (Exception e) {
            throw new InvalidCredentialsException();
        }
    }
}
