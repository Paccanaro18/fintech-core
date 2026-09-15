package com.paccanaro.fintech.usuario.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoginRequest(@NotBlank String email,
                           @NotBlank String senha
) {}
