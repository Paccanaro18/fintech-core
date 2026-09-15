package com.paccanaro.fintech.usuario.dto;

import com.paccanaro.fintech.usuario.Usuario;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CadastroRequest(@NotBlank String nome,
                              @Email @NotBlank String email,
                              @NotBlank @Size(min = 8) String senha) {


}
