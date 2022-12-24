package com.alvesmoney.api.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.alvesmoney.api.event.RecursoCriadoEvent;
import com.alvesmoney.api.model.Lancamento;
import com.alvesmoney.api.repository.LancamentoRespository;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/lancamento")
public class LancamentoResource {

	@Autowired
	private LancamentoRespository lr;

	@Autowired
	private ApplicationEventPublisher publisher;

	@GetMapping
	public ResponseEntity<?> pesquisarLancamentos() {

		List<Lancamento> listaLancamentos = this.lr.findAll();

		return !listaLancamentos.isEmpty() ? ResponseEntity.ok(listaLancamentos) : ResponseEntity.noContent().build();
	}

	@GetMapping("/{codigo}")
	public ResponseEntity<Lancamento> encontrarLancamentoPorCodigo(@PathVariable Long codigo) {

		Lancamento lancamento = lr.findById(codigo).orElseThrow(() -> new EmptyResultDataAccessException(1));

		return ResponseEntity.ok(lancamento);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Lancamento criarLancamento(@Valid @RequestBody Lancamento lancamento, HttpServletResponse response) {

		Lancamento novoLancamento = this.lr.save(lancamento);
		publisher.publishEvent(new RecursoCriadoEvent(this, response, novoLancamento.getCodigo()));

		return novoLancamento;
	}
}
