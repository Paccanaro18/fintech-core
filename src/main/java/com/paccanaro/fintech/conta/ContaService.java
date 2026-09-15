package com.paccanaro.fintech.conta;

import com.paccanaro.fintech.conta.dto.TransferenciaRequest;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import com.paccanaro.fintech.conta.dto.DepositoRequest;
import com.paccanaro.fintech.conta.dto.SaqueRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

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

    @Transactional
    public Conta transferir(TransferenciaRequest request) {
        Conta contaOrigem = buscarContaLogado();

        Conta contaDestino = contaRepository.findByNumero(request.numeroContaDestino())
                .orElseThrow(() -> new IllegalArgumentException("Conta de destino não encontrada"));

        if (contaOrigem.getId().equals(contaDestino.getId())) {
            throw new IllegalArgumentException("Não é possível transferir para a própria conta");
        }

        UUID primeiroId = contaOrigem.getId().compareTo(contaDestino.getId()) < 0
                ? contaOrigem.getId() : contaDestino.getId();
        UUID segundoId = contaOrigem.getId().compareTo(contaDestino.getId()) < 0
                ? contaDestino.getId() : contaOrigem.getId();

        Conta primeiraBloqueada = contaRepository.findByIdForUpdate(primeiroId)
                .orElseThrow(() -> new IllegalArgumentException("Conta não encontrada"));
        Conta segundaBloqueada = contaRepository.findByIdForUpdate(segundoId)
                .orElseThrow(() -> new IllegalArgumentException("Conta não encontrada"));

        Conta origem = primeiraBloqueada.getId().equals(contaOrigem.getId()) ? primeiraBloqueada : segundaBloqueada;
        Conta destino = primeiraBloqueada.getId().equals(contaOrigem.getId()) ? segundaBloqueada : primeiraBloqueada;

        if (origem.getSaldo().compareTo(request.valor()) < 0) {
            throw new IllegalArgumentException("Saldo insuficiente");
        }

        origem.setSaldo(origem.getSaldo().subtract(request.valor()));
        destino.setSaldo(destino.getSaldo().add(request.valor()));

        contaRepository.save(origem);
        contaRepository.save(destino);

        registrarTransacao(origem, TipoTransacao.TRANSFERENCIA_ENVIADA, request.valor(), request.descricao());
        registrarTransacao(destino, TipoTransacao.TRANSFERENCIA_RECEBIDA, request.valor(), request.descricao());

        return origem;
    }


}