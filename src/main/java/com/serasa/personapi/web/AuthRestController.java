package com.serasa.personapi.web;

import com.serasa.personapi.infrastructure.exchange.error.ErrorResponse;
import com.serasa.personapi.infrastructure.exchange.request.AuthRequest;
import com.serasa.personapi.infrastructure.exchange.response.AuthResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Autenticação", description = "Realiza autenticação na API para utilização dos demais recursos")
public interface AuthRestController {

    @Operation(description = "Autentica o usuário na API e retorna um JWT para utilização nos demais recursos")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Autenticação com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Credenciais inválidas",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    ResponseEntity<AuthResponse> login(
        @Parameter(
            description = "Corpo da requisição de autenticação"
        ) AuthRequest request
    );
}
