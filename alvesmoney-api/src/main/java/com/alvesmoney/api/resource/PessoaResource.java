package com.alvesmoney.api.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.alvesmoney.api.event.RecursoCriadoEvent;
import com.alvesmoney.api.model.Pessoa;
import com.alvesmoney.api.repository.PessoaRepository;
import com.alvesmoney.api.service.PessoaService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/pessoa")
public class PessoaResource {

	@Autowired
	private PessoaRepository pr;

	@Autowired
	private PessoaService ps;

	@Autowired
	private ApplicationEventPublisher publisher;

	@GetMapping
	public List<Pessoa> pesquisarPessoas() {

		return pr.findAll();
	}

	@GetMapping("/{codigo}")
	public ResponseEntity<Pessoa> encontrarPessoaPorCodigo(@PathVariable Long codigo) {

//		Pessoa resultado = pr.findById(codigo).orElse(null);
//		if (resultado == null) {
//			
//			throw new EmptyResultDataAccessException (String.format("Não existe pessoa com código %s ", codigo), 1); //lançando exception com mensagem personalizada
//		}
		Pessoa resultado = pr.findById(codigo).orElseThrow(() -> new EmptyResultDataAccessException(1));

		return resultado != null ? ResponseEntity.ok(resultado) : ResponseEntity.notFound().build();
	}

	@PostMapping
	public ResponseEntity<Pessoa> criarPessoa(@Valid @RequestBody Pessoa pessoa, HttpServletResponse response) {

		Pessoa novaPessoa = this.pr.save(pessoa);
		publisher.publishEvent(new RecursoCriadoEvent(this, response, novaPessoa.getCodigo()));

		return ResponseEntity.status(HttpStatus.CREATED).body(novaPessoa);
	}

	@DeleteMapping("/{codigo}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void removerPessoa(@PathVariable Long codigo) {

		this.pr.deleteById(codigo);
	}

	@PutMapping("/{codigo}")
	public ResponseEntity<Pessoa> atualizarPessoa(@PathVariable Long codigo, @Valid @RequestBody Pessoa pessoa) {

		Pessoa pessoaUTD = ps.atualizarPessoa(codigo, pessoa);

		return ResponseEntity.ok(pessoaUTD);
	}

	@PutMapping("/{codigo}/ativo")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void atualizarPropriedadeIsAtivoPessoa(@PathVariable Long codigo, @Valid @RequestBody Boolean isAtivo) {

		ps.atualizarPropriedadeIsAtivoPessoa(codigo, isAtivo);
	}
}
