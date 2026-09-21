package com.paccanaro.fintech.conta.dto;

import com.paccanaro.fintech.conta.TipoTransacao;
import com.paccanaro.fintech.conta.Transacao;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record TransacaoResponse(
        UUID id,
        TipoTransacao tipo,
        BigDecimal valor,
        LocalDateTime dataHora,
        String descricao,
        BigDecimal saldoAposOperacao
) {
    // Construtor para converter de Transacao
    public static TransacaoResponse from(Transacao transacao) {
        return new TransacaoResponse(
                transacao.getId(),
                transacao.getTipo(),
                transacao.getValor(),
                transacao.getDataHora(),
                transacao.getDescricao(),
                transacao.getSaldoAposOperacao()
        );
    }
}