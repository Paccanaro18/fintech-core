package com.paccanaro.fintech.usuario;

import com.paccanaro.fintech.usuario.dto.CadastroRequest;
import com.paccanaro.fintech.usuario.dto.LoginRequest;
import com.paccanaro.fintech.usuario.dto.LoginResponse;
import com.paccanaro.fintech.usuario.dto.UsuarioResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

   private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> cadastrarUsuario(@RequestBody @Valid CadastroRequest request) {

        Usuario usuario = usuarioService.cadastrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioResponse.fromEntity(usuario));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = usuarioService.login(request);
        return ResponseEntity.ok(response);
    }

}
