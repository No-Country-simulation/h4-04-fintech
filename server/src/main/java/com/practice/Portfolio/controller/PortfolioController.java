package com.practice.Portfolio.controller;

import com.practice.Portfolio.dtoRequest.InvestmentRequestDto;
import com.practice.Portfolio.dtoRequest.PortfolioRequestDto;
import com.practice.Portfolio.dtoResponse.PortfolioListResponseDto;
import com.practice.Portfolio.dtoResponse.PortfolioPageResponseDto;
import com.practice.Portfolio.dtoResponse.PortfolioResponseDto;
import com.practice.Portfolio.dtoResponse.PortfolioValueResponseDto;
import com.practice.Portfolio.service.PortfolioServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping
@Tag(name = "Portfolio", description = "Portfolio API")
public class PortfolioController {
    private final PortfolioServiceImpl portfolioServiceImpl;


    @Operation(summary = "Obtener todos los Portfolio", description = "Devuelve todos los Portfolio")
    @ApiResponse(responseCode = "200", description = "Portfolios obtenidos correctamente")
    @ApiResponse(responseCode = "404", description = "No se encontraron Portfolios")
    @GetMapping("/portfolio")
    public ResponseEntity<PortfolioPageResponseDto> findAllPortfolio(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        PortfolioPageResponseDto response = portfolioServiceImpl.findAllPortfolio(page, size);
        return ResponseEntity.ok(response);
    }


    @Operation(summary = "Obtener Portfolio por ID", description = "Devuelve un Portfolio por id")
    @ApiResponse(responseCode = "200", description = "Portfolio obtenido correctamente")
    @ApiResponse(responseCode = "404", description = "Portfolio no encontrado")
    @GetMapping("/portfolio/{id}")
    public ResponseEntity<PortfolioResponseDto> findPortfolioById(@PathVariable Long id) {
        PortfolioResponseDto portfolio = portfolioServiceImpl.findPortfolioById(id);
        return new ResponseEntity<>(portfolio, HttpStatus.OK);
    }


    @Operation(summary = "Guardar Portfolio", description = "Guarda un Portfolio")
    @ApiResponse(responseCode = "200", description = "Portfolio guardado correctamente")
    @ApiResponse(responseCode = "404", description = "Portfolio no guardado")
    @PostMapping("/portfolio")
    public ResponseEntity<PortfolioResponseDto> savePortfolio(@RequestBody @Valid PortfolioRequestDto portfolioRequest) {
        PortfolioResponseDto portfolio = portfolioServiceImpl.savePortfolio(portfolioRequest);
        return new ResponseEntity<>(portfolio,HttpStatus.OK);
    }


    @Operation(summary = "Actualizar Portfolio", description = "Actualiza un Portfolio")
    @ApiResponse(responseCode = "200", description = "Portfolio actualizado correctamente")
    @ApiResponse(responseCode = "404", description = "Portfolio no actualizado")
    @PatchMapping("/portfolio/{id}")
    public ResponseEntity<PortfolioResponseDto> updatePortfolio(@PathVariable Long id, @RequestBody @Valid PortfolioRequestDto portfolioRequest) {
        PortfolioResponseDto portfolio = portfolioServiceImpl.updatePortfolio(id, portfolioRequest);
        return new ResponseEntity<>(portfolio, HttpStatus.OK);
    }


    @Operation(summary = "Eliminar Portfolio", description = "Elimina un Portfolio")
    @ApiResponse(responseCode = "200", description = "Portfolio eliminado correctamente")
    @ApiResponse(responseCode = "404", description = "Portfolio no eliminado")
    @DeleteMapping("/portfolio/{id}")
    public ResponseEntity<String> deletePortfolio(@PathVariable Long id) {
        portfolioServiceImpl.deletePortfolio(id);
        return new ResponseEntity<>("Portfolio eliminado correctamente", HttpStatus.OK);
    }

