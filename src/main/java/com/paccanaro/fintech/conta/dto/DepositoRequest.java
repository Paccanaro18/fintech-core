package com.paccanaro.fintech.conta.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record DepositoRequest(
        @NotNull
        @DecimalMin(value = "0.01", message = "O valor deve ser maior que zero")
        BigDecimal valor,
        String descricao
) {}