package com.fiap.usu.controllers;

import com.fiap.usu.dtos.auth.AuthResponseDto;
import com.fiap.usu.dtos.auth.LoginRequestDto;
import com.fiap.usu.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Operações de login de usuários")
public class AuthController {

    private final AuthService authService;

    AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Autenticar usuário e gerar token JWT",
            description = "Recebe e-mail e senha e retorna token JWT se credenciais forem válidas.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Dados de login do usuário",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LoginRequestDto.class))))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autenticação realizada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class)))})
    @PostMapping("/v1/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginRequestDto req) {
        AuthResponseDto response = authService.login(req);
        return ResponseEntity.ok(response);
    }
}
