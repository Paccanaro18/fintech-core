package com.paccanaro.fintech.usuario.dto;

import com.paccanaro.fintech.usuario.Usuario;

import java.util.UUID;

public record UsuarioResponse(UUID id, String nome, String email) {
    public static UsuarioResponse fromEntity(Usuario usuario) {
        return new UsuarioResponse(usuario.getId(), usuario.getNome(), usuario.getEmail());
    }

}
