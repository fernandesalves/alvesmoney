package com.alvesmoney.api.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.alvesmoney.api.model.Pessoa;
import com.alvesmoney.api.repository.PessoaRepository;

import jakarta.validation.Valid;

@Service
public class PessoaService {

	@Autowired
	private PessoaRepository pr;

	private Pessoa buscarPessoaPorCodigo(Long codigo) {

		Pessoa pessoaEncontrada = this.pr.findById(codigo).orElseThrow(() -> new EmptyResultDataAccessException(1));

		return pessoaEncontrada;
	}

	public Pessoa atualizarPessoa(Long codigo, Pessoa pessoa) {

		Pessoa pessoaUTD = buscarPessoaPorCodigo(codigo);
		BeanUtils.copyProperties(pessoa, pessoaUTD, "codigo");

		return pr.save(pessoaUTD);
	}

	public void atualizarPropriedadeIsAtivoPessoa(Long codigo, @Valid Boolean isAtivo) {

		Pessoa isAtivoUTD = buscarPessoaPorCodigo(codigo);
		isAtivoUTD.setIsAtivo(isAtivo);
		pr.save(isAtivoUTD);
	}
}
