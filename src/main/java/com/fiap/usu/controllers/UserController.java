package com.fiap.usu.controllers;

import com.fiap.usu.dtos.user.*;
import com.fiap.usu.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("users")
@Tag(name = "Users", description = "Operações de gerenciamento de usuários")
public class UserController {

    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Consultar usuários", parameters = {
            @Parameter(name = "Authorization", description = "Token JWT", required = true, in = ParameterIn.HEADER),
            @Parameter(name = "page", description = "Número da página (começa em 0)", in = ParameterIn.QUERY),
            @Parameter(name = "size", description = "Quantidade de registros por página", in = ParameterIn.QUERY),
            @Parameter(name = "name", description = "Filtrar usuários pelo nome", in = ParameterIn.QUERY)})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listar usuários encontrados", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "403", description = "Usuário não tem permissão para acessar este recurso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class))),})
    @GetMapping("/v1")
    public ResponseEntity<Page<UserPagedDto>> getUsersPaged(@ParameterObject Pageable pageable,
                                                            @Parameter(description = "Filtrar usuários pelo nome") @RequestParam(name = "name",
                                                                    required = false) String name) {
        final Page<UserPagedDto> user = userService.getUsersPaged(pageable, name);
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Cria um novo usuário",
            parameters = {@Parameter(name = "Authorization", description = "Token JWT", in = ParameterIn.HEADER)})
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class)))})
    @PostMapping("/v1")
    public ResponseEntity<Void> create(@RequestBody @Valid UserCreateDto userCreateDto) {
        Long idUser = userService.create(userCreateDto);

        URI location = URI.create(String.format("/users/%d", idUser));
        return ResponseEntity.created(location).build();
    }

    @Operation(summary = "Atualizar usuário pelo ID", parameters = {
            @Parameter(name = "Authorization", description = "Token JWT", required = true, in = ParameterIn.HEADER),
            @Parameter(name = "id", description = "ID do usuário", required = true, in = ParameterIn.PATH)})
    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "Usuário atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "403", description = "Usuário não tem permissão para acessar este recurso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class)))})
    @PatchMapping("/v1/{id}")
    public ResponseEntity<Void> update(@PathVariable("id") Long id, @Valid @RequestBody UserUpdateDto dto) {
        Long idUser = userService.update(id, dto);

        URI location = URI.create(String.format("/users/%d", idUser));
        return ResponseEntity.noContent().location(location).build();
    }

    @Operation(summary = "Atualiza a senha do usuário", parameters = {
            @Parameter(name = "Authorization", description = "Token JWT", required = true, in = ParameterIn.HEADER),
            @Parameter(name = "Accept-Language", description = "Idioma da resposta", in = ParameterIn.HEADER)})
    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "Senha atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "403", description = "Usuário não tem permissão para acessar este recurso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class)))})
    @PatchMapping("/v1/password")
    public ResponseEntity<Void> passwordUpdate(@RequestBody @Valid PasswordUpdateDto passwordUpdateDto) {
        userService.passwordUpdate(passwordUpdateDto);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Consulta usuário pelo ID", parameters = {
            @Parameter(name = "Authorization", description = "Token JWT", required = true, in = ParameterIn.HEADER),
            @Parameter(name = "Accept-Language", description = "Idioma da resposta", in = ParameterIn.HEADER),
            @Parameter(name = "id", description = "ID do usuário", required = true, in = ParameterIn.PATH)})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário encontrado",
                    content = @Content(schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "403", description = "Usuário não tem permissão para acessar este recurso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class)))})
    @GetMapping("/v1/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable("id") Long id) {
        final UserDto user = userService.getUserDto(id);
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Deleta usuário pelo ID", parameters = {
            @Parameter(name = "Authorization", description = "Token JWT", required = true, in = ParameterIn.HEADER),
            @Parameter(name = "id", description = "ID do usuário", required = true, in = ParameterIn.PATH)})
    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "Usuário deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "403", description = "Usuário não tem permissão para acessar este recurso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class)))})
    @DeleteMapping("/v1/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