    @Operation(summary = "Calcular valor total del portafolio",
            description = "Este endpoint calcula el valor total de los instrumentos en el portafolio de un usuario dado su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Valor total calculado con éxito.",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Ejemplo de respuesta exitosa",
                                    value = "{\n  \"userId\": 1,\n  \"totalValue\": 15750.50\n}"
                            )
                    )),
            @ApiResponse(responseCode = "404", description = "El usuario no fue encontrado.")
    })
    @GetMapping("/{userId}/total-value")
    public ResponseEntity<PortfolioValueResponseDto> calculateTotalValue(@PathVariable Long userId) {
        PortfolioValueResponseDto response = portfolioServiceImpl.calculateTotalValue(userId);
        return ResponseEntity.ok(response);
    }


    @Operation(summary = "Obtener todas las inversiones de un usuario",
            description = "Este endpoint devuelve todas las inversiones de un usuario dado su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inversiones encontradas con ≠ 0.",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Ejemplo de respuesta exitosa",
                                    value = "{\n  \"userId\": 1,\n  \"totalValue\": 15750.50\n}"
                            )
                    )),
            @ApiResponse(responseCode = "404", description = "El usuario no fue encontrado.")
    })
    @GetMapping("/{userId}")
    public ResponseEntity<PortfolioListResponseDto> findPortfoliosByUserId(@PathVariable Long userId) {
        PortfolioListResponseDto response = portfolioServiceImpl.findPortfoliosByUserId(userId);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Agregar o actualizar una inversión para un usuario",
            description = """
        Este endpoint permite agregar una nueva inversión o actualizar una existente para un usuario.

        ### Reglas de negocio:
        - Si el usuario no existe, se devuelve un error 404.
        - Si el instrumento no existe, se devuelve un error 404.
        - Si el portafolio ya tiene este instrumento:
          - **Si es una compra:** Se recalcula el precio promedio ponderado.
          - **Si es una venta:** Se reduce la cantidad disponible, validando que haya suficiente cantidad para vender.
        - Si el portafolio no tiene este instrumento, se crea una nueva entrada para él.

        ### Ejemplo:
        #### Escenario:
        - Un usuario tiene 100 acciones de un instrumento con un precio promedio de $10 cada una.
        - Compra 50 acciones más a $12 cada una.

        #### Cálculo:
        - Precio promedio anterior: $10
        - Cantidad anterior: 100
        - Cantidad comprada: 50
        - Precio de compra: $12
        - Nuevo precio promedio = ((100 * 10) + (50 * 12)) / (100 + 50) = 10.67
    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Inversión agregada o actualizada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Usuario o instrumento no encontrado"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/users/{userId}/investments")
    public ResponseEntity<PortfolioResponseDto> addOrUpdateInvestment(
            @PathVariable @Parameter(description = "ID del usuario que realiza la inversión", example = "1") Long userId,
            @RequestBody @Valid @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = """
            Objeto que contiene los detalles de la inversión:
            - **instrumentId**: ID del instrumento (ejemplo: 101).
            - **quantity**: Cantidad a comprar o vender (ejemplo: 50).
            - **unitPrice**: Precio unitario del instrumento (ejemplo: 12.0).
            - **transactionType**: Tipo de transacción (COMPRA o VENTA).
        """,
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = InvestmentRequestDto.class),
                            examples = @ExampleObject(
                                    name = "Ejemplo de Compra",
                                    value = """
                {
                    "instrumentId": 101,
                    "quantity": 50,
                    "unitPrice": 12.0,
                    "commission": 2.5,
                    "transactionType": "COMPRA"
                }
                """
                            )
                    )
            ) InvestmentRequestDto investmentRequest
    ) {
        PortfolioResponseDto response = portfolioServiceImpl.addOrUpdateInvestment(userId, investmentRequest);
        return ResponseEntity.ok(response);
    }


    @Operation(summary = "Eliminar una inversión",
            description = "Este endpoint elimina una inversión específica en el portafolio de un usuario dado su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Inversión eliminada con éxito."),
            @ApiResponse(responseCode = "404", description = "El usuario, instrumento o portafolio no fue encontrado."),
            @ApiResponse(responseCode = "400", description = "No se puede eliminar un portafolio con transacciones asociadas.")
    })
    @DeleteMapping("/{userId}/investments/{instrumentId}")
    public ResponseEntity<Void> deleteInvestment(
            @PathVariable Long userId,
            @PathVariable Long instrumentId) {
        portfolioServiceImpl.deleteInvestment(userId, instrumentId);
        return ResponseEntity.noContent().build();
    }

}
