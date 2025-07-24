package com.serasa.personapi.web;

import com.serasa.personapi.infrastructure.exchange.error.ErrorResponse;
import com.serasa.personapi.infrastructure.exchange.request.PersonRequest;
import com.serasa.personapi.infrastructure.exchange.request.PersonUpdateRequest;
import com.serasa.personapi.infrastructure.exchange.response.PaginatedPersonResponse;
import com.serasa.personapi.infrastructure.exchange.response.PersonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Pessoas", description = "Realiza as operações de criação, busca, atualização e exclusão de pessoas")
public interface PersonRestController {

    @Operation(
        summary = "Cria uma nova pessoa. Apenas usuários ADMIN podem usar este recurso",
        security = @SecurityRequirement(name = "BearerAuth")
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Pessoa criada com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PersonResponse.class))
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Token de autenticação inválido ou expirado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Usuário proibido de usar o recurso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Requisição inválida",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    ResponseEntity<PersonResponse> create(
        @Parameter(description = "Corpo da requisição da criação de pessoas") PersonRequest request
    );

    @Operation(
        summary = "Busca pessoas por filtros e paginação. Todos os usuários podem acessar este recurso",
        security = @SecurityRequirement(name = "BearerAuth")
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Busca realizada com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaginatedPersonResponse.class))
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Token de autenticação inválido ou expirado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
        ),
    })
    ResponseEntity<PaginatedPersonResponse> search(
        @Parameter(description = "Nome da pessoa") String name,
        @Parameter(description = "Idade da pessoa") Integer age,
        @Parameter(description = "CEP da pessoa") String cep,
        @Parameter(description = "Página da paginação") Integer page,
        @Parameter(description = "Tamanho da página de resultados") Integer size
    );

    @Operation(
        summary = "Atualiza os dados de uma pessoa dado o seu id. Apenas usuários ADMIN podem usar este recurso",
        security = @SecurityRequirement(name = "BearerAuth")
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Pessoa atualizada com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PersonResponse.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Requisição inválida",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Token de autenticação inválido ou expirado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Usuário proibido de usar o recurso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Pessoa não encontrada",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    ResponseEntity<PersonResponse> update(
        @Parameter(description = "ID da pessoa a ser atualizada") Long id,
        @Parameter(description = "Corpo da requisição da atualização de pessoa") PersonUpdateRequest request
    );

    @Operation(
        summary = "Exclui uma pessoa dado seu ID. Apenas usuários ADMIN podem usar este recurso",
        security = @SecurityRequirement(name = "BearerAuth")
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Pessoa excluída com sucesso"
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Token de autenticação inválido ou expirado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Usuário proibido de usar o recurso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Pessoa não encontrada",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    ResponseEntity<Void> delete(
        @Parameter(description = "ID da pessoa a ser excluída") Long id
    );
}
