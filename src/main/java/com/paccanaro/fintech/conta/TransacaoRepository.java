package com.paccanaro.fintech.conta;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface TransacaoRepository extends JpaRepository<Transacao, UUID> {

    Page<Transacao> findByContaIdOrderByDataHoraDesc(UUID contaId, Pageable pageable);

}
