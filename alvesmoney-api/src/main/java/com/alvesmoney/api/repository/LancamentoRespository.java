package com.alvesmoney.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alvesmoney.api.model.Lancamento;

public interface LancamentoRespository extends JpaRepository<Lancamento, Long> {

}
