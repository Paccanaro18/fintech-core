package com.paccanaro.fintech.conta;

import com.paccanaro.fintech.conta.dto.ContaResponse;
import com.paccanaro.fintech.conta.dto.DepositoRequest;
import com.paccanaro.fintech.conta.dto.SaqueRequest;
import com.paccanaro.fintech.conta.dto.TransferenciaRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contas")
public class ContaController {

    private final ContaService contaService;

    public ContaController(ContaService contaService) {
        this.contaService = contaService;
    }

    @PostMapping("/depositar")
    public ResponseEntity<ContaResponse> depositar(@Valid @RequestBody DepositoRequest request) {
        Conta conta = contaService.depositar(request);
        return ResponseEntity.ok(ContaResponse.fromEntity(conta));
    }

    @PostMapping("/sacar")
    public ResponseEntity<ContaResponse> sacar(@Valid @RequestBody SaqueRequest request) {
        Conta conta = contaService.sacar(request);
        return ResponseEntity.ok(ContaResponse.fromEntity(conta));
    }

    @GetMapping
    public ResponseEntity<ContaResponse> visualizarSaldo() {
        Conta conta = contaService.buscarContaLogado();
        return ResponseEntity.ok(ContaResponse.fromEntity(conta));
    }

    @PostMapping("/transferir")
    public ResponseEntity<ContaResponse> transferir(@Valid @RequestBody TransferenciaRequest request) {
        Conta conta = contaService.transferir(request);
        return ResponseEntity.ok(ContaResponse.fromEntity(conta));
    }

}
