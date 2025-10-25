package com.fiap.usu.config;

import com.fiap.usu.security.JwtAuthFilter;
import com.fiap.usu.security.JwtUtil;
import com.fiap.usu.services.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Classe de configuração principal de segurança da aplicação.
 *
 * <p>
 * Responsável por definir as políticas de autenticação, autorização e filtros
 * de segurança utilizados pelo Spring Security.
 * </p>
 *
 * <p>
 * Esta configuração implementa autenticação baseada em JWT (JSON Web Token),
 * desativa o gerenciamento de sessão padrão (tornando a aplicação stateless) e
 * define quais rotas podem ser acessadas sem autenticação.
 * </p>
 *
 * <h2>Principais pontos:</h2>
 * <ul>
 * <li>Desabilita CSRF (Cross-Site Request Forgery), pois a aplicação utiliza
 * JWT.</li>
 * <li>Define a política de sessão como {@code STATELESS}, eliminando o uso de
 * sessão HTTP.</li>
 * <li>Registra o filtro {@link JwtAuthFilter} antes do
 * {@link UsernamePasswordAuthenticationFilter}.</li>
 * <li>Permite o acesso público às rotas de autenticação, cadastro de usuários e
 * documentação (Swagger).</li>
 * </ul>
 *
 * <p>
 * Exemplo de rotas públicas:
 * </p>
 *
 * <pre>
 * POST /users        -> Criação de novo usuário (cadastro)
 * /auth/**           -> Autenticação (login, refresh, etc.)
 * /swagger-ui/**     -> Documentação da API
 * /v3/api-docs/**    -> Especificação OpenAPI
 * </pre>
 *
 * <p>
 * Qualquer outra rota exige a presença de um token JWT válido no cabeçalho
 * {@code Authorization}.
 * </p>
 *
 * @author Matheus
 * @since 2025-10
 */
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final UserService userService;

    public SecurityConfig(JwtUtil jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configuração principal da cadeia de filtros de segurança do Spring
     * Security.
     *
     * <p>
     * Define o comportamento de autenticação e autorização da aplicação,
     * adicionando o filtro JWT e configurando as rotas públicas e privadas.
     * </p>
     *
     * @param http instância do {@link HttpSecurity} para personalização das
     *             regras
     * @return {@link SecurityFilterChain} construída e registrada no contexto
     * do Spring
     * @throws Exception caso ocorra algum erro de configuração
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        var jwtFilter = new JwtAuthFilter(jwtUtil, userService);
        http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.POST, "/users").permitAll()
                        .requestMatchers("/auth/**", "/h2-console/**", "/swagger-ui/**", "/v3/api-docs/**")
                        .permitAll().anyRequest().authenticated());

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}
