package com.paccanaro.fintech.conta.dto;

import com.paccanaro.fintech.conta.Conta;

import java.math.BigDecimal;

public record ContaResponse(String numero, String agencia, BigDecimal saldo) {
    public static ContaResponse fromEntity(Conta conta) {
        return new ContaResponse(conta.getNumero(), conta.getAgencia(), conta.getSaldo());
    }
}