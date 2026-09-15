package com.paccanaro.fintech.conta;

import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import com.paccanaro.fintech.conta.dto.DepositoRequest;
import com.paccanaro.fintech.conta.dto.SaqueRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class ContaService {

    private final ContaRepository contaRepository;
    private final TransacaoRepository transacaoRepository;

    public ContaService(ContaRepository contaRepository, TransacaoRepository transacaoRepository) {
        this.contaRepository = contaRepository;
        this.transacaoRepository = transacaoRepository;
    }


    public Conta buscarContaLogado() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return contaRepository.findByUsuarioEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Conta não encontrada"));
    }

    @Transactional
    public Conta depositar(DepositoRequest request) {
        Conta conta = buscarContaLogado();

        conta.setSaldo(conta.getSaldo().add(request.valor()));
        conta = contaRepository.save(conta);

        registrarTransacao(conta, TipoTransacao.DEPOSITO, request.valor(), request.descricao());

        return conta;
    }

    @Transactional
    public Conta sacar(SaqueRequest request) {
        Conta conta = buscarContaLogado();

        if (conta.getSaldo().compareTo(request.valor()) < 0) {
            throw new IllegalArgumentException("Saldo insuficiente");
        }

        conta.setSaldo(conta.getSaldo().subtract(request.valor()));
        conta = contaRepository.save(conta);

        registrarTransacao(conta, TipoTransacao.SAQUE, request.valor(), request.descricao());

        return conta;
    }

    private void registrarTransacao(Conta conta, TipoTransacao tipo, BigDecimal valor, String descricao) {
        Transacao transacao = new Transacao();
        transacao.setConta(conta);
        transacao.setTipo(tipo);
        transacao.setValor(valor);
        transacao.setDescricao(descricao);
        transacao.setDataHora(LocalDateTime.now());

        transacaoRepository.save(transacao);
    }
}